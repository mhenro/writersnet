import doFetch from './fetch.jsx';
import { getHost } from '../utils.jsx';

export const getUserBalance = (token) => {
    return doFetch(getHost() + 'balance', null, token);
};

export const getUserPaymentHistory = (token, page = 0, size = 20) => {
    return doFetch(getHost() + 'balance/history?page=' + page + '&size=' + size, null, token);
};

export const buy = (buyRequest, token) => {
    return doFetch(getHost() + 'buy', buyRequest, token);
};

export const SET_USER_BALANCE = 'SET_USER_BALANCE';
export const SHOW_CONFIRM_PAYMENT_FORM = 'SHOW_CONFIRM_PAYMENT_FORM';
export const CLOSE_CONFIRM_PAYMENT_FORM = 'CLOSE_CONFIRM_PAYMENT_FORM';

export const setUserBalance = (balance) => {
    return {
        type: SET_USER_BALANCE,
        balance
    }
};

export const showConfirmPaymentForm = () => {
    return {
        type: SHOW_CONFIRM_PAYMENT_FORM
    }
};

export const closeConfirmPaymentForm = () => {
    return {
        type: CLOSE_CONFIRM_PAYMENT_FORM
    }
};