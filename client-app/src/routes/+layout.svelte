<script lang="ts">
	import '../app.css';
	import 'govuk-frontend/dist/govuk/govuk-frontend.min.css';
	import { onMount } from 'svelte';
	import favicon from '$lib/assets/favicon.svg';
	import { page } from '$app/stores';
	import { enhance } from '$app/forms';

	let { children, data } = $props();

	onMount(async () => {
		const { initAll } = await import('govuk-frontend');
		initAll();
	});

	let pageTitle = $derived($page.data.title || '');

</script>

<svelte:head>
	<link rel="icon" href={favicon} />
</svelte:head>

<header class="govuk-header" data-module="govuk-header">
	<div class="govuk-header__container govuk-width-container">
		<div class="govuk-header__content">
			<a href="/" class="govuk-header__link govuk-header__service-name"> Application Tracker </a>
		</div>
	</div>
</header>

<div class="govuk-width-container">
	{#if data.session?.user}
		<div class="govuk-!-padding-top-2 govuk-!-padding-bottom-2" style="border-bottom: 1px solid #b1b4b6;">
			<div style="display: flex; justify-content: space-between; align-items: center;">
				<span class="govuk-body govuk-!-margin-bottom-0">
					{pageTitle}
				</span>
				<form method="POST" action="/signout" use:enhance style="display: inline; margin: 0;">
					<button type="submit" class="govuk-link" style="background: none; border: none; padding: 0; cursor: pointer;">
						Sign out
					</button>
				</form>
			</div>
		</div>
	{/if}

	<main class="govuk-main-wrapper" id="main-content">
		{#if data.session?.user}
			<span class="govuk-caption-l" style="text-transform: uppercase; color: #505a5f;">
				{data.session.user.name || data.session.user.email || 'User'}
			</span>
		{/if}
		{@render children?.()}
	</main>
</div>

<style>
	:global(body) {
		margin: 0;
	}
</style>
