export interface ProblemDetails {
	type: string;
	title: string;
	status: number;
	detail: string;
	instance: string;
}

export class ApiError extends Error {
	type: string;
	title: string;
	status: number;
	detail: string;
	instance: string;

	constructor(problemDetails: ProblemDetails) {
		super(problemDetails.title);
		this.name = 'ApiError';
		this.type = problemDetails.type;
		this.title = problemDetails.title;
		this.status = problemDetails.status;
		this.detail = problemDetails.detail;
		this.instance = problemDetails.instance;
	}
}