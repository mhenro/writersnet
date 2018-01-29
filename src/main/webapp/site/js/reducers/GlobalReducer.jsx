import {
    OPEN_LOGIN_FORM,
    CLOSE_LOGIN_FORM,
    OPEN_FORGOT_PASSWORD_FORM,
    CLOSE_FORGOT_PASSWORD_FORM,
    OPEN_USER_POLICY,
    CLOSE_USER_POLICY,
    OPEN_WRITE_MESSAGE_FORM,
    CLOSE_WRITE_MESSAGE_FORM,
    CREATE_NOTIFY,
    REMOVE_NOTIFIES,
    REMOVE_NOTIFY,
    GO_TO_COMMENTS,
    UPDATE_MUTABLE_DATE,
    SET_USER_DETAILS,
    SET_PURCHASE_ID,
    SET_CONTENT_HEIGHT
} from '../actions/GlobalActions.jsx';

import {
    SET_EMAIL,
    SET_LOGIN,
    SET_PASSWORD,
    SET_PASSWORD_CONFIRM,
    SET_TOKEN,
} from '../actions/AuthActions.jsx';

import {
    SET_UNREAD_MESSAGES
} from '../actions/MessageActions.jsx';

import {
    SET_NEW_FRIENDS
} from '../actions/AuthorActions.jsx';

import {
    SET_USER_BALANCE,
    SHOW_CONFIRM_PAYMENT_FORM,
    CLOSE_CONFIRM_PAYMENT_FORM
} from '../actions/BalanceActions.jsx';

const initialState = {
    registered: false,
    token: '',
    user: {
        email: '',
        login: 'Anonymous',
        password: '',
        passwordConfirm: '',
        balance: 0,
        details: {
            premium: false
        }
    },
    language: 'EN',
    showLoginForm: false,
    showForgotPasswordForm: false,
    showUserPolicy: false,
    showWriteMessageForm: false,
    showConfirmPaymentForm: false,
    loginFormRegister: true,
    alerts: [],
    goToComments: false,
    unreadMessages: 0,
    newFriends: 0,
    mutableDate: null,
    purchaseId: null,
    contentHeight: 0
};

const GlobalReducer = (state = initialState, action) => {
    switch(action.type) {
        case SET_EMAIL:
            return Object.assign({}, state, {user: {
                email: action.email,
                login: state.user.login,
                password: state.user.password,
                passwordConfirm: state.user.passwordConfirm
            }});

        case SET_LOGIN:
            return Object.assign({}, state, {user: {
                email: state.user.email,
                login: action.login,
                password: state.user.password,
                passwordConfirm: state.user.passwordConfirm
            }});

        case SET_PASSWORD:
            return Object.assign({}, state, {user: {
                email: state.user.email,
                login: state.user.login,
                password: action.password,
                passwordConfirm: state.user.passwordConfirm
            }});

        case SET_PASSWORD_CONFIRM:
            return Object.assign({}, state, {user: {
                email: state.user.email,
                login: state.user.login,
                password: state.user.password,
                passwordConfirm: action.passwordConfirm
            }});

        case SET_TOKEN:
            sessionStorage.setItem('token', action.token);
            if (action.token !== '') {
                return Object.assign({}, state, {token: action.token, registered: true});
            } else {
                return Object.assign({}, state, {token: '', registered: false, user: {
                    email: '',
                    login: 'Anonymous',
                    password: '',
                    passwordConfirm: '',
                    details: { premium: false },
                    balance: 0
                }});   //for logout
            }

        case CLOSE_LOGIN_FORM:
            return Object.assign({}, state, {showLoginForm: false});

        case OPEN_LOGIN_FORM:
            return Object.assign({}, state, {showLoginForm: true, loginFormRegister: action.loginFormRegister});

        case OPEN_FORGOT_PASSWORD_FORM:
            return Object.assign({}, state, {showForgotPasswordForm: true});

        case CLOSE_FORGOT_PASSWORD_FORM:
            return Object.assign({}, state, {showForgotPasswordForm: false});

        case OPEN_USER_POLICY:
            return Object.assign({}, state, {showUserPolicy: true});

        case CLOSE_USER_POLICY:
            return Object.assign({}, state, {showUserPolicy: false});

        case OPEN_WRITE_MESSAGE_FORM:
            return Object.assign({}, state, {showWriteMessageForm: true});

        case CLOSE_WRITE_MESSAGE_FORM:
            return Object.assign({}, state, {showWriteMessageForm: false});

        case CREATE_NOTIFY:
            let newAlert = {
                id: (new Date()).getTime(),
                type: action.nType,
                headline: action.header,
                message: action.message
            };
            return Object.assign({}, state, {alerts: [...state.alerts, newAlert]});

        case REMOVE_NOTIFIES:
            return Object.assign({}, state, {alerts: []});

        case REMOVE_NOTIFY:
            const alerts = state.alerts;
            const index = alerts.indexOf(action.alert);
            if (index >= 0) {
                return Object.assign({}, state, {alerts: [...alerts.slice(0, index), ...alerts.slice(index + 1)]});
            }

        case GO_TO_COMMENTS:
            return Object.assign({}, state, {goToComments: action.goToComments});

        case SET_UNREAD_MESSAGES:
            return Object.assign({}, state, {unreadMessages: action.unreadCount});

        case SET_NEW_FRIENDS:
            return Object.assign({}, state, {newFriends: action.newFriends});

        case UPDATE_MUTABLE_DATE:
            return Object.assign({}, state, {mutableDate: action.mutableDate});

        case SET_USER_DETAILS:
            return Object.assign({}, state, {user: {
                email: state.user.email,
                login: state.user.login,
                password: state.user.password,
                passwordConfirm: state.user.passwordConfirm,
                balance: state.user.balance,
                details: action.details
            },
                language: action.details.language});

        case SET_USER_BALANCE:
            return Object.assign({}, state, {user: {
                email: state.user.email,
                login: state.user.login,
                password: state.user.password,
                passwordConfirm: state.user.passwordConfirm,
                details: state.user.details,
                balance: action.balance
            }});

        case SHOW_CONFIRM_PAYMENT_FORM:
            return Object.assign({}, state, {showConfirmPaymentForm: true});

        case CLOSE_CONFIRM_PAYMENT_FORM:
            return Object.assign({}, state, {showConfirmPaymentForm: false});

        case SET_PURCHASE_ID:
            return Object.assign({}, state, {purchaseId: action.id});

        case SET_CONTENT_HEIGHT:
            return Object.assign({}, state, {contentHeight: action.height});
    }
    return state;
};

export default GlobalReducer;