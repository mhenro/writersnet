import doFetch from './fetch';

export const getBooks = (page) => {
    if (page) {
        return doFetch('http://localhost:8080/books?page=' + page + '&size=20');
    } else {
        return doFetch('http://localhost:8080/books');
    }
};

export const getSeries = (page) => {
    if (page) {
        return doFetch('http://localhost:8080/series?page=' + page + '&size=20');
    } else {
        return doFetch('http://localhost:8080/series');
    }
};

export const getGenres = () => {
    return doFetch('http://localhost:8080/genres');
};

export const getBookDetails = (bookId) => {
    return doFetch('http://localhost:8080/books/' + bookId);
};

export const saveBook = (book, token) => {
    return doFetch('http://localhost:8080/books', book, token);
};

export const saveBookText = (bookText, token) => {
    return doFetch('http://localhost:8080/text', bookText, token, 'multipart/form-data')
};

export const saveCover = (cover, token) => {
    return doFetch('http://localhost:8080/cover', cover, token, 'multipart/form-data');
};

export const deleteBook = (bookTextRequest, token) => {
    return doFetch('http://localhost:8080/books/' + bookTextRequest, 'DELETE', token);
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