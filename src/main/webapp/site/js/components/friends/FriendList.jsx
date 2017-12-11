import React from 'react';
import FriendListItem from './FriendListItem.jsx';

/*
    props:
    - friends - array
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
class FriendList extends React.Component {
    getSortedFriends() {
        return this.props.friends.sort((a, b) => {
            return a.name.localeCompare(b.name);
        });
    }

    render() {
        return (
            <div>
                {this.getSortedFriends().map((friend, key) => {
                    return (
                        <FriendListItem friend={friend} key={key}
                                        sendMsgButton={this.props.sendMsgButton}
                                        addFriendButton={this.props.addFriendButton}
                                        readNewsButton={this.props.readNewsButton}
                                        removeFriendButton={this.props.removeFriendButton}
                                        onAddToFriends={this.props.onAddToFriends}
                                        onRemoveSubscription={this.props.onRemoveSubscription}
                                        login={this.props.login}
                                        token={this.props.token}
                                        onGetGroupId={this.props.onGetGroupId}/>
                    )
                })}
            </div>
        )
    }
}

export default FriendList;