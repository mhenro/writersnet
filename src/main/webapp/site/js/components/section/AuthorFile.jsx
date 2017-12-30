import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';

/*
    props:
    - me - boolean - authorized user on his page
    - author - page owner
    - registered
    - login - registered user id
    - onAddToFriends - callback function
    - friendship - array of booleans which represents relationships between authors
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
        if (this.props.me) {
            this.context.router.history.push('/options');
        }
    }

    getFriendsButtonCaption() {
        if (this.props.friendship.friend) {
            return 'Already in friends';
        }
        if (this.props.friendship.subscription) {
            return 'You are already subscribed';
        }
        return 'Add to friends';
    }

    getFriendsButtonClass() {
        let baseCls = 'btn btn-success ' + (this.props.me ? 'hidden' : '');
        if (this.props.friendship.friend || this.props.friendship.subscription) {
            baseCls += ' disabled';
        }

        return baseCls;
    }

    onAddToFriends() {
        if (!this.props.friendship.friend && !this.props.friendship.subscription) {
            this.props.onAddToFriends(this.props.author.username);
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
                            <button className={'btn btn-success ' + (this.props.registered && !this.props.me ? '' : 'hidden')}>Send message</button>
                            <br/>
                            <button onClick={() => this.onAddToFriends()} className={this.getFriendsButtonClass()}>{this.getFriendsButtonCaption()}</button>
                            <br/>
                            <Link to="/options" className={'btn btn-success ' + (this.props.me ? '' : 'hidden')}>Options</Link>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AuthorFile;