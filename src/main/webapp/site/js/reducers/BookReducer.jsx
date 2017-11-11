import {
    SET_BOOKS,
    SET_BOOK,
    SET_SERIES,
    SET_GENRES,
    OPEN_BOOKPROPS_FORM,
    CLOSE_BOOKPROPS_FORM,
    OPEN_EDITSERIES_FORM,
    CLOSE_EDITSERIES_FORM
} from '../actions/BookActions.jsx';

const initialState = {
    books: [],
    series: [],
    genres: [],
    book: null,
    showBookPropsForm: false,
    showEditSeriesForm: false
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

        case OPEN_BOOKPROPS_FORM:
            return Object.assign({}, state, {showBookPropsForm: true, editableBook: action.book});

        case CLOSE_BOOKPROPS_FORM:
            return Object.assign({}, state, {showBookPropsForm: false, editableBook: null});

        case OPEN_EDITSERIES_FORM:
            return Object.assign({}, state, {showEditSeriesForm: true});

        case CLOSE_EDITSERIES_FORM:
            return Object.assign({}, state, {showEditSeriesForm: false});
    }
    return state;
};

export default BookReducer;