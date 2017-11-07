import React from 'react';
import { connect } from 'react-redux';
import ReactStars from 'react-stars';
import {
    getBookDetails,
    setBook,
    addStar,
    getBookComments,
    saveComment,
    deleteComment
} from '../actions/BookActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

import UserComments from '../components/UserComments.jsx';

/*
    props:
    - book
 */
class BookReader extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            comments: []
        };
        ['updateReader', 'newVote', 'renderComments'].map(fn => this[fn] = this[fn].bind(this));

        this.props.onGetBookDetails(this.props.match.params.bookId);
        this.props.onGetBookComments(this.props.match.params.bookId, this.renderComments);
    }

    getAverageRating() {
        return parseFloat(this.props.book.totalRating.averageRating.toFixed(2));
    }

    getTotalVotes() {
        return this.props.book.totalRating.userCount;
    }

    newVote(estimation) {
        this.props.onAddStar(this.props.book.id, estimation, this.updateReader);
    }

    isRatingAllowed() {
        return true;
    }

    updateReader() {
        this.props.onGetBookDetails(this.props.match.params.bookId);
        this.props.onGetBookComments(this.props.match.params.bookId, this.renderComments);
    }

    renderComments(comments) {
        this.setState({
            comments: comments
        });
    }

    isDataLoaded() {
        if (!this.props.book) {
            return false;
        }
        return true;
    }

    render() {
        if (!this.isDataLoaded()) {
            return null;
        }

        return (
            <div className="col-sm-12">
                <div className="col-sm-12 section-name" style={{textAlign: 'center'}}>
                    {this.props.book.author.firstName + ' ' + this.props.book.author.lastName}
                </div>
                <div className="col-sm-12 section-author-name" style={{textAlign: 'center'}}>
                    {this.props.book.name}
                </div>
                <div className="col-sm-12">
                    <br/>
                    <hr/>
                    <br/>
                    <div className="col-sm-6">
                        <ReactStars count={5} size={18} color2={'orange'} half={false} value={this.getAverageRating()} onChange={this.newVote}/>
                        &nbsp;
                        <span><b>{this.getAverageRating() + ' * ' + this.getTotalVotes()}</b></span>
                        <br/>
                        <br/>
                        <br/>
                    </div>
                    <div className="col-sm-6"></div>
                </div>
                <div className="col-sm-12">
                    <div dangerouslySetInnerHTML={{ __html: this.props.book.bookText.text }} />
                    <hr/>
                </div>
                <UserComments comments={this.state.comments}
                              owner={this.props.login === this.props.book.author.username}
                              onSaveComment={this.props.onSaveComment}
                              onDeleteComment={this.props.onDeleteComment}
                              bookId={this.props.book.id}
                              login={this.props.login}
                              callback={this.updateReader}
                              token={this.props.token}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        book: state.BookReducer.book,
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetBookDetails: (bookId) => {
            return getBookDetails(bookId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setBook(json));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetBookComments: (bookId, callback) => {
            return getBookComments(bookId).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.content);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSaveComment: (comment, callback) => {
            return saveComment(comment).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Your comment was added'));
                    callback();
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onDeleteComment: (bookId, commentId, token, callback) => {
            return deleteComment(bookId, commentId, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Comment was deleted'));
                    callback();
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onAddStar: (bookId, starValue, callback) => {
            return addStar(bookId, starValue).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', "Your vote was added"));
                    callback();
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(BookReader);