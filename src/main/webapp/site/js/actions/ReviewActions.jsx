import doFetch from './fetch.jsx';
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

export const getReviewDetails = (reviewId) => {
    return doFetch(getHost() + 'review/' + reviewId);
};

export const likeReview = (reviewId) => {
    return doFetch(getHost() + 'review/' + reviewId + '/like');
};

export const dislikeReview = (reviewId) => {
    return doFetch(getHost() + 'review/' + reviewId + '/dislike');
};

export const OPEN_REVIEW_READER_FORM = 'OPEN_REVIEW_READER_FORM';
export const CLOSE_REVIEW_READER_FORM = 'CLOSE_REVIEW_READER_FORM';

export const openReviewReaderForm = (review) => {
    return {
        type: OPEN_REVIEW_READER_FORM,
        review
    }
};

export const closeReviewReaderForm = () => {
    return {
        type: CLOSE_REVIEW_READER_FORM
    }
};