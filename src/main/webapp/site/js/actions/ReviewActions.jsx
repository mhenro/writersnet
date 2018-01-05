import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const saveReview = (review, token) => {
    return doFetch(getHost() + 'review', review, token);
};

export const getReviews = (bookId, page = 0, size = 5) => {
    if (bookId !== -1) {
        return doFetch(getHost() + 'reviews/' + bookId + '?page=' + page + '&size=' + size);
    } else {
        return doFetch(getHost() + 'reviews?page=' + page + '&size=' + size);
    }
};