import React from 'react';
import { Modal, Button, Tooltip, OverlayTrigger } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { formatDate } from '../../utils.jsx';
import { getLocale } from '../../locale.jsx';

/*
    props:
    - comment
    - relatedComment
    - owner - boolean - is user owner of the current book?
    - onDeleteComment - callback
    - onQuoteComment - callback
    - token
    - bookId
    - updateComments - callback
    - language
 */
class CommentItem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            confirmDialogShow: false
        };
    }

    getAuthorName() {
        if (this.props.comment.userId) {
            return <Link to={'/authors/' + this.props.comment.userId}>{this.props.comment.userFullName}</Link>
        } else {
            return this.props.comment.userFullName;
        }
    }

    getCommentDate(created = this.props.comment.created) {
        let date = new Date(created);
        return formatDate(date);
    }

    getCommentText() {
        return this.props.comment.comment;
    }

    renderRelatedComment() {
        if (this.props.comment.relatedTo) {
            return (
                <div className="well">
                    <h4>{this.props.comment.relatedTo.userFullName} <small>{this.getCommentDate(this.props.comment.relatedTo.created)}</small></h4>
                    <p>{this.props.comment.relatedTo.comment}</p>
                </div>
            )
        }
    }

    renderQuoteButton() {
        let tooltip = (
            <Tooltip id="tooltip">{getLocale(this.props.language)['Quote this comment']}</Tooltip>
        );
        return (
            <OverlayTrigger placement="top" overlay={tooltip}>
                <button onClick={() => this.props.onQuoteComment(this.props.comment)} className="btn btn-default btn-xs glyphicon glyphicon-circle-arrow-right"></button>
            </OverlayTrigger>
        )
    }

    renderCloseButton() {
        let tooltip = (
            <Tooltip id="tooltip">{getLocale(this.props.language)['Delete this comment']}</Tooltip>
        );
        if (this.props.owner) {
            return (
                <OverlayTrigger placement="top" overlay={tooltip}>
                    <button onClick={() => this.onConfirm()}
                            className="btn btn-danger btn-xs glyphicon glyphicon-remove"></button>
                </OverlayTrigger>
            )
        }
        return null;
    }

    onConfirm() {
        this.setState({
            confirmDialogShow: true
        });
    }

    onDelete() {
        this.props.onDeleteComment(this.props.bookId, this.props.comment.id, this.props.token, this.props.updateComments);
        this.onCancel();
    }

    onCancel() {
        this.setState({
            confirmDialogShow: false
        });
    }

    render() {
        return (
            <div className="row">
                <div className="col-sm-2 text-center">
                    <img src={this.props.comment.userAvatar /*+ '?date=' + new Date()*/} className="img-rounded" width="65" height="auto" alt="avatar"/>
                </div>
                <div className="col-sm-10">
                    <h4>{this.getAuthorName()} <small>{this.getCommentDate()} &nbsp; {this.renderQuoteButton()} &nbsp; {this.renderCloseButton()}</small></h4>
                    {this.renderRelatedComment()}
                    <p>{this.getCommentText()}</p>
                    <br/>
                    <small>{'#' + this.props.comment.id}</small>
                    <div><hr/></div>
                </div>

                {/* delete confirmation dialog */}
                <Modal show={this.state.confirmDialogShow} onHide={() => this.onCancel()}>
                    <Modal.Header>
                        {getLocale(this.props.language)['Attention!']}
                    </Modal.Header>
                    <Modal.Body>
                        {getLocale(this.props.language)['Are you sure you want to delete this comment?']}
                    </Modal.Body>
                    <Modal.Footer>
                        <div className="btn-group">
                            <Button onClick={() => this.onDelete()} className="btn btn-danger">{getLocale(this.props.language)['Delete']}</Button>
                            <Button onClick={() => this.onCancel()} className="btn btn-default">{getLocale(this.props.language)['Cancel']}</Button>
                        </div>
                    </Modal.Footer>
                </Modal>
            </div>
        )
    }
}

export default CommentItem;