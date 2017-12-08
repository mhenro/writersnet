import React from 'react';

import MessageListItem from './MessageListItem.jsx';

/*
    props:
    - messages - array
 */
class MessageList extends React.Component {
    render() {
        return (
            <div>
                {this.props.messages.map((message, key) => {
                    return (
                        <MessageListItem message={message} key={key}/>
                    )
                })}
            </div>
        )
    }
}

export default MessageList;