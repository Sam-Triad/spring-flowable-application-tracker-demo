import { getAvailableUserTasks } from "$lib/api/api";
import type { Application, Page, UserTask } from "$lib/api/types";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async ({ fetch, locals }) => {
    const session = await locals.auth();

    const apiResponse: UserTask[] = await getAvailableUserTasks(
        { fetch, accessToken: session?.accessToken }
    );
    return {
        title: 'Tasks',
        apiResponse: apiResponse
    }
}
