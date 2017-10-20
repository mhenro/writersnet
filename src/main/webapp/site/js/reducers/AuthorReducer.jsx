import {
    SET_AUTHORS,
    SET_AUTHOR
} from '../actions/AuthorActions.jsx';

const initialState = {
    authors: [],
    author: null
};

const AuthorReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_AUTHORS:
            return Object.assign({}, state, {authors: action.authors});

        case SET_AUTHOR:
            return Object.assign({}, state, {author: action.author});
    }
    return state;
};

export default AuthorReducer;