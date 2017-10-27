export const OPEN_LOGIN_FORM = 'OPEN_LOGIN_FORM';
export const CLOSE_LOGIN_FORM = 'CLOSE_LOGIN_FORM';

export const OPEN_BOOKPROPS_FORM = 'OPEN_BOOKPROPS_FORM';
export const CLOSE_BOOKPROPS_FORM = 'CLOSE_BOOKPROPS_FORM';

export const CREATE_NOTIFY = 'CREATE_NOTIFY';
export const REMOVE_NOTIFIES = 'REMOVE_NOTIFIES';
export const REMOVE_NOTIFY = 'REMOVE_NOTIFY';

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

export const openBookPropsForm = (book) => {
    return {
        type: OPEN_BOOKPROPS_FORM,
        book
    }
};

export const closeBookPropsForm = () => {
    return {
        type: CLOSE_BOOKPROPS_FORM
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