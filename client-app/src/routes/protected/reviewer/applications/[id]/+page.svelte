<script lang="ts">
	import { enhance } from '$app/forms';
	import type { PageData } from './$types';

	let { data }: { data: PageData } = $props();

	let application = $derived(data.application);
	let task = $derived(data.task);
	let isClaimed = $derived(task.assigneeUsername === data.session?.user?.username);
	let isProcessing = $state(false);
</script>

<div class="govuk-grid-row">
	<div class="govuk-grid-column-two-thirds">
		<a href="/protected/reviewer/applications" class="govuk-back-link">Back to available tasks</a>

		<h1 class="govuk-heading-xl govuk-!-margin-top-6">Review application</h1>

		<h2 class="govuk-heading-m">Application information</h2>

		<dl class="govuk-summary-list">
			<div class="govuk-summary-list__row">
				<dt class="govuk-summary-list__key">Application ID</dt>
				<dd class="govuk-summary-list__value">{application.id}</dd>
			</div>

			<div class="govuk-summary-list__row">
				<dt class="govuk-summary-list__key">Applicant</dt>
				<dd class="govuk-summary-list__value">
					{application.applicantUsername}
				</dd>
			</div>

			{#if task.assigneeUsername}
				<div class="govuk-summary-list__row">
					<dt class="govuk-summary-list__key">Assigned to</dt>
					<dd class="govuk-summary-list__value">
						{#if isClaimed}
							<strong class="govuk-tag govuk-tag--blue">You</strong>
						{/if}
					</dd>
				</div>
			{/if}

			<div class="govuk-summary-list__row">
				<dt class="govuk-summary-list__key">Information</dt>
				<dd class="govuk-summary-list__value">
					{#if application.information}
						{application.information}
					{:else}
						<span class="govuk-hint">Not provided</span>
					{/if}
				</dd>
			</div>
		</dl>
		{#if task.state === 'CREATED'}
			{#if !task.assigneeUsername}
				<!-- Unclaimed task -->
				<form
					method="POST"
					action="?/claim"
					use:enhance={() => {
						isProcessing = true;
						return async ({ update }) => {
							await update();
							isProcessing = false;
						};
					}}
				>
					<div class="govuk-inset-text">
						This application is available to review. Claim it to start your review.
					</div>
					<button
						type="submit"
						class="govuk-button"
						data-module="govuk-button"
						disabled={isProcessing}
					>
						{isProcessing ? 'Claiming...' : 'Claim task'}
					</button>
				</form>
			{:else if isClaimed}
				<!-- Claimed by current user -->
				<h2 class="govuk-heading-m govuk-!-margin-top-6">Make a decision</h2>

				<p class="govuk-body">
					Review the application information above and decide whether to approve or reject this
					application.
				</p>

				<div class="govuk-button-group govuk-!-margin-top-6">
					<form
						method="POST"
						action="?/approve"
						use:enhance={() => {
							isProcessing = true;
							return async ({ update }) => {
								await update();
								isProcessing = false;
							};
						}}
					>
						<button
							type="submit"
							class="govuk-button"
							data-module="govuk-button"
							disabled={isProcessing}
						>
							{isProcessing ? 'Processing...' : 'Approve application'}
						</button>
					</form>

					<form
						method="POST"
						action="?/reject"
						use:enhance={() => {
							isProcessing = true;
							return async ({ update }) => {
								await update();
								isProcessing = false;
							};
						}}
					>
						<button
							type="submit"
							class="govuk-button govuk-button--warning"
							data-module="govuk-button"
							disabled={isProcessing}
						>
							{isProcessing ? 'Processing...' : 'Reject application'}
						</button>
					</form>
				</div>
			{:else}
				<!-- Claimed by another user -->
				<div class="govuk-warning-text">
					<span class="govuk-warning-text__icon" aria-hidden="true">!</span>
					<strong class="govuk-warning-text__text">
						<span class="govuk-visually-hidden">Warning</span>
						This application is currently being reviewed by {task.assigneeUsername}.
					</strong>
				</div>
			{/if}
		{:else if task.state === 'COMPLETED'}
			<!-- Task completed - GDS compliant notification -->
			<div
				class="govuk-notification-banner govuk-notification-banner--success govuk-!-margin-top-6"
				role="alert"
				aria-labelledby="govuk-notification-banner-title"
				data-module="govuk-notification-banner"
			>
				<div class="govuk-notification-banner__header">
					<h2 class="govuk-notification-banner__title" id="govuk-notification-banner-title">
						Success
					</h2>
				</div>
				<div class="govuk-notification-banner__content">
					<h3 class="govuk-notification-banner__heading">Review completed</h3>
					<p class="govuk-body">
						Your decision has been recorded and the applicant has been notified.
					</p>
				</div>
			</div>

			<p class="govuk-body">
				<a href="/protected/reviewer/applications" class="govuk-link">Return to task list</a>
			</p>
		{/if}
	</div>
</div>
