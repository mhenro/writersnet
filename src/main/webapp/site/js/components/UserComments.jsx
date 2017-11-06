import React from 'react';
import CommentItem from './CommentItem.jsx';

/*
    props:
    - comments - array of comments
 */
class UserComments extends React.Component {
    sortComments(a, b) {
        if (a.created < b.created) {
            return -1;
        }
        if (a.created > b.created) {
            return 1;
        }
        return 0;
    }

    getRelatedComments(parentId) {
        return this.props.comments.filter(comment => comment.relatedTo === parentId);
    }

    render() {
        let sort = (a, b) => this.sortComments(a, b);

        return (
            <div>
                <h4>Leave a comment:</h4>
                <form role="form">
                    <div className="form-group">
                        <textarea className="form-control" rows="3" required></textarea>
                    </div>
                    <button type="submit" className="btn btn-success">Submit</button>
                </form>
                <br/>
                <br/>
                <p>Comments:</p>
                <br/>
                {this.props.comments.sort(sort).map((comment, key) =>
                    <CommentItem comment={comment} key={key}>
                        {this.getRelatedComments(comment.id).sort(sort).map((comment, key) =>
                            <CommentItem comment={comment} key={key}/>
                        )}
                    </CommentItem>
                )}
            </div>
        )
    }
}

export default UserComments;