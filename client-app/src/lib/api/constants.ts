export const BASE_URL = 'http://localhost:8080/api';

export const JSON_HEADERS: Record<string, string> = {
	Accept: 'application/json'
};

export const JSON_POST_HEADERS: Record<string, string> = {
	...JSON_HEADERS,
	'Content-Type': 'application/json'
};
