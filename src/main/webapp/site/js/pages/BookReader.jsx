import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';
import ReactStars from 'react-stars';
import { Link } from 'react-router-dom';
import Diff from 'react-diff';

import {
    getBookDetails,
    setBook,
    addStar,
    getBookComments,
    saveComment,
    deleteComment
} from '../actions/BookActions.jsx';
import {
    createNotify,
    goToComments
} from '../actions/GlobalActions.jsx';
import { setToken } from '../actions/AuthActions.jsx';

import UserComments from '../components/UserComments.jsx';

/*
    props:
    - book
 */
class BookReader extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            comments: [],
            currentPage: 1,
            totalPages: 1,
            bookPage: 1,
            bookTotalPages: 1
        };

        this.props.onGetBookDetails(this.props.match.params.bookId, this.state.bookPage, totalPages => this.setBookTotalPages(totalPages));
        this.props.onGetBookComments(this.props.match.params.bookId, this.state.currentPage, comments => this.renderComments(comments), totalPages => this.setTotalPages(totalPages));
    }

    componentDidMount() {
        if (this.props.goToComments) {
            let timer = setInterval(() => {
                let anchor = document.getElementById('commentsAnchor');
                if (anchor) {
                    anchor.scrollIntoView();
                    this.props.onGoToComments(false);
                    clearInterval(timer);
                }
            }, 500);
        } else {
            window.scrollTo(0, 0);
        }
    }

    getAuthorName() {
        return <Link className="section-name" to={'/authors/' + this.props.book.authorId}>{this.props.book.authorFullName}</Link>
    }

    getAverageRating() {
        return parseFloat(this.props.book.totalRating.averageRating.toFixed(2));
    }

    getTotalVotes() {
        return this.props.book.totalRating.userCount;
    }

    newVote(estimation) {
        this.props.onAddStar(this.props.book.id, estimation, () => this.updateBook());
    }

    isRatingAllowed() {
        return true;
    }

    setCommentPage(page) {
        this.setState({
            currentPage: page
        });
    }

    setTotalPages(totalPages) {
        this.setState({
            totalPages: totalPages
        });
    }

    updateBook(page = 1) {
        this.props.onGetBookDetails(this.props.match.params.bookId, page, totalPages => this.setBookTotalPages(totalPages));
    }

    pageSelect(page) {
        this.setState({
            bookPage: page
        });
        this.updateBook(page);
        window.scrollTo(0, 0);
    }

    setBookTotalPages(totalPages) {
        this.setState({
            bookTotalPages: totalPages + 1
        });
    }

    updateComments() {
        this.props.onGetBookComments(this.props.match.params.bookId, this.state.currentPage, comments => this.renderComments(comments), totalPages => this.setTotalPages(totalPages));
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
                <div className="col-sm-12 section-name text-center">
                    {this.getAuthorName()}
                </div>
                <div className="col-sm-12 section-author-name text-center">
                    {this.props.book.name}
                </div>
                <div className="col-sm-12">
                    <br/>
                    <hr/>
                    <br/>
                    <div className="col-sm-6">
                        <ReactStars count={5} size={18} color2={'orange'} half={false} value={this.getAverageRating()} onChange={estimation => this.newVote(estimation)}/>
                        &nbsp;
                        <span><b>{this.getAverageRating() + ' * ' + this.getTotalVotes()}</b></span>
                        <br/>
                        <br/>
                        <br/>
                    </div>
                    <div className="col-sm-6"></div>
                </div>
                <div className="col-sm-12 text-center">
                    <Pagination
                        className={'shown'}
                        prev
                        next
                        first
                        last
                        ellipsis
                        boundaryLinks
                        items={this.state.bookTotalPages}
                        maxButtons={3}
                        activePage={this.state.bookPage}
                        onSelect={page => this.pageSelect(page)}/>
                    <br/>
                </div>
                <div className="col-sm-12 text-justify">
                    <div dangerouslySetInnerHTML={{ __html: this.props.book.bookText.text }} />
                    <br/>
                </div>
                {/* TODO: activate this feature for premium accounts */}
                {/*<div className="col-sm-12">
                    <Diff inputA={this.props.book.bookText.prevText ? this.props.book.bookText.prevText.replace(/<br>/g, '') : this.props.book.bookText.text.replace(/<br>/g, '')} inputB={this.props.book.bookText.text.replace(/<br>/g, '')} type="sentences" />
                </div>*/}
                <div className="col-sm-12 text-center">
                    <br/>
                    <Pagination
                        className={'shown'}
                        prev
                        next
                        first
                        last
                        ellipsis
                        boundaryLinks
                        items={this.state.bookTotalPages}
                        maxButtons={3}
                        activePage={this.state.bookPage}
                        onSelect={page => this.pageSelect(page)}/>
                    <hr/>
                </div>
                <div id="commentsAnchor" className="col-sm-12"></div>
                <UserComments comments={this.state.comments}
                              owner={this.props.login === this.props.book.authorId}
                              onGetComments={this.props.onGetBookComments}
                              onSaveComment={this.props.onSaveComment}
                              onDeleteComment={this.props.onDeleteComment}
                              onSetCommentPage={page => this.setCommentPage(page)}
                              currentPage={this.state.currentPage}
                              totalPages={this.state.totalPages}
                              bookId={this.props.book.id}
                              login={this.props.login}
                              updateComments={() => this.updateComments()}
                              totalPagesCallback={totalPages => this.setTotalPages(totalPages)}
                              token={this.props.token}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        book: state.BookReducer.book,
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token,
        goToComments: state.GlobalReducer.goToComments
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetBookDetails: (bookId, page, callbackTotalPages) => {
            return getBookDetails(bookId, page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setBook(json));
                    callbackTotalPages(json.totalPages);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetBookComments: (bookId, page, callback, totalPagesCallback) => {
            return getBookComments(bookId, page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.content);
                    totalPagesCallback(json.totalPages);
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
                    dispatch(setToken(json.token));
                }
                else if (json.message.includes('JWT expired at')) {
                    dispatch(setToken(''));
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
        },

        onGoToComments: (state) => {
            dispatch(goToComments(state));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(BookReader);