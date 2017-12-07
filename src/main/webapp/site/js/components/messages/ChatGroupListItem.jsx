import React from 'react';
import {formatDate} from '../../utils.jsx';

/*
    props:
    - group
*/
class ChatGroupListItem extends React.Component {
    getName() {
        return this.props.group.creatorFullName;
    }

    getDate() {
        return formatDate(new Date(this.props.group.created));
    }

    getLastMessage() {
        return this.props.group.lastMessage;
    }

    render() {
        return (
            <div>
                <hr/>
                <div className="row">
                    <div className="col-sm-1">
                        <img src={'https://localhost/css/images/avatars/default_avatar.png?date=' + new Date()} className="img-rounded clickable" width="100%" height="auto"/>
                    </div>
                    <div className="col-sm-11">
                        <div className="row">
                            <div className="col-sm-6 chat-group-header">
                                {this.getName()}
                            </div>
                            <div className="col-sm-6 text-right">
                                {this.getDate()}
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-1">
                                <img src={'https://localhost/css/images/avatars/default_avatar.png?date=' + new Date()} className="img-rounded clickable" width="100%" height="auto"/>
                            </div>
                            <div className="col-sm-11 chat-group-body">
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