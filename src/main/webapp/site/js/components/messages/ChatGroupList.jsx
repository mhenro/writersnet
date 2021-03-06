import React from 'react';
import ChatGroupListItem from './ChatGroupListItem.jsx';

/*
    props:
    - groups - array
    - author
 */
class ChatGroupList extends React.Component {
    getSortedChatGroups() {
        return this.props.groups.sort((a, b) => {
            return b.lastMessageDate - a.lastMessageDate;
        });
    }

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
        return this.getSortedChatGroups().map((group, key) => {
                return (
                    <ChatGroupListItem group={group} author={this.props.author} key={key}/>
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