import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';

/*
    props:
    - author
    - registered
    - login - user id
    - onAddToFriends - callback function
 */
class AuthorFile extends React.Component {
    static contextTypes = {
        router: PropTypes.shape({
            history: PropTypes.shape({
                push: PropTypes.func.isRequired,
                replace: PropTypes.func.isRequired
            }).isRequired,
            staticContext: PropTypes.object
        }).isRequired
    };

    onAuthorClick() {
        if (this.props.registered && this.props.login === this.props.author.username) {
            //window.location.href = window.location.origin + '/options';
            this.context.router.history.push('/options');
        }
    }

    isFriend() {
        return this.props.author.subscribers.some(subscriber => subscriber.subscriberName === this.props.login)
            && this.props.author.subscriptions.some(subscription => subscription.subscriptionName === this.props.login);
    }

    isSubscriber() {
        return this.props.author.subscriptions.some(subscription => subscription.subscriptionName === this.props.login);
    }

    isSubscription() {
        return this.props.author.subscribers.some(subscriber => subscriber.subscriberName === this.props.login);
    }

    getFriendsButtonCaption() {
        if (this.isFriend()) {
            return 'Already in friends';
        }
        if (this.isSubscription()) {
            return 'You are already subscribed';
        }
        return 'Add to friends';
    }

    getFriendsButtonClass() {
        let baseCls = 'btn btn-success ' + (this.props.registered && this.props.login !== this.props.author.username? '' : 'hidden');
        if (this.isFriend() || this.isSubscription()) {
            baseCls += ' disabled';
        }

        return baseCls;
    }

    onAddToFriends() {
        if (!this.isFriend() || !this.isSubscription()) {
            this.props.onAddToFriends(this.props.login, this.props.author.username);
        }
    }

    render() {
        return (
            <div className="panel panel-default" style={{padding: '10px'}}>
                <div className="row">
                    <div className="col-sm-12" style={{textAlign: 'center'}}>
                        <img src={this.props.author.avatar + '?date=' + new Date()} onClick={() => this.onAuthorClick()} className="img-rounded clickable" width="100%" height="auto"/>
                    </div>
                </div>
                <br/>
                <div className="row">
                    <div className="col-sm-12" style={{textAlign: 'center'}}>
                        <div className="btn-group-vertical">
                            <button className={'btn btn-success ' + (this.props.registered && this.props.login !== this.props.author.username ? '' : 'hidden')}>Send message</button>
                            <br/>
                            <button onClick={() => this.onAddToFriends()} className={this.getFriendsButtonClass()}>{this.getFriendsButtonCaption()}</button>
                            <br/>
                            <Link to="/options" className={'btn btn-success ' + (this.props.registered && this.props.login === this.props.author.username ? '' : 'hidden')}>Options</Link>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AuthorFile;