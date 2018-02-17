import {
    SET_AUTHORS,
    SET_AUTHOR,
    SHOW_AUTHOR_GIFTS_FORM,
    CLOSE_AUTHOR_GIFTS_FORM
} from '../actions/AuthorActions.jsx';

const initialState = {
    authors: [],
    author: null,
    showAuthorGiftsForm: false
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
    }
    return state;
};

export default AuthorReducer;