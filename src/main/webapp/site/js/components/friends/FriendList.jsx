import React from 'react';
import FriendListItem from './FriendListItem.jsx';

/*
    props:
    - friends - array
    - sendMsgButton - boolean
    - addFriendButton - boolean
 */
class FriendList extends React.Component {
    render() {
        return (
            <div>
                {this.props.friends.map((friend, key) => {
                    return (
                        <FriendListItem friend={friend} key={key} sendMsgButton={this.props.sendMsgButton} addFriendButton={this.props.addFriendButton}/>
                    )
                })}
            </div>
        )
    }
}

export default FriendList;