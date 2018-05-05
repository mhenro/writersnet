import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { formatDate } from '../../utils.jsx';

/*
    props:
    - message
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

    getAuthorName() {
        return <Link to={'/authors/' + this.props.message.creatorId}>{this.props.message.creatorFullName}</Link>;
    }

    getAuthorAvatar() {
        return this.props.message.creatorAvatar;
    }

    getMessageDate() {
        return formatDate(new Date(this.props.message.created));
    }

    getMessageText() {
        return this.props.message.message;
    }

    render() {
        return (
            <div>
                <hr/>
                <div className="row">
                    <div className="col-sm-1 col-xs-3">
                        <img src={this.getAuthorAvatar()/* + '?date=' + new Date()*/} className="img-rounded" width="100%" height="auto"/>
                    </div>
                    <div className="col-sm-11 col-xs-9">
                        <div className="row">
                            <div className="col-sm-6 col-xs-6 text-left">
                                {this.getAuthorName()}
                            </div>
                            <div className="col-sm-6 col-xs-6 text-right">
                                {this.getMessageDate()}
                            </div>
                        </div>
                        <div className="col-sm-12">
                            {this.getMessageText()}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default ChatGroupListItem;