import doFetch from './fetch';

export const getBooks = (page) => {
    return doFetch('http://localhost:8080/books?page=' + page + '&size=20');
};

export const getBookDetails = (bookId) => {
    return doFetch('http://localhost:8080/books/' + bookId);
};

export const saveBook = (book, token) => {
    return doFetch('http://localhost:8080/books', book, token);
};

export const saveCover = (cover, token) => {
    return doFetch('http://localhost:8080/cover', cover, token, 'multipart/form-data');
};

export const SET_BOOKS = 'SET_BOOKS';
export const SET_BOOK = 'SET_BOOK';

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