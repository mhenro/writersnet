import {
    SET_BOOKS,
    SET_BOOK,
    SET_SERIES,
    SET_GENRES
} from '../actions/BookActions.jsx';

const initialState = {
    books: [],
    series: [],
    genres: [],
    book: null
};

const BookReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_BOOKS:
            return Object.assign({}, state, {books: action.books});

        case SET_BOOK:
            return Object.assign({}, state, {book: action.book});

        case SET_SERIES:
            return Object.assign({}, state, {series: action.series});

        case SET_GENRES:
            return Object.assign({}, state, {genres: action.genres});
    }
    return state;
};

export default BookReducer;