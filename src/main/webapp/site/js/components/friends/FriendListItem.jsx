import React from 'react';
import PropTypes from 'prop-types';

/*
    props:
    - friend
    - sendMsgButton - boolean
    - addFriendButton - boolean
    - readNewsButton - boolean
    - removeFriendButton - boolean
    - onAddToFriends - callback
    - onRemoveSubscription - callback
    - login
    - token
    - onGetGroupId - callback
 */
class FriendListItem extends React.Component {
    static contextTypes = {
        router: PropTypes.shape({
            history: PropTypes.shape({
                push: PropTypes.func.isRequired,
                replace: PropTypes.func.isRequired
            }).isRequired,
            staticContext: PropTypes.object
        }).isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            groupId: null
        };
    }

    componentDidMount() {
        this.props.onGetGroupId(this.props.friend.id, this.props.login, this.props.token, groupId => this.setGroupId(groupId));
    }

    setGroupId(groupId) {
        this.setState({
            groupId: groupId
        });
    }

    onAuthorClick() {
        this.context.router.history.push('/authors/' + this.props.friend.id);
    }

    renderAddFriendButton() {
        if (this.props.addFriendButton) {
            return <button onClick={() => this.props.onAddToFriends(this.props.friend.id)} className="btn btn-xs btn-success">Add to friends</button>;
        }
    }

    onSendMessage() {
        this.context.router.history.push('/chat/' + this.state.groupId);
    }

    renderSendMsgButton() {
        if (this.props.sendMsgButton && this.state.groupId) {
            return <button onClick={() => this.onSendMessage()} className="btn btn-xs btn-success">Send message</button>;
        }
    }

    renderReadNewsButton() {
        return null;
        /*if (this.props.readNewsButton) {
            return <button className="btn btn-xs btn-success">Read news</button>;
        }*/
    }

    renderRemoveFriendButton() {
        if (this.props.removeFriendButton) {
            return <button onClick={() => this.props.onRemoveSubscription(this.props.friend.id)} className="btn btn-xs btn-success">Remove</button>;
        }
    }

    render() {
        return (
            <div className="col-sm-12">
                <hr/>
                <div className="col-sm-1">
                    <img src={this.props.friend.avatar + '?date=' + new Date()} onClick={() => this.onAuthorClick()} className="img-rounded clickable" width="100%" height="auto"/>
                </div>
                <div className="col-sm-11">
                    <div className="friendlist-item-name">{this.props.friend.name}</div>
                    <div className="friendlist-item-section">{this.props.friend.section}</div>
                    <div className="btn-group-vertical">
                        {this.renderAddFriendButton()}
                        {this.renderSendMsgButton()}
                        {this.renderReadNewsButton()}
                        {this.renderRemoveFriendButton()}
                    </div>
                </div>
            </div>
        )
    }
}

export default FriendListItem;