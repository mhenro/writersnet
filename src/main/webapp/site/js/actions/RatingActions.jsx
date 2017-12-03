import doFetch from './fetch';
import {getHost} from '../utils.jsx';

/* author tops */
export const getTopAuthorsByRating = (page) => {
    if (typeof page !== 'undefined') {
        return doFetch(getHost() + 'top/authors/rating?page=' + page + '&size=10');
    } else {
        return doFetch(getHost() + 'top/authors/rating');
    }
};

export const getTopAuthorsByBookCount = (page) => {
    if (typeof page !== 'undefined') {
        return doFetch(getHost() + 'top/authors/bookcount?page=' + page + '&size=10');
    } else {
        return doFetch(getHost() + 'top/authors/bookcount');
    }
};

export const getTopAuthorsByComments = (page) => {
    if (typeof page !== 'undefined') {
        return doFetch(getHost() + 'top/authors/comments?page=' + page + '&size=10');
    } else {
        return doFetch(getHost() + 'top/authors/comments');
    }
};

export const getTopAuthorsByViews = (page) => {
    if (typeof page !== 'undefined') {
        return doFetch(getHost() + 'top/authors/views?page=' + page + '&size=10');
    } else {
        return doFetch(getHost() + 'top/authors/views');
    }
};

/* book tops */
export const getTopBooksByNovelty = (page) => {
    if (typeof page !== 'undefined') {
        return doFetch(getHost() + 'top/books/novelties?page=' + page + '&size=10');
    } else {
        return doFetch(getHost() + 'top/books/novelties');
    }
};

export const getTopBooksByRating = (page) => {
    if (typeof page !== 'undefined') {
        return doFetch(getHost() + 'top/books/rating?page=' + page + '&size=10');
    } else {
        return doFetch(getHost() + 'top/books/rating');
    }
};

export const getTopBooksByVolume = (page) => {
    if (typeof page !== 'undefined') {
        return doFetch(getHost() + 'top/books/volume?page=' + page + '&size=10');
    } else {
        return doFetch(getHost() + 'top/books/volume');
    }
};

export const getTopBooksByComments = (page) => {
    if (typeof page !== 'undefined') {
        return doFetch(getHost() + 'top/books/comments?page=' + page + '&size=10');
    } else {
        return doFetch(getHost() + 'top/books/comments');
    }
};

export const getTopBooksByViews = (page) => {
    if (typeof page !== 'undefined') {
        return doFetch(getHost() + 'top/books/views?page=' + page + '&size=10');
    } else {
        return doFetch(getHost() + 'top/books/views');
    }
};