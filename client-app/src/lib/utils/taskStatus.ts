export function getTaskStatusTag(status: string) {
    switch (status) {
        case 'CREATED':
            return { text: 'Available', class: 'govuk-tag--grey' };
        case 'COMPLETED':
            return { text: 'Done', class: 'govuk-tag--green' };
        default:
            return { text: status, class: '' };
    }
}