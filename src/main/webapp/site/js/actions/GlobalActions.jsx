export const OPEN_LOGIN_FORM = 'OPEN_LOGIN_FORM';
export const CLOSE_LOGIN_FORM = 'CLOSE_LOGIN_FORM';

export const OPEN_USER_POLICY = 'OPEN_USER_POLICY';
export const CLOSE_USER_POLICY = 'CLOSE_USER_POLICY';

export const OPEN_WRITE_MESSAGE_FORM = 'OPEN_WRITE_MESSAGE_FORM';
export const CLOSE_WRITE_MESSAGE_FORM = 'CLOSE_WRITE_MESSAGE_FORM';

export const CREATE_NOTIFY = 'CREATE_NOTIFY';
export const REMOVE_NOTIFIES = 'REMOVE_NOTIFIES';
export const REMOVE_NOTIFY = 'REMOVE_NOTIFY';

export const GO_TO_COMMENTS = 'GO_TO_COMMENTS';

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