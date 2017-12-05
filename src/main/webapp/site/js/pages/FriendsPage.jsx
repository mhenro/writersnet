import React from 'react';
import { connect } from 'react-redux';

import FriendList from '../components/friends/FriendList.jsx';

import {
    getAuthorDetails,
    setAuthor,
    subscribeOn
} from '../actions/AuthorActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';
import {
    isFriend,
    isSubscriber,
    isSubscription
} from '../utils.jsx';

class FriendsPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeTab: 'friends'    //requests
        };
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

    getFriends() {
        return this.props.author.subscribers
            .filter(subscriber => this.props.author.subscriptions.some(subscription => subscription.subscriberId === subscriber.subscriptionId))
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
        return this.props.author.subscriptions
            .filter(subscription => subscription.subscriptionId === this.props.author.username)
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
        return this.props.author.subscribers
            .filter(subscriber => subscriber.subscriberId === this.props.author.username)
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

    render() {
        if (!this.isDataLoaded()) {
            return null;
        }

        return (
            <div>
                <ul className="nav nav-tabs">
                    <li className={this.getActiveClass('friends')}><a onClick={() => this.changeTab('friends')}>My friends</a></li>
                    <li className={this.getActiveClass('subscribers')}><a onClick={() => this.changeTab('subscribers')}>Subscribers</a></li>
                    <li className={this.getActiveClass('subscriptions')}><a onClick={() => this.changeTab('subscriptions')}>Subscriptions</a></li>
                </ul>
                <br/>
                <div className="input-group">
                    <input type="text" className="form-control" placeholder="Search" />
                        <div className="input-group-btn">
                            <button className="btn btn-default" type="submit">
                                <i className="glyphicon glyphicon-search"></i>
                            </button>
                        </div>
                </div>
                <FriendList friends={this.getItems()} sendMsgButton={this.getSendMsgButtonVisibility()} addFriendButton={this.getAddFriendButtonVisibility()}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        login: state.GlobalReducer.user.login,
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
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(FriendsPage);