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
 */
class FriendList extends React.Component {
    render() {
        return (
            <div>
                {this.props.friends.map((friend, key) => {
                    return (
                        <FriendListItem friend={friend} key={key}
                                        sendMsgButton={this.props.sendMsgButton}
                                        addFriendButton={this.props.addFriendButton}
                                        readNewsButton={this.props.readNewsButton}
                                        removeFriendButton={this.props.removeFriendButton}
                                        onAddToFriends={this.props.onAddToFriends}/>
                    )
                })}
            </div>
        )
    }
}

export default FriendList;