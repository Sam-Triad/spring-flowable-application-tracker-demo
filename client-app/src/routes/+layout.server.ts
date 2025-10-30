import { signOut } from '$lib/server/authConfig';
import type { LayoutServerLoad, Actions } from './$types';
import { redirect } from '@sveltejs/kit';

export const load: LayoutServerLoad = async (event) => {
    const session = await event.locals.auth();

    return {
        session
    };
};

// export const actions = {
// 	signout: async (event) => {
// 		await signOut(event);
// 		throw redirect(303, '/signin');
// 	}
// } satisfies Actions;