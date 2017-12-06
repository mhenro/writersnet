import React from 'react';

/*
    props:
    - group
*/
class ChatGroupListItem extends React.Component {
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
                                FirstName LastName
                            </div>
                            <div className="col-sm-6 text-right">
                                28-11-12 16:30:21
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-1">
                                <img src={'https://localhost/css/images/avatars/default_avatar.png?date=' + new Date()} className="img-rounded clickable" width="100%" height="auto"/>
                            </div>
                            <div className="col-sm-11 chat-group-body">
                                Last message in group...
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default ChatGroupListItem;