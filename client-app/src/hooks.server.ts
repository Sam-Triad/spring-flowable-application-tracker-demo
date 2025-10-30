import { redirect, error, type Handle } from '@sveltejs/kit';
import { sequence } from '@sveltejs/kit/hooks';
import { handle as authConfig } from '$lib/server/authConfig';
import { env } from '$env/dynamic/private';

const authentication: Handle = async ({ event, resolve }) => {

	// Public routes
	const exceptions = [
		'/',
		'/signin',
		'/signout',
	];

	if (exceptions.includes(event.url.pathname)) {
		console.debug('DEBUG: ', `Route ${event.url.pathname} is public.`);
		return resolve(event);
	}

	// API
	if (event.url.pathname.startsWith('/api')) {
		console.debug('DEBUG: ', `Secure API route ${event.url.pathname} is called.`);
		const authHeader = event.request.headers.get('Authorization');

		if (!authHeader) {
			return error(401, 'Unauthorized');
		}

		const [, token] = authHeader.split(' ');
		if (!token || token !== env.API_SECRET) {
			return error(401, 'Unauthorized');
		}
		return resolve(event);
	}

	// Non API
	let session = await event.locals.auth();
	if (session) {
		console.debug('DEBUG: ', `Session protected route ${event.url.pathname} is called.`);
		return resolve(event);
	}

	// If user is not signed in or session is no longer valid, redirect to login for all other routes
	return redirect(303, '/signin');
};

// First handle authentication
// Each function acts as a middleware, receiving the request handle
// And returning a handle which gets passed to the next function
export const handle: Handle = sequence(authConfig, authentication);
