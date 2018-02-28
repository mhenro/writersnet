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

export const SHOW_CONTEST_EDIT_FORM = 'SHOW_CONTEST_EDIT_FORM';
export const CLOSE_CONTEST_EDIT_FORM = 'CLOSE_CONTEST_EDIT_FORM';

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