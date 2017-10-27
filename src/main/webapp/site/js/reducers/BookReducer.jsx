import {
    SET_BOOKS,
    SET_BOOK
} from '../actions/BookActions.jsx';

const initialState = {
    books: [],
    book: null
};

const BookReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_BOOKS:
            return Object.assign({}, state, {books: action.books});

        case SET_BOOK:
            return Object.assign({}, state, {book: action.book});
    }
    return state;
};

export default BookReducer;