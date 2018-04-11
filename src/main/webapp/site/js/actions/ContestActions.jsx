import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const getAllContests = (page = 0, size = 20) => {
    return doFetch(getHost() + 'contests?page=' + page + '&size=' + size);
};

export const getParticipantContests = (userId, page = 0, size = 20) => {
    return doFetch(getHost() + 'contests/participants/' + userId + '?page=' + page + '&size=' + size);
};

export const getJudgeContests = (userId, page = 0, size = 20) => {
    return doFetch(getHost() + 'contests/judges/' + userId + '?page=' + page + '&size=' + size);
};

export const getCreatorContests = (userId, page = 0, size = 20) => {
    return doFetch(getHost() + 'contests/creators/' + userId + '?page=' + page + '&size=' + size);
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

export const getJudgesIdFromContest = (contestId) => {
    return doFetch(getHost() + 'contests/' + contestId + '/judges');
};

export const removeJudgeFromContest = (contestId, judgeId, token) => {
    return doFetch(getHost() + 'contests/' + contestId + '/judges/' + judgeId, 'DELETE', token);
};

export const getJudgesFromContest = (contestId, page = 0, size = 5) => {
    return doFetch(getHost() + 'contests/' + contestId + '/judges-full?page=' + page + '&size=' + size);
};

export const addParticipantsToContest = (addJudgeRequest, token) => {
    return doFetch(getHost() + 'contests/participants', addJudgeRequest, token);
};

export const getParticipantsIdFromContest = (contestId) => {
    return doFetch(getHost() + 'contests/' + contestId + '/participants');
};

export const removeParticipantFromContest = (contestId, participantId, token) => {
    return doFetch(getHost() + 'contests/' + contestId + '/participants/' + participantId, 'DELETE', token);
};

export const getParticipantsFromContest = (contestId, page = 0, size = 5) => {
    return doFetch(getHost() + 'contests/' + contestId + '/participants-full?page=' + page + '&size=' + size);
};

export const isContestReadyForStart = (contestId) => {
    return doFetch(getHost() + 'contests/' + contestId + '/readiness');
};

export const joinInContestAsJudge = (contestId, token) => {
    return doFetch(getHost() + 'contests/' + contestId + '/join', null, token);
};

export const joinInContestAsParticipant = (contestId, bookId, token) => {
    return doFetch(getHost() + 'contests/' + contestId + '/join/' + bookId, null, token);
};

export const refuseContestAsJudge = (contestId, token) => {
    return doFetch(getHost() + 'contests/' + contestId + '/refuse', null, token);
};

export const refuseContestAsParticipant = (contestId, bookId, token) => {
    return doFetch(getHost() + 'contests/' + contestId + '/refuse/' + bookId, null, token);
};

export const startContest = (contestId) => {
    return doFetch(getHost() + 'contests/' + contestId + '/start');
};

export const SHOW_CONTEST_EDIT_FORM = 'SHOW_CONTEST_EDIT_FORM';
export const CLOSE_CONTEST_EDIT_FORM = 'CLOSE_CONTEST_EDIT_FORM';

export const SHOW_CONTEST_DONATE_FORM = 'SHOW_CONTEST_DONATE_FORM';
export const CLOSE_CONTEST_DONATE_FORM = 'CLOSE_CONTEST_DONATE_FORM';

export const SHOW_SEARCH_AUTHOR_FORM = 'SHOW_SEARCH_AUTHOR_FORM';
export const CLOSE_SEARCH_AUTHOR_FORM = 'CLOSE_SEARCH_AUTHOR_FORM';

export const SET_CONTEST_ID_FOR_DONATE = 'SET_CONTEST_ID_FOR_DONATE';

export const SHOW_CONTEST_ESTIMATION_FORM = 'SHOW_CONTEST_ESTIMATION_FORM';
export const CLOSE_CONTEST_ESTIMATION_FORM = 'CLOSE_CONTEST_ESTIMATION_FORM';

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

export const showContestEstimationForm = () => {
    return {
        type: SHOW_CONTEST_ESTIMATION_FORM
    }
};

export const closeContestEstimationForm = () => {
    return {
        type: CLOSE_CONTEST_ESTIMATION_FORM
    }
};