import React from 'react';
import FriendListItem from './FriendListItem.jsx';

/*
    props:
    - friends - array
 */
class FriendList extends React.Component {
    render() {
        return (
            <div>
                {this.props.friends.map((friend, key) => {
                    return (
                        <FriendListItem friend={friend} key={key}/>
                    )
                })}
            </div>
        )
    }
}

export default FriendList;