<script lang="ts">
	import GovHelp from '$lib/components/GovHelp.svelte';
</script>

<div class="govuk-grid-row">
	<div class="govuk-grid-column-two-thirds">
		<h1 class="govuk-heading-xl">Application Tracker</h1>

		<p class="govuk-body-l">View and manage your applications.</p>

		<a
			href="/protected/applicant/applications"
			role="button"
			draggable="false"
			class="govuk-button govuk-button--start"
			data-module="govuk-button"
		>
			View my applications
			<svg
				class="govuk-button__start-icon"
				xmlns="http://www.w3.org/2000/svg"
				width="17.5"
				height="19"
				viewBox="0 0 33 40"
				aria-hidden="true"
				focusable="false"
			>
				<path fill="currentColor" d="M0 0h13l20 20-20 20H0l20-20z" />
			</svg>
		</a>

		<h2 class="govuk-heading-m">Before you start</h2>

		<p class="govuk-body">
			You can save your application and return to it later. You'll need to complete all required
			sections before submitting.
		</p>

		<GovHelp />
	</div>
</div>
