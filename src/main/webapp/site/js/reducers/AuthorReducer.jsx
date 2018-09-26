import {
    SET_AUTHORS,
    SET_AUTHOR,
    SHOW_AUTHOR_GIFTS_FORM,
    CLOSE_AUTHOR_GIFTS_FORM,
    SHOW_COMPLAINT_FORM,
    CLOSE_COMPLAINT_FORM
} from '../actions/AuthorActions.jsx';

const initialState = {
    authors: [],
    author: null,
    showAuthorGiftsForm: false,
    showComplaintForm: false
};

const AuthorReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_AUTHORS:
            return Object.assign({}, state, {authors: action.authors});

        case SET_AUTHOR:
            return Object.assign({}, state, {author: action.author});

        case SHOW_AUTHOR_GIFTS_FORM:
            return Object.assign({}, state, {showAuthorGiftsForm: true});

        case CLOSE_AUTHOR_GIFTS_FORM:
            return Object.assign({}, state, {showAuthorGiftsForm: false});

        case SHOW_COMPLAINT_FORM:
            return Object.assign({}, state, {showComplaintForm: true});

        case CLOSE_COMPLAINT_FORM:
            return Object.assign({}, state, {showComplaintForm: false});
    }
    return state;
};

export default AuthorReducer;