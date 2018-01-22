import React from 'react';
import PropTypes from 'prop-types';
import {formatDate} from '../../utils.jsx';

/*
    props:
    - group
    - author
*/
class ChatGroupListItem extends React.Component {
    static contextTypes = {
        router: PropTypes.shape({
            history: PropTypes.shape({
                push: PropTypes.func.isRequired,
                replace: PropTypes.func.isRequired
            }).isRequired,
            staticContext: PropTypes.object
        }).isRequired
    };

    getUserFullName(user) {
        return user.firstName + ' ' + user.lastName;
    }

    getGroupName() {
        if (this.getUserFullName(this.props.author) === this.props.group.primaryRecipientFullName) {
            return this.props.group.creatorFullName;
        }
        return this.props.group.primaryRecipientFullName;
    }

    getGroupAvatar() {
        return this.props.group.primaryRecipientAvatar;
    }

    getLastMessageDate() {
        return formatDate(new Date(this.props.group.lastMessageDate));
    }

    getLastMessage() {
        return this.props.group.lastMessageText;
    }

    getLastMessageAvatar() {
        return this.props.group.lastMessageAvatar;
    }

    onItemClick() {
        this.context.router.history.push('/chat/' + this.props.group.id);
    }

    isUnread() {
        if (this.props.group.creatorId === this.props.author.username) {
            return this.props.group.unreadByCreator;
        } else {
            return this.props.group.unreadByRecipient;
        }
    }

    render() {
        return (
            <div>
                <hr/>
                <div className={'row clickable ' + (this.isUnread() ? 'unread' : '')} onClick={() => this.onItemClick()}>
                    <div className="col-sm-1 col-xs-3">
                        <img src={this.getGroupAvatar()/* + '?date=' + new Date()*/} className="img-rounded" width="100%" height="auto"/>
                    </div>
                    <div className="col-sm-11 col-xs-9">
                        <div className="row">
                            <div className="col-sm-6 col-xs-6 chat-group-header">
                                {this.getGroupName()}
                            </div>
                            <div className="col-sm-6 col-xs-6 text-right">
                                {this.getLastMessageDate()}
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-1 col-xs-3">
                                <img src={this.getLastMessageAvatar() /*+ '?date=' + new Date()*/} className="img-rounded" width="100%" height="auto"/>
                            </div>
                            <div className="col-sm-11 col-xs-9 chat-group-body">
                                {this.getLastMessage()}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default ChatGroupListItem;