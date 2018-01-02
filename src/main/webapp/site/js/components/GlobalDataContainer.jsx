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
import {
    getNewFriendsCount,
    setNewFriends
} from '../actions/AuthorActions.jsx';

class GlobalDataContainer extends React.Component {
    constructor(props) {
        super(props);
        ['onSetUnreadMessages', 'getSubscribersCount'].map(fn => this[fn] = this[fn].bind(this));
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

        this.globalTimer = setInterval(() => {
            if ((this.props.login !== 'Anonymous') && (this.props.token !== '')) {
                this.props.onGetUnreadMessages(this.props.login, this.props.token, this.onSetUnreadMessages);
                this.props.onGetNewFriendsCount(this.props.login, this.props.token);
            }
        }, 5000);
    }

    getFriendIds(author) {
        return author.subscribers
            .filter(subscriber => author.subscriptions.some(subscription => subscription.subscriberId === subscriber.subscriptionId))
            .map(friend => friend.subscriptionId);
    }

    getSubscribersCount(author) {
        let friends = this.getFriendIds(author);
        return author.subscriptions
            .filter(subscription => subscription.subscriptionId === author.username)
            .filter(subscription => !friends.some(friend => friend === subscription.subscriberId)).length;
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
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(GlobalDataContainer);