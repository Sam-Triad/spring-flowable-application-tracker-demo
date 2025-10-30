import { error, redirect } from '@sveltejs/kit';
import type { PageServerLoad } from './signin/$types';

export const load: PageServerLoad = async (event) => {
	const session = await event.locals.auth();
	
	if (!session?.user) {
		// Not logged in - redirect to signin
		throw redirect(303, '/signin');
	}
	
	// Logged in - check roles and redirect accordingly
	const roles = session.user.roles || [];
    console.error('User roles:', roles);
	
	// Priority order: check roles in order of precedence
	if (roles.includes('Applicant')) {
		throw redirect(303, '/protected/applicant');
	}
	
	if (roles.includes('Verifier') || roles.includes('First_Level_Admin')|| roles.includes('Second_Level_Admin')) {
		throw redirect(303, '/protected/reviewer');
	}
	
	// Default redirect for authenticated users without specific roles
	throw error(500, 'No valid role assigned to the user.');
};