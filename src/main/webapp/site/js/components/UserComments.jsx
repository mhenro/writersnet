import React from 'react';
import CommentItem from './CommentItem.jsx';

/*
    props:
    - comments - array of comments
    - owner - boolean - is user owner of the current book?
    - onSaveComment - callback
    - onDeleteComment - callback
    - login - user login if existed
    - token
    - bookId
    - callback - updateReader function
 */
class UserComments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            comment: ''
        };
        ['onCommentChange'].map(fn => this[fn] = this[fn].bind(this));
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

    saveComment() {
        let comment = {
            bookId: this.props.bookId,
            userId: this.props.login === 'Anonymous' ? undefined : this.props.login,
            comment: this.state.comment
        };
        this.props.onSaveComment(comment, this.props.callback);
    }

    onSubmit(event) {
        event.preventDefault();
    }

    render() {
        let sort = (a, b) => this.sortComments(a, b);

        return (
            <div>
                <h4>Leave a comment:</h4>
                <form role="form" onSubmit={this.onSubmit}>
                    <div className="form-group">
                        <textarea onChange={this.onCommentChange} className="form-control" rows="3" required></textarea>
                    </div>
                    <button onClick={() => this.saveComment()} type="submit" className="btn btn-success">Submit</button>
                </form>
                <br/>
                <br/>
                <p>Comments:</p>
                <br/>
                {this.props.comments.sort(sort).map((comment, key) =>
                    <CommentItem comment={comment}
                                 relatedComment={this.getRelatedComment(comment.relatedTo)}
                                 owner={this.props.owner}
                                 onDeleteComment={this.props.onDeleteComment}
                                 token={this.props.token}
                                 bookId={this.props.bookId}
                                 callback={this.props.callback}
                                 key={key}/>
                )}
            </div>
        )
    }
}

export default UserComments;