import React from 'react';
import { Tooltip, OverlayTrigger } from 'react-bootstrap';
import { formatDate } from '../utils.jsx';

/*
    props:
    - comment
    - relatedComment
    - owner - boolean - is user owner of the current book?
    - onDeleteComment - callback
    - token
    - bookId
    - callback - updateReader function
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

    renderRelatedComment() {
        if (this.props.relatedComment) {
            return (
                <div className="well">
                    <h4>{this.props.relatedComment.authorInfo.firstName + ' ' + this.props.relatedComment.authorInfo.lastName}</h4>
                    <p>{this.props.relatedComment.comment}</p>
                </div>
            )
        }
    }

    renderCloseButton() {
        let tooltip = (
            <Tooltip id="tooltip">Delete this comment</Tooltip>
        );
        if (this.props.owner) {
            return (
                <OverlayTrigger placement="top" overlay={tooltip}>
                    <button onClick={() => this.props.onDeleteComment(this.props.bookId, this.props.comment.id, this.props.token, this.props.callback)}
                            className="btn btn-danger btn-xs">X
                    </button>
                </OverlayTrigger>
            )
        }
        return null;
    }

    render() {
        return (
            <div className="row">
                <div className="col-sm-2 text-center">
                    <img src={this.props.comment.authorInfo.avatar + '?date=' + new Date()} className="img-rounded" width="65" height="auto" alt="avatar"/>
                </div>
                <div className="col-sm-10">
                    <h4>{this.getAuthorName()} <small>{this.getCommentDate()} &nbsp; {this.renderCloseButton()}</small></h4>
                    {this.renderRelatedComment()}
                    <p>{this.getCommentText()}</p>
                    <br/>
                </div>
            </div>
        )
    }
}

export default CommentItem;