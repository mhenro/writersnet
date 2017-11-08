import React from 'react';
import { Pagination } from 'react-bootstrap';
import CommentItem from './CommentItem.jsx';

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
    - callback - updateReader function
    - totalPagesCallback
 */
class UserComments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            comment: '',
            relatedComment: null
        };
        ['onCommentChange', 'onQuoteComment', 'handleSelect', 'onSubmit'].map(fn => this[fn] = this[fn].bind(this));
    }

    sortComments(a, b) {
        if (a.created < b.created) {
            return -1;
        }
        if (a.created > b.created) {
            return 1;
        }
        return 0;
    }

    getRelatedComment(parentId) {
        if (!parentId) {
            return null;
        }
        return this.props.comments.filter(comment => comment.id === parentId)[0];
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
    }

    renderRelatedComment() {
        if (this.state.relatedComment) {
            return (
                <div>
                    <small>Quoted comment:</small>
                    <div className="well well-sm">
                        <small>
                            <h4>{this.state.relatedComment.authorInfo.firstName + ' ' + this.state.relatedComment.authorInfo.lastName}</h4>
                            <p>{this.state.relatedComment.comment}</p>
                        </small>
                    </div>
                </div>
            )
        }
    }

    handleSelect(eventKey) {
        this.props.onSetCommentPage(eventKey);
        this.props.onGetComments(this.props.bookId, eventKey, this.props.callback, this.props.totalPagesCallback);
    }

    saveComment() {
        let comment = {
            bookId: this.props.bookId,
            userId: this.props.login === 'Anonymous' ? undefined : this.props.login,
            comment: this.state.comment,
            relatedTo: this.state.relatedComment ? this.state.relatedComment.id : undefined
        };
        this.props.onSaveComment(comment, this.props.callback);
    }

    onSubmit(event) {
        event.preventDefault();
        this.clearComment();
    }

    render() {
        let sort = (a, b) => this.sortComments(a, b);

        return (
            <div>
                <h4>Leave a comment:</h4>
                <form role="form" onSubmit={this.onSubmit}>
                    <div className="form-group">
                        {this.renderRelatedComment()}
                    </div>
                    <div className="form-group">
                        <textarea onChange={this.onCommentChange} value={this.state.comment} className="form-control" rows="3" required></textarea>
                    </div>
                    <button onClick={() => this.saveComment()} type="submit" className="btn btn-success">Submit</button>
                </form>
                <br/>
                <br/>
                <p>Comments:</p>
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
                    onSelect={this.handleSelect}/>
                <br/>
                {this.props.comments.sort(sort).map((comment, key) =>
                    <CommentItem comment={comment}
                                 relatedComment={this.getRelatedComment(comment.relatedTo)}
                                 owner={this.props.owner}
                                 onDeleteComment={this.props.onDeleteComment}
                                 onQuoteComment={this.onQuoteComment}
                                 token={this.props.token}
                                 bookId={this.props.bookId}
                                 callback={this.props.callback}
                                 key={key}/>
                )}
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
                        onSelect={this.handleSelect}/>
                </div>
            </div>
        )
    }
}

export default UserComments;