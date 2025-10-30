import { getApplications } from "$lib/api/api";
import type { Application, Page } from "$lib/api/types";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async ({ fetch, locals }) => {
    const session = await locals.auth();

    const apiResponse: Page<Application> = await getApplications(
        {
            page: 0,
            size: 10,
            sortDir: 'DESC'
        },
        { fetch, accessToken: session?.accessToken }
    );
    return {
        title: 'Application Dashboard',
        apiResponse: apiResponse
    }
}
