import { signOut } from "$lib/server/authConfig";
import type { Actions } from "./$types";
import { KEYCLOAK_ISSUER } from '$env/static/private';
import { redirect } from '@sveltejs/kit';

export const actions: Actions = {
    default: async (event) => {
        const { url } = event;
        const session = await event.locals.auth();
        
        // Sign out from SvelteKit
        await signOut(event);
        
        // Build Keycloak logout URL
        const keycloakLogoutUrl = new URL(`${KEYCLOAK_ISSUER}/protocol/openid-connect/logout`);
        keycloakLogoutUrl.searchParams.set('post_logout_redirect_uri', `${url.origin}/signin`);
        
        // Optionally include id_token_hint for better logout
        if (session?.idToken) {
            keycloakLogoutUrl.searchParams.set('id_token_hint', session.idToken as string);
        }
        
        // Redirect to Keycloak logout
        throw redirect(303, keycloakLogoutUrl.toString());
    }
};