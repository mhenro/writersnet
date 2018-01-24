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
    - language
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
                                        onAddToFriends={this.props.onAddToFriends}
                                        onRemoveSubscription={this.props.onRemoveSubscription}
                                        login={this.props.login}
                                        token={this.props.token}
                                        onGetGroupId={this.props.onGetGroupId}
                                        language={this.props.language}/>
                    )
                })}
            </div>
        )
    }
}

export default FriendList;