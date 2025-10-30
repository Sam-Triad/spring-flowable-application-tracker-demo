import { BASE_URL } from './constants';

export enum HttpMethod {
	GET = 'GET',
	POST = 'POST',
	PUT = 'PUT',
	DELETE = 'DELETE',
	PATCH = 'PATCH'
}

export interface RequestProperties {
	url: string;
	httpMethod: HttpMethod;
	params?: QueryParams;
}

export interface RequestConfig {
	fetch: typeof fetch;
	signal?: AbortSignal;
	accessToken?: string;
}

type QueryValue = string | number | boolean;
type QueryParams = Record<string, QueryValue>;

export function buildUrl<T extends QueryParams>(path: string, params?: T): URL {
	const url = new URL(`${BASE_URL}${path}`);
	if (params) {
		for (const [key, value] of Object.entries(params)) {
			if (value == null) continue;

			url.searchParams.append(key, String(value));
		}
	}
	return url;
}