<script lang="ts">
	import { signIn } from '@auth/sveltekit/client';
</script>

<div class="govuk-grid-row">
	<div class="govuk-grid-column-two-thirds">
		<h1 class="govuk-heading-xl">Sign in</h1>
		
		<p class="govuk-body">
			Sign in to your account to access the Application Tracker service.
		</p>

		<button 
			class="govuk-button" 
			data-module="govuk-button"
			on:click={() => signIn('keycloak')}
		>
			Sign in
		</button>
	</div>
</div>