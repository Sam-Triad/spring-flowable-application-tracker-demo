<script lang="ts">
	import { getTaskStatusTag } from '$lib/utils/taskStatus';
	import type { PageData } from './$types';

	let { data }: { data: PageData } = $props();

	let tasks = $derived(data.apiResponse);
</script>

<div class="govuk-grid-row">
	<div class="govuk-grid-column-two-thirds">
		<h1 class="govuk-heading-xl">Available tasks</h1>

		{#if tasks.length === 0}
			<p class="govuk-body">No available tasks.</p>
		{:else}
			<p class="govuk-body">
				{tasks.length}
				{tasks.length === 1 ? 'task' : 'tasks'} available to review.
			</p>

			{#each tasks as task, index}
				<div class="govuk-!-margin-bottom-8">
					<h2 class="govuk-heading-l">
						{task.elementId.split('_').join(' ')}
					</h2>

					<dl class="govuk-summary-list govuk-!-margin-bottom-4">
						<div class="govuk-summary-list__row">
							<dt class="govuk-summary-list__key">Task ID</dt>
							<dd class="govuk-summary-list__value">{task.key}</dd>
						</div>
						<div class="govuk-summary-list__row">
							<dt class="govuk-summary-list__key">Status</dt>
							<dd class="govuk-summary-list__value">
								{getTaskStatusTag(task.state).text}
							</dd>
						</div>
					</dl>

					<p class="govuk-body">
						<a href="/protected/reviewer/applications/{task.key}" class="govuk-link"
							>View application details</a
						>
					</p>
				</div>

				{#if index !== tasks.length - 1}
					<hr class="govuk-section-break govuk-section-break--xl govuk-section-break--visible" />
				{/if}
			{/each}
		{/if}
	</div>
</div>
