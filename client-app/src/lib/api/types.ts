export type Page<T> = {
	content: T[];
	pageable: Record<string, unknown>;
	totalPages: number;
	totalElements: number;
	size: number;
	number: number;
	first: boolean;
	last: boolean;
	empty: boolean;
};

export interface Application {
    id: string;
    applicantUsername: string;
    information: string;
    applicationStatus: string;
}

export type SortDirection = 'ASC' | 'DESC';

export interface PageParams {
	page?: number;
	size?: number;
	sortDir?: SortDirection;
}

export interface UserTask {
	key: number;
	elementId: string;
	applicationId: string;
	assigneeUsername: string;
	state: 'CREATED' | 'COMPLETED';
}