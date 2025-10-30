<script lang="ts">
	import { enhance } from '$app/forms';
	import type { PageData } from './$types';

	let { data }: { data: PageData } = $props();

	let application = $derived(data.apiResponse);
</script>

<div class="govuk-grid-row">
	<div class="govuk-grid-column-two-thirds">
		<a href="/protected/applicant/applications/{application.id}" class="govuk-back-link">Back to application</a>

		<h1 class="govuk-heading-xl govuk-!-margin-top-6">Edit Application Information</h1>

		<form method="POST" action="?/update" use:enhance>
			<div class="govuk-form-group">
				<label class="govuk-label govuk-label--m" for="information">
					Application information
				</label>
				<div id="information-hint" class="govuk-hint">
					Provide any additional information about your application.
				</div>
				<textarea
					class="govuk-textarea"
					id="information"
					name="information"
					rows="8"
					aria-describedby="information-hint"
					value={application.information || ''}
				></textarea>
			</div>

			<div class="govuk-button-group">
				<button type="submit" class="govuk-button" data-module="govuk-button">
					Save and continue
				</button>
				<a class="govuk-link" href="/protected/applicant/applications/{application.id}">Cancel</a>
			</div>
		</form>
	</div>
</div>
