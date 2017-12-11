import React from 'react';
import { getQueryParams } from '../utils.jsx';
import { connect } from 'react-redux';
import {
    sendActivationToken,
    setToken,
    setLogin
} from '../actions/AuthActions.jsx';
import { createNotify } from '../actions/GlobalActions.jsx';
import {
    getUnreadMessagesFromUser,
    setUnreadMessages
} from '../actions/MessageActions.jsx';

class GlobalDataContainer extends React.Component {
    constructor(props) {
        super(props);
        ['onSetUnreadMessages'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        /* activating new user if needed */
        let query = getQueryParams(document.location.search);
        let activationToken = query.activationToken;
        if (activationToken) {
            this.props.onSendActivationToken(activationToken);
        }

        /* loading session after page refresh */
        let token = sessionStorage.getItem('token'),
            username = sessionStorage.getItem('username');

        if (this.props.token === '' && token) {
            this.props.onSetToken(token);
            if (username) {
                this.props.onSetLogin(username);
            }
        }

        this.getUnreadMessagesTimer = setInterval(() => {
            if ((this.props.login !== 'Anonymous') && (this.props.token !== '')) {
                this.props.onGetUnreadMessages(this.props.login, this.props.token, this.onSetUnreadMessages);
            }
        }, 5000);
    }

    onSetUnreadMessages(unreadCount) {
        this.props.onSetUnreadMessages(unreadCount);
    }

    render() {
        return (
            <div></div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        registered: state.GlobalReducer.registered,
        token: state.GlobalReducer.token,
        login: state.GlobalReducer.user.login
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onSendActivationToken: (activationToken) => {
            return sendActivationToken(activationToken).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('info', 'Info', 'User activation was completed! Please log-in.'));
                }
                else if (response.status === 403) {
                    dispatch(createNotify('danger', 'Error', 'Activation user error'));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSetToken: (token) => {
            dispatch(setToken(token));
        },

        onSetLogin: (login) => {
            dispatch(setLogin(login));
        },

        onGetUnreadMessages: (userId, token, callback) => {
            return getUnreadMessagesFromUser(userId, token).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.message);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSetUnreadMessages: (unreadCount) => {
            dispatch(setUnreadMessages(unreadCount));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(GlobalDataContainer);