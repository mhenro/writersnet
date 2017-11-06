import React from 'react';
import { formatDate } from '../utils.jsx';

/*
    props:
    - comment
 */
class CommentItem extends React.Component {
    getAuthorName() {
        return this.props.comment.authorInfo.firstName + ' ' + this.props.comment.authorInfo.lastName;
    }

    getCommentDate() {
        let date = new Date(this.props.comment.created);
        return formatDate(date);
    }

    getCommentText() {
        return this.props.comment.comment;
    }

    render() {
        return (
            <div className="row">
                <div className="col-sm-2 text-center">
                    <img src={this.props.comment.authorInfo.avatar + '?date=' + new Date()} className="img-rounded" width="65" height="auto" alt="avatar"/>
                </div>
                <div className="col-sm-10">
                    <h4>{this.getAuthorName()} <small>{this.getCommentDate()}</small></h4>
                    <p>{this.getCommentText()}</p>
                    <br/>
                </div>
            </div>
        )
    }
}

export default CommentItem;