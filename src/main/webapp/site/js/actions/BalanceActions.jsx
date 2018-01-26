import doFetch from './fetch';
import { getHost } from '../utils.jsx';

export const getUserBalance = (token) => {
    return doFetch(getHost() + 'balance', null, token);
};

export const getUserPaymentHistory = (token, page = 0, size = 20) => {
    return doFetch(getHost() + 'balance/history?page=' + page + '&size=' + size, null, token);
};

export const SET_USER_BALANCE = 'SET_USER_BALANCE';

export const setUserBalance = (balance) => {
    return {
        type: SET_USER_BALANCE,
        balance
    }
};