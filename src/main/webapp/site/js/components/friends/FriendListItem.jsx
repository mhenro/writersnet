import React from 'react';
import PropTypes from 'prop-types';

/*
    props:
    - friend
     - sendMsgButton - boolean
     - addFriendButton - boolean
 */
class FriendListItem extends React.Component {
    static contextTypes = {
        router: PropTypes.shape({
            history: PropTypes.shape({
                push: PropTypes.func.isRequired,
                replace: PropTypes.func.isRequired
            }).isRequired,
            staticContext: PropTypes.object
        }).isRequired
    };

    onAuthorClick() {
        this.context.router.history.push('/authors/' + this.props.friend.id);
    }

    renderAddFriendButton() {
        if (this.props.addFriendButton) {
            return <button className="btn btn-xs btn-success">Add to friends</button>;
        }
    }

    renderSendMsgButton() {
        if (this.props.sendMsgButton) {
            return <button className="btn btn-xs btn-success">Send message</button>;
        }
    }

    render() {
        return (
            <div className="col-sm-12">
                <hr/>
                <div className="col-sm-1">
                    <img src={this.props.friend.avatar + '?date=' + new Date()} onClick={() => this.onAuthorClick()} className="img-rounded clickable" width="100%" height="auto"/>
                </div>
                <div className="col-sm-11">
                    <div className="friendlist-item-name">{this.props.friend.name}</div>
                    <div className="friendlist-item-section">{this.props.friend.section}</div>
                    {this.renderAddFriendButton()}
                    {this.renderSendMsgButton()}
                </div>
                <div className="col-sm-12">
                    <br/>
                    <br/>
                </div>

            </div>
        )
    }
}

export default FriendListItem;