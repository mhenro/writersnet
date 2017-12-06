import React from 'react';

import ChatGroupListItem from './ChatGroupListItem.jsx';

/*
    props:
    - groups - array
 */
class ChatGroupList extends React.Component {
    renderChatGroups() {
        if (this.props.groups.length === 0) {
            return (
                <div className="col-sm-12 text-center">
                    <br/>
                    <br/>
                    <br/>
                    <span>No messages yet...</span>
                    <br/>
                    <br/>
                    <br/>
                </div>
            )
        }
        return this.props.groups.map((group, key) => {
                return (
                    <ChatGroupListItem group={group} key={key}/>
                )
        });
    }

    render() {
        return (
            <div>
                {this.renderChatGroups()}
            </div>
        )
    }
}

export default ChatGroupList;