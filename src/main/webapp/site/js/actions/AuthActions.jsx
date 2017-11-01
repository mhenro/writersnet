import doFetch from './fetch';
import {getHost} from '../utils.jsx';

export const sendLogin = (username, password) => {
    return doFetch(getHost() + 'auth', {
        username: username,
        password: password
    });
};

export const sendRegister = (email, username, password) => {
    return doFetch(getHost() + 'register', {
        email: email,
        username: username,
        password: password
    });
};

export const sendActivationToken = (activationToken) => {
    return doFetch(getHost() + 'activate?activationToken=' + activationToken);
};

export const SET_EMAIL = 'SET_EMAIL';
export const SET_LOGIN = 'SET_LOGIN';
export const SET_PASSWORD = 'SET_PASSWORD';
export const SET_PASSWORD_CONFIRM = 'SET_PASSWORD_CONFIRM';
export const SET_TOKEN = 'SET_TOKEN';

export const setEmail = (email) => {
    return {
        type: SET_EMAIL,
        email
    }
};

export const setLogin = (login) => {
    return {
        type: SET_LOGIN,
        login
    }
};

export const setPassword = (password) => {
    return {
        type: SET_PASSWORD,
        password
    }
};

export const setPasswordConfirm = (passwordConfirm) => {
    return {
        type: SET_PASSWORD_CONFIRM,
        passwordConfirm
    }
};

export const setToken = (token) => {
    return {
        type: SET_TOKEN,
        token
    }
};