import doFetch from './fetch.jsx';
import {getHost} from '../utils.jsx';

export const getBooks = (name, genre = null, language = null, page = 0, size = 5, sort = 'name') => {
    let api = getHost() + 'books';
    if (name) {
        api += '/name/' + name;
    }
    api += '?page=' + page + '&size=' + size + '&sort=' + sort;
    if (genre) {
        api += '&genre=' + genre;
    }
    if (language) {
        api += '&language=' + language;
    }
    return doFetch(api);
};

export const getBooksByAuthor = (authorId, page = 0, size = 200) => {
    return doFetch(getHost() + 'books/author/' + authorId + '?page=' + page + '&size=' + size);
};

export const getBooksCount = () => {
    return doFetch(getHost() + 'count/books');
};

export const getSeries = (userId, page = 0, size = 200) => {
    return doFetch(getHost() + 'series/' + userId + '?page=' + page + '&size=' + size);
};

export const saveSerie = (bookSerie, token) => {
    return doFetch(getHost() + 'series', bookSerie, token);
};

export const deleteSerie = (id, token) => {
    return doFetch(getHost() + 'series/' + id, 'DELETE', token);
};

export const getGenres = () => {
    return doFetch(getHost() + 'genres');
};

export const addStar = (bookId, starValue) => {
    return doFetch(getHost() + 'books/' + bookId + '/rating/' + starValue);
};

export const getBookDetails = (bookId, token = null, page = 0, size = 10000) => {
    return doFetch(getHost() + 'books/' + bookId + '?page=' + page + '&size=' + size, null, token);
};

export const getBookComments = (bookid, page = 0, size = 10) => {
    return doFetch(getHost() + 'books/' + bookid + '/comments?page=' + page + '&size=' + size);
};

export const getAllComments = (page = 0, size = 20) => {
    return doFetch(getHost() + 'books/comments?page=' + page + '&size=' + size);
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

export const restoreDefaultCover = (bookId, token) => {
    return doFetch(getHost() + 'cover/restore/' + bookId, null, token);
};

export const deleteBook = (bookTextRequest, token) => {
    return doFetch(getHost() + 'books/' + bookTextRequest, 'DELETE', token);
};

export const getBookCost = (bookId) => {
    return doFetch(getHost() + 'books/cost/' + bookId);
};

export const getBookAsPdf = (bookId, token = null) => {
    return doFetch(getHost() + 'books/pdf/' + bookId, null, token, 'application/pdf', 'blob');
};

export const SET_BOOKS = 'SET_BOOKS';
export const SET_BOOK = 'SET_BOOK';
export const SET_SERIES = 'SET_SERIES';
export const SET_GENRES = 'SET_GENRES';

export const OPEN_BOOKPROPS_FORM = 'OPEN_BOOKPROPS_FORM';
export const CLOSE_BOOKPROPS_FORM = 'CLOSE_BOOKPROPS_FORM';

export const OPEN_EDITSERIES_FORM = 'OPEN_EDITSERIES_FORM';
export const CLOSE_EDITSERIES_FORM = 'CLOSE_EDITSERIES_FORM';

export const OPEN_REVIEW_FORM = 'OPEN_REVIEW_FORM';
export const CLOSE_REVIEW_FORM = 'CLOSE_REVIEW_FORM';

export const OPEN_PAY_BOOK_FORM = 'OPEN_PAY_BOOK_FORM';
export const CLOSE_PAY_BOOK_FORM = 'CLOSE_PAY_BOOK_FORM';

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

export const openBookPropsForm = (book) => {
    return {
        type: OPEN_BOOKPROPS_FORM,
        book
    }
};

export const closeBookPropsForm = () => {
    return {
        type: CLOSE_BOOKPROPS_FORM
    }
};

export const openEditSeriesForm = () => {
    return {
        type: OPEN_EDITSERIES_FORM
    }
};

export const closeEditSeriesForm = () => {
    return {
        type: CLOSE_EDITSERIES_FORM
    }
};

export const openReviewForm = (book) => {
    return {
        type: OPEN_REVIEW_FORM,
        book
    }
};

export const closeReviewForm = () => {
    return {
        type: CLOSE_REVIEW_FORM
    }
};

export const openPayBookForm = () => {
    return {
        type: OPEN_PAY_BOOK_FORM
    }
};

export const closePayBookForm = () => {
    return {
        type: CLOSE_PAY_BOOK_FORM
    }
};