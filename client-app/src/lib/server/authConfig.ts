import {
    KEYCLOAK_CLIENT_ID,
    KEYCLOAK_CLIENT_SECRET,
    KEYCLOAK_ISSUER,
    AUTH_SECRET
} from '$env/static/private';
import type { Provider } from '@auth/core/providers';
import Keycloak, { type KeycloakProfile } from '@auth/core/providers/keycloak';
import { SvelteKitAuth } from "@auth/sveltekit";

const keycloak: Provider = Keycloak({
    clientId: KEYCLOAK_CLIENT_ID,
    clientSecret: KEYCLOAK_CLIENT_SECRET,
    issuer: KEYCLOAK_ISSUER
});

async function refreshAccessToken(token: any) {
    try {
        const url = `${KEYCLOAK_ISSUER}/protocol/openid-connect/token`;

        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                client_id: KEYCLOAK_CLIENT_ID,
                client_secret: KEYCLOAK_CLIENT_SECRET,
                grant_type: 'refresh_token',
                refresh_token: token.refreshToken,
            }),
        });

        const refreshedTokens = await response.json();

        if (!response.ok) {
            throw refreshedTokens;
        }

        return {
            ...token,
            accessToken: refreshedTokens.access_token,
            expiresAt: Math.floor(Date.now() / 1000) + refreshedTokens.expires_in,
            refreshToken: refreshedTokens.refresh_token ?? token.refreshToken, // Fall back to old refresh token
            idToken: refreshedTokens.id_token ?? token.idToken,
        };
    } catch (error) {
        console.error('Error refreshing access token:', error);
        return {
            ...token,
            error: 'RefreshAccessTokenError',
        };
    }
}

export const { handle, signIn, signOut } = SvelteKitAuth({
    providers: [keycloak],
    secret: AUTH_SECRET,
    trustHost: true,
    pages: {
        signIn: '/signin'
    },
    callbacks: {
        async jwt({ token, account, profile }) {
            if (account && profile) {
                const keycloakProfile = profile as KeycloakProfile;

                return {
                    accessToken: account.access_token,
                    refreshToken: account.refresh_token,
                    expiresAt: account.expires_at,
                    idToken: account.id_token,
                    roles: keycloakProfile.groups || [],
                    username: keycloakProfile.preferred_username,
                };
            }

            // Return previous token if the access token has not expired yet
            if (Date.now() < (token.expiresAt as number) * 1000) {
                return token;
            }

            // Access token has expired, try to refresh it
            console.log('Token expired, refreshing...');
            return await refreshAccessToken(token);
        },
        async session({ session, token }) {
            // Send roles to the client in the session
            if (session.user) {
                session.user.roles = (token.roles as string[]) || [];
                session.user.username = token.username;
            }
            // Add access token to session
            session.accessToken = token.accessToken as string;
            session.idToken = token.idToken as string;

            return session;
        }
    },
    events: {
        async signOut(message) {
            if ('token' in message && message.token) {
                const keycloakLogoutUrl = new URL(`${KEYCLOAK_ISSUER}/protocol/openid-connect/logout`);

                keycloakLogoutUrl.searchParams.set('post_logout_redirect_uri', 'http://localhost:5173/signin');
                keycloakLogoutUrl.searchParams.set('id_token_hint', message.token.idToken as string); // Prevents confirm logout page

                try {
                    await fetch(keycloakLogoutUrl.toString());
                } catch (error) {
                    console.error('Failed to logout from Keycloak:', error);
                }
            }
        }
    }
});