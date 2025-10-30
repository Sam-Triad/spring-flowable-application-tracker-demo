<script lang="ts">
	import { enhance } from '$app/forms';
	import { getApplicationStatusTag } from '$lib/utils/applicationStatus';
	import type { PageData } from './$types';

	let { data }: { data: PageData } = $props();

	let application = $derived(data.apiResponse);
	let isSubmitting = $state(false);
</script>

<div class="govuk-grid-row">
	<div class="govuk-grid-column-two-thirds">
		<a href="/protected/applicant/applications" class="govuk-back-link">Back to your applications</a>

		<h1 class="govuk-heading-xl govuk-!-margin-top-6">Application details</h1>

		<h2 class="govuk-heading-m">Application information</h2>

		<dl class="govuk-summary-list">
			<div class="govuk-summary-list__row">
				<dt class="govuk-summary-list__key">Application ID</dt>
				<dd class="govuk-summary-list__value">{application.id}</dd>
			</div>

			<div class="govuk-summary-list__row">
				<dt class="govuk-summary-list__key">Applicant</dt>
				<dd class="govuk-summary-list__value">
					{data.session?.user.name} ({application.applicantUsername})
				</dd>
			</div>

			<div class="govuk-summary-list__row">
				<dt class="govuk-summary-list__key">Status</dt>
				<dd class="govuk-summary-list__value">
					<strong class="govuk-tag {getApplicationStatusTag(application.applicationStatus).class}">
						{getApplicationStatusTag(application.applicationStatus).text}
					</strong>
				</dd>
			</div>

			<div class="govuk-summary-list__row">
				<dt class="govuk-summary-list__key">Information</dt>
				<dd class="govuk-summary-list__value">
					{#if application.information}
						{application.information}
					{:else}
						<span class="govuk-hint">Not provided</span>
					{/if}
				</dd>
				{#if application.applicationStatus === 'WAITING_FOR_SUBMISSION'}
					<dd class="govuk-summary-list__actions">
						<a class="govuk-link" href="/protected/applicant/applications/{application.id}/edit">
							Change<span class="govuk-visually-hidden"> information</span>
						</a>
					</dd>
				{/if}
			</div>
		</dl>

		{#if application.applicationStatus === 'WAITING_FOR_SUBMISSION'}
			<form
				method="POST"
				action="?/submit"
				use:enhance={() => {
					isSubmitting = true;
					return async ({ update }) => {
						await update();
						isSubmitting = false;
					};
				}}
			>
				<div class="govuk-warning-text govuk-!-margin-top-6">
					<span class="govuk-warning-text__icon" aria-hidden="true">!</span>
					<strong class="govuk-warning-text__text">
						<span class="govuk-visually-hidden">Warning</span>
						Once you submit your application, you will not be able to make changes.
					</strong>
				</div>
				<button
					type="submit"
					class="govuk-button"
					data-module="govuk-button"
					disabled={isSubmitting}
				>
					{isSubmitting ? 'Submitting...' : 'Submit application'}
				</button>
			</form>
		{:else if application.applicationStatus === 'IN_REVIEW'}
			<div class="govuk-inset-text">
				Your application is currently being reviewed. We'll contact you once a decision has been
				made.
			</div>
		{:else if application.applicationStatus === 'APPROVED'}
			<div class="govuk-panel govuk-panel--confirmation">
				<h2 class="govuk-panel__title">Application approved</h2>
				<div class="govuk-panel__body">
					Your application reference<br /><strong>{application.id}</strong>
				</div>
			</div>
		{:else if application.applicationStatus === 'REJECTED'}
			<div class="govuk-warning-text">
				<span class="govuk-warning-text__icon" aria-hidden="true">!</span>
				<strong class="govuk-warning-text__text">
					<span class="govuk-visually-hidden">Warning</span>
					Your application has been rejected.
				</strong>
			</div>
		{/if}
	</div>
</div>
