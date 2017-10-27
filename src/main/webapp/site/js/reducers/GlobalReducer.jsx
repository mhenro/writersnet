import {
    OPEN_LOGIN_FORM,
    CLOSE_LOGIN_FORM,
    OPEN_BOOKPROPS_FORM,
    CLOSE_BOOKPROPS_FORM,
    CREATE_NOTIFY,
    REMOVE_NOTIFIES,
    REMOVE_NOTIFY
} from '../actions/GlobalActions.jsx';

import {
    SET_EMAIL,
    SET_LOGIN,
    SET_PASSWORD,
    SET_PASSWORD_CONFIRM,
    SET_TOKEN,
} from '../actions/AuthActions.jsx';

const initialState = {
    registered: false,
    token: '',
    user: {
        email: '',
        login: 'Anonymous',
        password: '',
        passwordConfirm: ''
    },
    showLoginForm: false,
    loginFormRegister: true,
    showBookPropsForm: false,
    editableBook: null,
    alerts: []
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
                return Object.assign({}, state, {token: '', registered: false});   //for logout
            }

        case CLOSE_LOGIN_FORM:
            return Object.assign({}, state, {showLoginForm: false});

        case OPEN_LOGIN_FORM:
            return Object.assign({}, state, {showLoginForm: true, loginFormRegister: action.loginFormRegister});

        case OPEN_BOOKPROPS_FORM:
            return Object.assign({}, state, {showBookPropsForm: true, editableBook: action.book});

        case CLOSE_BOOKPROPS_FORM:
            return Object.assign({}, state, {showBookPropsForm: false, editableBook: null});

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
    }
    return state;
};

export default GlobalReducer;