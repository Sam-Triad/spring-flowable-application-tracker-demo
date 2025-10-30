import { JSON_HEADERS, JSON_POST_HEADERS } from './constants';
import { ApiError, type ProblemDetails } from './errors';
import { buildUrl, type RequestConfig, HttpMethod, type RequestProperties } from './fetchHelpers';
import type {
	Application,
	Page,
	SortDirection,
	UserTask
} from './types';

export async function makeRequest<T>(
	config: RequestConfig,
	properties: RequestProperties,
	payload?: any
): Promise<T> {
	console.log("makeRequest");
	const url = buildUrl(properties.url, properties.params);
	const $fetch = config.fetch;

	const headers = payload ? { ...JSON_POST_HEADERS } : { ...JSON_HEADERS };
	if (config.accessToken) {
		headers['Authorization'] = `Bearer ${config.accessToken}`;
	}

	const res = await $fetch(url.toString(), {
		method: properties.httpMethod,
		signal: config.signal,
		body: JSON.stringify(payload),
		headers
	});

	if (!res.ok) {
		console.error(`API request failed: ${res.status} ${res.statusText}`);

		let body: ProblemDetails;
		try {
			body = (await res.json()) as ProblemDetails;
		} catch (error) {
			body = {
				type: 'about:blank',
				instance: '',
				title: res.statusText,
				status: res.status,
				detail: 'No additional details provided'
			};
		}

		console.error('API Error Response:', res);
		console.error('API Error Details:', body);
		throw new ApiError(body);
	}

	const contentLength = res.headers.get('Content-Length');
	const contentType = res.headers.get('Content-Type');
	if (contentLength === '0' || res.status === 204 || contentType === null) {
		return undefined as unknown as T;
	}
	const text = await res.text();
	if (!text) {
		return undefined as unknown as T;
	}

	if (contentType.includes('application/json')) {
		try {
			return JSON.parse(text) as T;
		} catch (error) {
			console.error('Failed to parse JSON response:', error);
			throw new Error('Failed to parse JSON response');
		}
	}

	throw new Error('Expected JSON response from API');
}

export async function decideApproval(
	userTaskKey: string,
	approved: boolean,
	config: RequestConfig
): Promise<Application> {
	return makeRequest<Application>(
		config,
		{
			url: `/tasks/decide-approval`,
			httpMethod: HttpMethod.POST
		},
		{ userTaskKey, approved }
	);
}

export async function claimUserTask(
	key: string,
	config: RequestConfig
): Promise<UserTask> {
	return makeRequest<UserTask>(config, {
		url: `/tasks/claim-task/${key}`,
		httpMethod: HttpMethod.POST
	})
}


export async function getUserTask(
	key: string,
	config: RequestConfig
): Promise<UserTask> {
	return makeRequest<UserTask>(config, {
		url: `/tasks/${key}`,
		httpMethod: HttpMethod.GET
	})
}


export async function getAvailableUserTasks(
	config: RequestConfig
): Promise<UserTask[]> {
	return makeRequest<UserTask[]>(config, {
		url: '/tasks/available',
		httpMethod: HttpMethod.GET
	});
}

export async function getUserTasksInReview(
	config: RequestConfig
): Promise<UserTask[]> {
	return makeRequest<UserTask[]>(config, {
		url: '/tasks/in-review',
		httpMethod: HttpMethod.GET
	});
}

export async function getApplications(
	params: {
		page?: number;
		size?: number;
		sortDir?: SortDirection;
	},
	config: RequestConfig
): Promise<Page<Application>> {
	return makeRequest<Page<Application>>(config, {
		url: '/applications',
		httpMethod: HttpMethod.GET,
		params: params
	});
}

export async function getApplication(
	applicationId: string,
	config: RequestConfig
): Promise<Application> {
	return makeRequest<Application>(config, {
		url: `/applications/${applicationId}`,
		httpMethod: HttpMethod.GET
	});
}

export async function updateApplication(
	applicationId: string,
	information: string,
	config: RequestConfig
): Promise<Application> {
	return makeRequest<Application>(
		config,
		{
			url: `/applications/${applicationId}`,
			httpMethod: HttpMethod.PUT
		},
		{ information }
	);
}

export async function submitApplication(
	applicationId: string,
	config: RequestConfig
): Promise<void> {
	return makeRequest<void>(
		config,
		{
			url: `/applications/${applicationId}/submit`,
			httpMethod: HttpMethod.POST
		}
	);
}