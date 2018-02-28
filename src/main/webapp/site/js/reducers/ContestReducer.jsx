import {
    SHOW_CONTEST_EDIT_FORM,
    CLOSE_CONTEST_EDIT_FORM,
    SHOW_CONTEST_DONATE_FORM,
    CLOSE_CONTEST_DONATE_FORM,
    SET_CONTEST_ID_FOR_DONATE
} from '../actions/ContestActions.jsx';

const initialState = {
    showContestEditForm: false,
    showContestDonateForm: false,
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

        case SET_CONTEST_ID_FOR_DONATE:
            return Object.assign({}, state, {contestIdForDonate: action.contestId});
    }
    return state;
};

export default ContestReducer;