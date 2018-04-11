import {
    SHOW_CONTEST_EDIT_FORM,
    CLOSE_CONTEST_EDIT_FORM,
    SHOW_CONTEST_DONATE_FORM,
    CLOSE_CONTEST_DONATE_FORM,
    SHOW_SEARCH_AUTHOR_FORM,
    CLOSE_SEARCH_AUTHOR_FORM,
    SET_CONTEST_ID_FOR_DONATE,
    SHOW_CONTEST_ESTIMATION_FORM,
    CLOSE_CONTEST_ESTIMATION_FORM
} from '../actions/ContestActions.jsx';

const initialState = {
    showContestEditForm: false,
    showContestDonateForm: false,
    showSearchAuthorForm: false,
    showContestEstimationForm: false,
    contestIdForDonate: null
};

const ContestReducer = (state = initialState, action) => {
    switch (action.type) {
        case SHOW_CONTEST_EDIT_FORM:
            return Object.assign({}, state, {showContestEditForm: true});

        case CLOSE_CONTEST_EDIT_FORM:
            return Object.assign({}, state, {showContestEditForm: false});

        case SHOW_CONTEST_DONATE_FORM:
            return Object.assign({}, state, {showContestDonateForm: true});

        case CLOSE_CONTEST_DONATE_FORM:
            return Object.assign({}, state, {showContestDonateForm: false});

        case SHOW_SEARCH_AUTHOR_FORM:
            return Object.assign({}, state, {showSearchAuthorForm: true});

        case CLOSE_SEARCH_AUTHOR_FORM:
            return Object.assign({}, state, {showSearchAuthorForm: false});

        case SET_CONTEST_ID_FOR_DONATE:
            return Object.assign({}, state, {contestIdForDonate: action.contestId});

        case SHOW_CONTEST_ESTIMATION_FORM:
            return Object.assign({}, state, {showContestEstimationForm: true});

        case CLOSE_CONTEST_ESTIMATION_FORM:
            return Object.assign({}, state, {showContestEstimationForm: false});
    }
    return state;
};

export default ContestReducer;