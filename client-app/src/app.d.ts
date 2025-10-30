import type { DefaultSession } from '@auth/core/types';

declare module '@auth/core/types' {
	interface Session {
		idToken?: string;
		accessToken?: string;
		user: {
			roles?: string[];
			username?: string;
		} & DefaultSession['user'];
	}
}

declare module '@auth/core/jwt' {
	interface JWT {
		idToken?: string;
		username?: string;
		accessToken?: string;
		refreshToken?: string;
		expiresAt?: number;
		roles?: string[];
	}
}
// See https://svelte.dev/docs/kit/types#app.d.ts
// for information about these interfaces
declare global {
	namespace App {
		interface Locals {
			auth: () => Promise<Session | null>;
		}
		// interface Error {}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}
}

export { };
