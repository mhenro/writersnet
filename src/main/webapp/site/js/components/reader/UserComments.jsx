import React from 'react';
import { Pagination } from 'react-bootstrap';
import CommentItem from './CommentItem.jsx';
import { getLocale } from '../../locale.jsx';

/*
    props:
    - comments - array of comments
    - owner - boolean - is user owner of the current book?
    - onGetComments - callback
    - onSaveComment - callback
    - onDeleteComment - callback
    - onSetCommentPage - callback for set current page
    - currentPage - page number
    - totalPages
    - login - user login if existed
    - token
    - bookId
    - updateComments - callback
    - totalPagesCallback
    - language
 */
class UserComments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            comment: '',
            relatedComment: null
        };
    }

    onCommentChange(event) {
        this.setState({
            comment: event.target.value
        });
    }

    clearComment() {
        this.setState({
            comment: '',
            relatedComment: null
        });
    }

    onQuoteComment(comment) {
        this.setState({
            relatedComment: comment
        });
        let commentField = document.getElementById('commentField');
        commentField.scrollIntoView();
    }

    renderRelatedComment() {
        if (this.state.relatedComment) {
            return (
                <div>
                    <small>{getLocale(this.props.language)['Quoted comment:']}</small>
                    <div className="well well-sm">
                        <small>
                            <h4>{this.state.relatedComment.userFullName}</h4>
                            <p>{this.state.relatedComment.comment}</p>
                        </small>
                    </div>
                </div>
            )
        }
    }

    handleSelect(eventKey) {
        this.props.onSetCommentPage(eventKey);
        this.props.onGetComments(this.props.bookId, eventKey, this.props.updateComments, this.props.totalPagesCallback);
    }

    saveComment() {
        if (this.state.comment.trim().length === 0) {
            return;
        }
        let comment = {
            bookId: this.props.bookId,
            userId: this.props.login === 'Anonymous' ? undefined : this.props.login,
            comment: this.state.comment,
            relatedTo: this.state.relatedComment ? this.state.relatedComment.id : undefined
        };
        this.props.onSaveComment(comment, this.props.updateComments);
    }

    onSubmit(event) {
        event.preventDefault();
        this.clearComment();
    }

    renderPagination() {
        if (this.props.comments && this.props.comments.length > 0) {
            return (
                <div className="col-sm-12 text-center">
                    <Pagination
                        className={'shown'}
                        prev
                        next
                        first
                        last
                        ellipsis
                        boundaryLinks
                        items={this.props.totalPages}
                        maxButtons={3}
                        activePage={this.props.currentPage}
                        onSelect={event => this.handleSelect(event)}/>
                    <br/>
                </div>
            )
        }
    }

    render() {
        return (
            <div>
                <h4>{getLocale(this.props.language)['Leave a comment:']}</h4>
                <form role="form" onSubmit={event => this.onSubmit(event)} id="commentField">
                    <div className="form-group">
                        {this.renderRelatedComment()}
                    </div>
                    <div className="form-group">
                        <textarea onChange={event => this.onCommentChange(event)} value={this.state.comment} className="form-control" rows="3" required></textarea>
                    </div>
                    <button onClick={() => this.saveComment()} type="submit" className="btn btn-success">{getLocale(this.props.language)['Submit']}</button>
                </form>
                <br/>
                <br/>
                <p>{getLocale(this.props.language)['Comments:']}</p>
                {this.renderPagination()}
                {this.props.comments.map((comment, key) =>
                    <CommentItem comment={comment}
                                 owner={this.props.owner}
                                 onDeleteComment={this.props.onDeleteComment}
                                 onQuoteComment={comment => this.onQuoteComment(comment)}
                                 token={this.props.token}
                                 bookId={this.props.bookId}
                                 updateComments={this.props.updateComments}
                                 language={this.props.language}
                                 key={key}/>
                )}
                {this.renderPagination()}
            </div>
        )
    }
}

export default UserComments;