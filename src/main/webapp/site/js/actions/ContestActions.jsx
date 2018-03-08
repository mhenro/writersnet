import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const getAllContests = (page = 0, size = 20) => {
    return doFetch(getHost() + 'contests?page=' + page + '&size=' + size);
};

export const getContest = (id) => {
    return doFetch(getHost() + 'contests/' + id);
};

export const saveContest = (contestRequest, token) => {
    return doFetch(getHost() + 'contests', contestRequest, token);
};

export const addJudgesToContest = (addJudgeRequest, token) => {
    return doFetch(getHost() + 'contests/judges', addJudgeRequest, token);
};

export const SHOW_CONTEST_EDIT_FORM = 'SHOW_CONTEST_EDIT_FORM';
export const CLOSE_CONTEST_EDIT_FORM = 'CLOSE_CONTEST_EDIT_FORM';
export const SHOW_CONTEST_DONATE_FORM = 'SHOW_CONTEST_DONATE_FORM';
export const CLOSE_CONTEST_DONATE_FORM = 'CLOSE_CONTEST_DONATE_FORM';
export const SHOW_SEARCH_AUTHOR_FORM = 'SHOW_SEARCH_AUTHOR_FORM';
export const CLOSE_SEARCH_AUTHOR_FORM = 'CLOSE_SEARCH_AUTHOR_FORM';
export const SET_CONTEST_ID_FOR_DONATE = 'SET_CONTEST_ID_FOR_DONATE';

export const showContestEditForm = () => {
    return {
        type: SHOW_CONTEST_EDIT_FORM
    }
};

export const closeContestEditForm = () => {
    return {
        type: CLOSE_CONTEST_EDIT_FORM
    }
};

export const showContestDonateForm = () => {
    return {
        type: SHOW_CONTEST_DONATE_FORM
    }
};
export const closeContestDonateForm = () => {
    return {
        type: CLOSE_CONTEST_DONATE_FORM
    }
};

export const showSearchAuthorForm = () => {
    return {
        type: SHOW_SEARCH_AUTHOR_FORM
    }
};

export const closeSearchAuthorForm = () => {
    return {
        type: CLOSE_SEARCH_AUTHOR_FORM
    }
};

export const setContestIdForDonate = (contestId) => {
    return {
        type: SET_CONTEST_ID_FOR_DONATE,
        contestId
    }
};