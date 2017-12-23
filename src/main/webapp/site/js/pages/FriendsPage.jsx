import React from 'react';
import { connect } from 'react-redux';

import FriendList from '../components/friends/FriendList.jsx';

import {
    getAuthorDetails,
    setAuthor,
    subscribeOn,
    removeSubscription
} from '../actions/AuthorActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';
import {
    getGroupIdByRecipient
} from '../actions/MessageActions.jsx';
import { setToken } from '../actions/AuthActions.jsx';

class FriendsPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeTab: 'friends',    //requests
            searchPattern: ''
        };
        ['onAddToFriends', 'onRemoveFriend', 'onSearchChange'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        let timer = setInterval(() => {
            if (this.props.login) {
                this.props.onGetAuthorDetails(this.props.login);
                clearInterval(timer);
            }
        }, 1000);
    }

    getActiveClass(tabName) {
        if (tabName === this.state.activeTab) {
            return 'active';
        }
        return null;
    }

    changeTab(tabName) {
        this.setState({
            activeTab: tabName
        });
    }

    onSearchChange(event) {
        this.setState({
            searchPattern: event.target.value
        });
    }

    getFriendIds() {
        return this.props.author.subscribers
            .filter(subscriber => this.props.author.subscriptions.some(subscription => subscription.subscriberId === subscriber.subscriptionId))
            .map(friend => friend.subscriptionId);
    }

    getFriends() {
        return this.props.author.subscribers
            .filter(subscriber => this.props.author.subscriptions.some(subscription => subscription.subscriberId === subscriber.subscriptionId))
            .filter(friend => (this.state.searchPattern !== '' ? friend.subscriptionFullName.toLowerCase().includes(this.state.searchPattern.toLowerCase()) : true))
            .map(friend => {
                return {
                    date: friend.date,
                    active: friend.active,
                    id: friend.subscriptionId,
                    name: friend.subscriptionFullName,
                    section: friend.subscriptionSectionName,
                    avatar: friend.subscriptionAvatar
                };
            });
    }

    getSubscribers() {
        let friends = this.getFriendIds();
        return this.props.author.subscriptions
            .filter(subscription => subscription.subscriptionId === this.props.author.username)
            .filter(subscription => !friends.some(friend => friend === subscription.subscriberId))
            .filter(subscriber => (this.state.searchPattern !== '' ? subscriber.subscriberFullName.toLowerCase().includes(this.state.searchPattern.toLowerCase()) : true))
            .map(subscriber => {
                return {
                    date: subscriber.date,
                    active: subscriber.active,
                    id: subscriber.subscriberId,
                    name: subscriber.subscriberFullName,
                    section: subscriber.subscriberSectionName,
                    avatar: subscriber.subscriberAvatar
                };
            });
    }

    getSubscriptions() {
        let friends = this.getFriendIds();
        return this.props.author.subscribers
            .filter(subscriber => subscriber.subscriberId === this.props.author.username)
            .filter(subscriber => !friends.some(friend => friend === subscriber.subscriptionId))
            .filter(subscription => (this.state.searchPattern !== '' ? subscription.subscriptionFullName.toLowerCase().includes(this.state.searchPattern.toLowerCase()) : true))
            .map(subscription => {
                return {
                    date: subscription.date,
                    active: subscription.active,
                    id: subscription.subscriptionId,
                    name: subscription.subscriptionFullName,
                    section: subscription.subscriptionSectionName,
                    avatar: subscription.subscriptionAvatar
                };
            });
    }

    getSendMsgButtonVisibility() {
        if (this.state.activeTab === 'friends') {
            return true;
        } else if (this.state.activeTab === 'subscribers') {
            return false;
        } else if (this.state.activeTab === 'subscriptions') {
            return false;
        }
    }

    getAddFriendButtonVisibility() {
        if (this.state.activeTab === 'friends') {
            return false;
        } else if (this.state.activeTab === 'subscribers') {
            return true;
        } else if (this.state.activeTab === 'subscriptions') {
            return false;
        }
    }

    getReadNewsButtonVisibility() {
        if (this.state.activeTab === 'friends') {
            return true;
        } else if (this.state.activeTab === 'subscribers') {
            return false;
        } else if (this.state.activeTab === 'subscriptions') {
            return true;
        }
    }

    getRemoveFriendButtonVisibility() {
        if (this.state.activeTab === 'friends') {
            return true;
        } else if (this.state.activeTab === 'subscribers') {
            return false;
        } else if (this.state.activeTab === 'subscriptions') {
            return true;
        }
    }

    isDataLoaded() {
        if (this.props.author && this.props.login) {
            return true;
        }
        return false;
    }

    getItems() {
        if (this.state.activeTab === 'friends') {
            return this.getFriends();
        } else if (this.state.activeTab === 'subscribers') {
            return this.getSubscribers();
        } else if (this.state.activeTab === 'subscriptions') {
            return this.getSubscriptions();
        }
    }

    getTabCaption(tabName) {
        if (tabName === 'friends') {
            let count = this.getFriends().length;
            return <span>My friends <span className="counter">{count}</span></span>;
        } else if (tabName === 'subscribers') {
            let count = this.getSubscribers().length;
            return <span>Subscribers <span className="counter">{count}</span></span>;
        } else if (tabName === 'subscriptions') {
            let count = this.getSubscriptions().length;
            return <span>Subscriptions <span className="counter">{count}</span></span>;
        }
    }

    onAddToFriends(friend) {
        this.props.onSubcribeOn(friend, this.props.token, () => this.props.onGetAuthorDetails(this.props.login));
    }

    onRemoveFriend(friend) {
        this.props.onRemoveSubscription(friend, this.props.token, () => this.props.onGetAuthorDetails(this.props.login));
    }

    render() {
        if (!this.isDataLoaded()) {
            return null;
        }

        return (
            <div>
                <ul className="nav nav-tabs">
                    <li className={this.getActiveClass('friends')}><a onClick={() => this.changeTab('friends')}>{this.getTabCaption('friends')}</a></li>
                    <li className={this.getActiveClass('subscribers')}><a onClick={() => this.changeTab('subscribers')}>{this.getTabCaption('subscribers')}</a></li>
                    <li className={this.getActiveClass('subscriptions')}><a onClick={() => this.changeTab('subscriptions')}>{this.getTabCaption('subscriptions')}</a></li>
                </ul>
                <br/>
                <div className="input-group">
                    <input value={this.state.searchPattern} onChange={this.onSearchChange} type="text" className="form-control" placeholder="Input friend name" />
                        <div className="input-group-btn">
                            <button className="btn btn-default" type="submit">
                                <i className="glyphicon glyphicon-search"></i>
                            </button>
                        </div>
                </div>
                <FriendList friends={this.getItems()}
                            sendMsgButton={this.getSendMsgButtonVisibility()}
                            addFriendButton={this.getAddFriendButtonVisibility()}
                            readNewsButton={this.getReadNewsButtonVisibility()}
                            removeFriendButton={this.getRemoveFriendButtonVisibility()}
                            onAddToFriends={this.onAddToFriends}
                            onRemoveSubscription={this.onRemoveFriend}
                            login={this.props.login}
                            token={this.props.token}
                            onGetGroupId={this.props.onGetGroupIdByRecipient}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token,
        author: state.AuthorReducer.author
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthorDetails: (userId) => {
            return getAuthorDetails(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setAuthor(json));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSubcribeOn: (authorName, token, callback) => {
            return subscribeOn(authorName, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', json.message));
                    callback();
                    dispatch(setToken(json.token));
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

        onRemoveSubscription: (authorName, token, callback) => {
            return removeSubscription(authorName, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', json.message));
                    callback();
                    dispatch(setToken(json.token));
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

        onGetGroupIdByRecipient: (recipientId, userId, token, callback) => {
            return getGroupIdByRecipient(recipientId, userId, token).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.message);
                    dispatch(setToken(json.token));
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
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(FriendsPage);