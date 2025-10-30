import { getApplication, getUserTask, claimUserTask, decideApproval } from "$lib/api/api";
import type { Application, UserTask } from "$lib/api/types";
import { error, redirect, type Actions } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { submitApplication } from "$lib/api/api";

export const load: PageServerLoad = async ({ fetch, locals, params }) => {
    const session = await locals.auth();

    const task: UserTask = await getUserTask(
        params.id,
        { fetch, accessToken: session?.accessToken }
    )

    const application: Application = await getApplication(
        task.applicationId,
        { fetch, accessToken: session?.accessToken }
    );

    return {
        title: 'Task',
        application,
        task
    }
}

export const actions: Actions = {
    claim: async ({ params, fetch, locals }) => {
        const session = await locals.auth();

        if (!params.id) {
            throw error(400, 'Task Key is required');
        }

        await claimUserTask(
            params.id,
            { fetch, accessToken: session?.accessToken }
        );

        throw redirect(303, `/protected/reviewer/applications/${params.id}`);

    },
    approve: async ({ params, fetch, locals }) => {
        const session = await locals.auth();

        if (!params.id) {
            throw error(400, 'Task Key is required');
        }

        await decideApproval(
            params.id, true,
            { fetch, accessToken: session?.accessToken }
        );

        throw redirect(303, `/protected/reviewer/applications/${params.id}`);

    },
    reject: async ({ params, fetch, locals }) => {
        const session = await locals.auth();

        if (!params.id) {
            throw error(400, 'Task Key is required');
        }

        await decideApproval(
            params.id, false,
            { fetch, accessToken: session?.accessToken }
        );

        throw redirect(303, `/protected/reviewer/applications/${params.id}`);

    },
};