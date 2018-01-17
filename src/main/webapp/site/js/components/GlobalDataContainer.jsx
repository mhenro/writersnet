import React from 'react';
import { getQueryParams } from '../utils.jsx';
import { connect } from 'react-redux';
import {
    sendActivationToken,
    setToken,
    setLogin
} from '../actions/AuthActions.jsx';
import { createNotify, updateMutableDate, setDefaultPassword } from '../actions/GlobalActions.jsx';
import {
    getUnreadMessagesFromUser,
    setUnreadMessages
} from '../actions/MessageActions.jsx';
import {
    getNewFriendsCount,
    setNewFriends
} from '../actions/AuthorActions.jsx';

class GlobalDataContainer extends React.Component {
    componentDidMount() {
        /* activating new user if needed */
        let query = getQueryParams(document.location.search);
        let activationToken = query.activationToken;
        if (activationToken) {
            this.props.onSendActivationToken(activationToken);
        }

        /* set default password for user if needed */
        let tokenForPassword = query.token,
            email = query.email;
        if (tokenForPassword && email) {
            this.props.onSetDefaultPassword(tokenForPassword, email);
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

        this.globalTimer = setInterval(() => {
            if ((this.props.login !== 'Anonymous') && (this.props.token !== '')) {
                this.props.onGetUnreadMessages(this.props.login, this.props.token, unreadCount => this.onSetUnreadMessages(unreadCount));
                this.props.onGetNewFriendsCount(this.props.login, this.props.token);
            }
        }, 5000);

        this.cacheImgTimer = setInterval(() => {
            this.updateMutableDate();
        }, 20000);
    }

    componentWillUnmount() {
        clearInterval(this.globalTimer);
        clearInterval(this.cacheImgTimer);
    }

    updateMutableDate() {
        this.props.onUpdateMutableDate();
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
        login: state.GlobalReducer.user.login,
        mutableDate: state.GlobalReducer.mutableDate
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onSendActivationToken: (activationToken) => {
            return sendActivationToken(activationToken).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('info', 'Info', 'User activation was completed! Please log-in.'));
                } else if (response.status === 403) {
                    dispatch(createNotify('danger', 'Error', 'Activation user error'));
                } else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSetDefaultPassword: (token, email) => {
            return setDefaultPassword(token, email).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('info', 'Info', 'Temporary password was generated and was sent to your email. Please, check it.'));
                } else {
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
                    dispatch(setToken(json.token));
                    callback(json.message);
                }
                else if (json.message.includes('JWT expired at')) {
                    dispatch(setToken(''));
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
        },

        onGetNewFriendsCount: (userId, token) => {
            return getNewFriendsCount(userId, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setNewFriends(json.message));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onUpdateMutableDate: () => {
            dispatch(updateMutableDate(new Date()));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(GlobalDataContainer);