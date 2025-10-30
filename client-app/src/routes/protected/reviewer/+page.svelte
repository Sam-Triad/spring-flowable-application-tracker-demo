<script lang="ts">
	import GovHelp from "$lib/components/GovHelp.svelte";

</script>

<div class="govuk-grid-row">
	<div class="govuk-grid-column-two-thirds">
		<h1 class="govuk-heading-xl">Application Tracker</h1>

		<p class="govuk-body-l">Review and assess applications assigned to you.</p>

		<a
			href="/protected/reviewer/applications"
			role="button"
			draggable="false"
			class="govuk-button govuk-button--start"
			data-module="govuk-button"
		>
			View available applications
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

		<h2 class="govuk-heading-m">Your responsibilities</h2>

		<ul class="govuk-list govuk-list--bullet">
			<li>Review applications thoroughly</li>
			<li>Approve or reject the applications</li>
		</ul>

		<GovHelp/>
	</div>
</div>
