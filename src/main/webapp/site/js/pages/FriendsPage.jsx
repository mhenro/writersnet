import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';
import { getLocale } from '../locale.jsx';

import FriendList from '../components/friends/FriendList.jsx';

import {
    getAllFriends,
    getAllSubscribers,
    getAllSubscriptions,
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
            searchPattern: '',
            friends: [],
            subscribers: [],
            subscriptions: [],
            currentPage: 1,
            totalPages: 1,
            totalFriends: 0,
            totalSubscribers: 0,
            totalSubscriptions: 0
        };
    }

    componentDidMount() {
        let timer = setInterval(() => {
            if (this.props.login) {
                this.getFriendships('subscriptions');   //not a good solution. It is for counting friend groups
                this.getFriendships('subscribers');     //not a good solution. It is for counting friend groups
                this.getFriendships('friends');         //not a good solution. It is for counting friend groups
                clearInterval(timer);
            }
        }, 1000);
    }

    getFriendships(activeTab = this.state.activeTab, page = this.state.currentPage) {
        switch(activeTab) {
            case 'friends': this.props.onGetAllFriends(this.props.login, this.props.token, page - 1, friendships => this.updateFriendships(friendships, 'friends', 'totalFriends')); break;
            case 'subscribers': this.props.onGetAllSubscribers(this.props.login, this.props.token, page - 1, friendships => this.updateFriendships(friendships, 'subscribers', 'totalSubscribers')); break;
            case 'subscriptions': this.props.onGetAllSubscriptions(this.props.login, this.props.token, page - 1, friendships => this.updateFriendships(friendships, 'subscriptions', 'totalSubscriptions')); break;
        }
    }

    pageSelect(page) {
        this.setState({
            currentPage: page
        });
        this.getFriendships(this.state.activeTab, page);
    }

    updateFriendships(friendships, array, friendshipGroup) {
        let state = {
            currentPage: friendships.number + 1,
            totalPages: friendships.totalPages,
        };
        state[array] = friendships.content;
        state[friendshipGroup] = friendships.totalElements;
        this.setState(state);
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
        this.getFriendships(tabName);
    }

    onSearchChange(event) {
        this.setState({
            searchPattern: event.target.value
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
        if (this.props.login && this.props.token) {
            return true;
        }
        return false;
    }

    getTabCaption(tabName) {
        if (tabName === 'friends') {
            let count = this.state.totalFriends;
            return <span>{getLocale(this.props.language)['My friends']} <span className="counter">{count}</span></span>;
        } else if (tabName === 'subscribers') {
            let count = this.state.totalSubscribers;
            return <span>{getLocale(this.props.language)['Subscribers']} <span className="counter">{count}</span></span>;
        } else if (tabName === 'subscriptions') {
            let count = this.state.totalSubscriptions;
            return <span>{getLocale(this.props.language)['Subscriptions']} <span className="counter">{count}</span></span>;
        }
    }

    onAddToFriends(friend) {
        this.props.onSubcribeOn(friend, this.props.token, () => this.componentDidMount());
    }

    onRemoveFriend(friend) {
        this.props.onRemoveSubscription(friend, this.props.token, () => this.componentDidMount());
    }

    getItems() {
        switch (this.state.activeTab) {
            case 'friends': return this.state.friends; break;
            case 'subscribers': return this.state.subscribers; break;
            case 'subscriptions': return this.state.subscriptions; break;
        }
    }

    render() {
        if (!this.isDataLoaded()) {
            return null;
        }

        return (
            <div>
                <div className="col-sm-12">
                    <ul className="nav nav-tabs">
                        <li className={this.getActiveClass('friends')}><a onClick={() => this.changeTab('friends')}>{this.getTabCaption('friends')}</a></li>
                        <li className={this.getActiveClass('subscribers')}><a onClick={() => this.changeTab('subscribers')}>{this.getTabCaption('subscribers')}</a></li>
                        <li className={this.getActiveClass('subscriptions')}><a onClick={() => this.changeTab('subscriptions')}>{this.getTabCaption('subscriptions')}</a></li>
                    </ul>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <div className="input-group">
                        <input value={this.state.searchPattern} onChange={event => this.onSearchChange(event)} type="text" className="form-control" placeholder={getLocale(this.props.language)['Input friend name']} />
                            <div className="input-group-btn">
                                <button className="btn btn-default" type="submit">
                                    <i className="glyphicon glyphicon-search"></i>
                                </button>
                            </div>
                    </div>
                    <br/>
                </div>
                <div className="col-sm-12 text-center">
                    <Pagination
                        className={'shown'}
                        prev
                        next
                        first
                        last
                        ellipsis
                        boundaryLinks
                        items={this.state.totalPages}
                        maxButtons={3}
                        activePage={this.state.currentPage}
                        onSelect={page => this.pageSelect(page)}/>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <FriendList friends={this.getItems()}
                                sendMsgButton={this.getSendMsgButtonVisibility()}
                                addFriendButton={this.getAddFriendButtonVisibility()}
                                readNewsButton={this.getReadNewsButtonVisibility()}
                                removeFriendButton={this.getRemoveFriendButtonVisibility()}
                                onAddToFriends={friend => this.onAddToFriends(friend)}
                                onRemoveSubscription={friend => this.onRemoveFriend(friend)}
                                login={this.props.login}
                                token={this.props.token}
                                onGetGroupId={this.props.onGetGroupIdByRecipient}
                                language={this.props.language}/>
                    <br/>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAllFriends: (userId, token, page, callback) => {
            getAllFriends(userId, token, page).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
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

        onGetAllSubscribers: (userId, token, page, callback) => {
            getAllSubscribers(userId, token, page).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
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

        onGetAllSubscriptions: (userId, token, page, callback) => {
            getAllSubscriptions(userId, token, page).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
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

        onSubcribeOn: (authorName, token, callback) => {
            subscribeOn(authorName, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', json.message));
                    callback();
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
            removeSubscription(authorName, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', json.message));
                    callback();
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
            getGroupIdByRecipient(recipientId, userId, token).then(([response, json]) => {
                if (response.status === 200) {
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
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(FriendsPage);