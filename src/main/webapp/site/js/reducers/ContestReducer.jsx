import {
    SHOW_CONTEST_EDIT_FORM,
    CLOSE_CONTEST_EDIT_FORM
} from '../actions/ContestActions.jsx';

const initialState = {
    showContestEditForm: false
};

const ContestReducer = (state = initialState, action) => {
    switch (action.type) {
        case SHOW_CONTEST_EDIT_FORM:
            return Object.assign({}, state, {showContestEditForm: true});

        case CLOSE_CONTEST_EDIT_FORM:
            return Object.assign({}, state, {showContestEditForm: false});
    }
    return state;
};

export default ContestReducer;