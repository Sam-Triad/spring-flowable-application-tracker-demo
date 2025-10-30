import { getApplication } from "$lib/api/api";
import type { Application } from "$lib/api/types";
import { error, redirect, type Actions } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { submitApplication } from "$lib/api/api";

export const load: PageServerLoad = async ({ fetch, locals, params }) => {
    const session = await locals.auth();

    const apiResponse: Application = await getApplication(
        params.id,
        { fetch, accessToken: session?.accessToken }
    );
    return {
        title: 'Application',
        apiResponse: apiResponse
    }
}

export const actions: Actions = {
    submit: async ({ request, params, fetch, locals }) => {
        const session = await locals.auth();

		if (!params.id) {
			throw error(400, 'Application ID is required');
		}

        await submitApplication(
            params.id,
            {fetch, accessToken: session?.accessToken}
        );

        // Redirect to update status via API
        throw redirect(303, `/protected/applicant/applications/${params.id}`);
    }
};