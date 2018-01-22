import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const getSessionsCount = () => {
    return doFetch(getHost() + 'count/sessions');
};

export const confirmPasswordChanging = (email) => {
    return doFetch(getHost() + 'reminder/confirm?email=' + email);
};

export const setDefaultPassword = (token, email) => {
    return doFetch(getHost() + 'reminder/password?token=' + token + '&email=' + email);
};

export const OPEN_LOGIN_FORM = 'OPEN_LOGIN_FORM';
export const CLOSE_LOGIN_FORM = 'CLOSE_LOGIN_FORM';

export const OPEN_FORGOT_PASSWORD_FORM = 'OPEN_FORGOT_PASSWORD_FORM';
export const CLOSE_FORGOT_PASSWORD_FORM = 'CLOSE_FORGOT_PASSWORD_FORM';

export const OPEN_USER_POLICY = 'OPEN_USER_POLICY';
export const CLOSE_USER_POLICY = 'CLOSE_USER_POLICY';

export const OPEN_WRITE_MESSAGE_FORM = 'OPEN_WRITE_MESSAGE_FORM';
export const CLOSE_WRITE_MESSAGE_FORM = 'CLOSE_WRITE_MESSAGE_FORM';

export const CREATE_NOTIFY = 'CREATE_NOTIFY';
export const REMOVE_NOTIFIES = 'REMOVE_NOTIFIES';
export const REMOVE_NOTIFY = 'REMOVE_NOTIFY';

export const GO_TO_COMMENTS = 'GO_TO_COMMENTS';

export const UPDATE_MUTABLE_DATE = 'UPDATE_MUTABLE_DATE';

export const SET_USER_DETAILS = 'SET_USER_DETAILS';

export const openLoginForm = (loginFormRegister) => {
    return {
        type: OPEN_LOGIN_FORM,
        loginFormRegister
    }
};

export const closeLoginForm = () => {
    return {
        type: CLOSE_LOGIN_FORM
    }
};

export const openForgotPasswordForm = () => {
    return {
        type: OPEN_FORGOT_PASSWORD_FORM
    }
};

export const closeForgotPasswordForm = () => {
    return {
        type: CLOSE_FORGOT_PASSWORD_FORM
    }
};

export const openUserPolicy = () => {
    return {
        type: OPEN_USER_POLICY
    }
};

export const closeUserPolicy = () => {
    return {
        type: CLOSE_USER_POLICY
    }
};

export const openWriteMessageForm = () => {
    return {
        type: OPEN_WRITE_MESSAGE_FORM
    }
};

export const closeWriteMessageForm = () => {
    return {
        type: CLOSE_WRITE_MESSAGE_FORM
    }
};

export const createNotify = (type, header, message) => {
    return {
        type: CREATE_NOTIFY,
        nType: type,
        header,
        message
    }
};

export const removeNotifies = () => {
    return {
        type: REMOVE_NOTIFIES
    }
};

export const removeNotify = (alert) => {
    return {
        type: REMOVE_NOTIFY,
        alert
    }
};

export const goToComments = (goToComments) => {
    return {
        type: GO_TO_COMMENTS,
        goToComments
    }
};

export const updateMutableDate = (mutableDate) => {
    return {
        type: UPDATE_MUTABLE_DATE,
        mutableDate
    }
};

export const setUserDetails = (details) => {
    return {
        type: SET_USER_DETAILS,
        details
    }
};