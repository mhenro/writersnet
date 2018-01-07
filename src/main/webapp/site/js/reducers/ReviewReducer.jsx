import { OPEN_REVIEW_READER_FORM, CLOSE_REVIEW_READER_FORM } from '../actions/ReviewActions.jsx';

const initialState = {
    showReviewReaderForm: false,
    review: null
};

const GlobalReducer = (state = initialState, action) => {
    switch(action.type) {
        case OPEN_REVIEW_READER_FORM:
            return Object.assign({}, state, {showReviewReaderForm: true, review: action.review});

        case CLOSE_REVIEW_READER_FORM:
            return Object.assign({}, state, {showReviewReaderForm: false, review: null});
    }
    return state;
};

export default GlobalReducer;