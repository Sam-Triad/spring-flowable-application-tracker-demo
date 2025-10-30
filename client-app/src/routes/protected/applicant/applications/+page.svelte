<script lang="ts">
	import { getApplicationStatusTag } from '$lib/utils/applicationStatus';
	import type { PageData } from './$types';

	let { data }: { data: PageData } = $props();
	
	let applications = $derived(data.apiResponse);
	let content = $derived(applications.content);
	let empty = $derived(applications.empty);
	let totalElements = $derived(applications.totalElements);
</script>

<div class="govuk-grid-row">
	<div class="govuk-grid-column-two-thirds">
		<h1 class="govuk-heading-xl">Your applications</h1>

		{#if empty}
			<p class="govuk-body">You have no applications.</p>
		{:else}
			<p class="govuk-body">
				You have {totalElements}
				{totalElements === 1 ? 'application' : 'applications'}.
			</p>

			{#each content as application, index}
				<div class="govuk-!-margin-bottom-8">
					<h2 class="govuk-heading-l">
						Test Application
						<strong
							class="govuk-tag {getApplicationStatusTag(application.applicationStatus)
								.class} govuk-!-margin-left-2"
						>
							{getApplicationStatusTag(application.applicationStatus).text}
						</strong>
					</h2>

					<dl class="govuk-summary-list govuk-!-margin-bottom-4">
						<div class="govuk-summary-list__row">
							<dt class="govuk-summary-list__key">Application ID</dt>
							<dd class="govuk-summary-list__value">{application.id}</dd>
						</div>
						<div class="govuk-summary-list__row">
							<dt class="govuk-summary-list__key">Status</dt>
							<dd class="govuk-summary-list__value">
								{getApplicationStatusTag(application.applicationStatus).text}
							</dd>
						</div>
						{#if application.information}
							<div class="govuk-summary-list__row">
								<dt class="govuk-summary-list__key">Information</dt>
								<dd class="govuk-summary-list__value">{application.information}</dd>
							</div>
						{/if}
					</dl>

					<p class="govuk-body">
						<a href="/protected/applicant/applications/{application.id}" class="govuk-link">View application details</a>
					</p>
				</div>

				{#if index !== content.length - 1}
					<hr class="govuk-section-break govuk-section-break--xl govuk-section-break--visible" />
				{/if}
			{/each}
		{/if}
	</div>
</div>