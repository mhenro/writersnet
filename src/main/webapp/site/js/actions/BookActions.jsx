import doFetch from './fetch';
import {getHost} from '../utils.jsx';

export const getBooks = (page) => {
    if (page) {
        return doFetch(getHost() + 'books?page=' + page + '&size=20');
    } else {
        return doFetch(getHost() + 'books');
    }
};

export const getSeries = (page) => {
    if (page) {
        return doFetch(getHost() + 'series?page=' + page + '&size=20');
    } else {
        return doFetch(getHost() + 'series');
    }
};

export const getGenres = () => {
    return doFetch(getHost() + 'genres');
};

export const addStar = (bookId, starValue) => {
    return doFetch(getHost() + 'books/' + bookId + '/rating/' + starValue);
};

export const getBookDetails = (bookId) => {
    return doFetch(getHost() + 'books/' + bookId);
};

export const getBookComments = (bookid, page) => {
    return doFetch(getHost() + 'books/' + bookid + '/comments?page=' + page + '&size=10');
};

export const saveComment = (comment) => {
    return doFetch(getHost() + 'books/comments', comment);
};

export const deleteComment = (bookId, commentId, token) => {
    return doFetch(getHost() + 'books/' + bookId + '/comments/' + commentId, 'DELETE', token);
};

export const saveBook = (book, token) => {
    return doFetch(getHost() + 'books', book, token);
};

export const saveBookText = (bookText, token) => {
    return doFetch(getHost() + 'text', bookText, token, 'multipart/form-data')
};

export const saveCover = (cover, token) => {
    return doFetch(getHost() + 'cover', cover, token, 'multipart/form-data');
};

export const deleteBook = (bookTextRequest, token) => {
    return doFetch(getHost() + 'books/' + bookTextRequest, 'DELETE', token);
};

export const SET_BOOKS = 'SET_BOOKS';
export const SET_BOOK = 'SET_BOOK';
export const SET_SERIES = 'SET_SERIES';
export const SET_GENRES = 'SET_GENRES';

export const setBooks = (books) => {
    return {
        type: SET_BOOKS,
        books
    }
};

export const setBook = (book) => {
    return {
        type: SET_BOOK,
        book
    }
};

export const setSeries = (series) => {
    return {
        type: SET_SERIES,
        series
    }
};

export const setGenres = (genres) => {
    return {
        type: SET_GENRES,
        genres
    }
};