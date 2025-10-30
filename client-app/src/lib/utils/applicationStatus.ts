export function getApplicationStatusTag(status: string) {
    switch (status) {
        case 'WAITING_FOR_SUBMISSION':
            return { text: 'In Progress', class: 'govuk-tag--grey' };
        case 'IN_REVIEW':
            return { text: 'In Review', class: 'govuk-tag--blue' };
        case 'APPROVED':
            return { text: 'Approved', class: 'govuk-tag--green' };
        case 'REJECTED':
            return { text: 'Rejected', class: 'govuk-tag--red' };
        default:
            return { text: status, class: '' };
    }
}