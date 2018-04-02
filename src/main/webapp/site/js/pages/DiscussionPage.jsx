import React from 'react';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { formatDate } from '../utils.jsx';
import { getLocale } from '../locale.jsx';

import {
    createNotify
} from '../actions/GlobalActions.jsx';

import { getAllComments } from '../actions/BookActions.jsx';

/*
    props:
    -
 */
class DiscussionPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activePage: 1,
            totalPages: 1,
            comments: []
        };
    }

    componentDidMount() {
        this.props.onGetAllComments(this.state.activePage, page => this.updatePage(page));
    }

    updatePage(page) {
        this.setState({
            activePage: page.number + 1,
            totalPages: page.totalPages,
            comments: page.content
        });
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });
        this.props.onGetAllComments(page, content => this.updatePage(content));
    }

    setTotalPages(totalPages) {
        this.setState({
            totalPages: totalPages
        });
    }

    renderComments() {
        let getBookName = (bookId, bookName, bookCover) => {
            let name;
            if (bookName.length > 18) {
                name = bookName.substr(0, 18) + '...';
            } else {
                name = bookName;
            }
            return (
                <div>
                    <div className="discussion-short-avatar">
                        <img src={bookCover} className="img-rounded" width="32" height="auto"/>
                    </div>&nbsp;
                    <div className="discussion-text-float">
                        <Link to={'/reader/' + bookId}>{name}</Link>
                    </div>
                    <div className="stars-end"></div>
                </div>
            )
        };
        let getAuthorName = (authorId, authorName, authorAvatar) => {
            let name;
            if (authorName.length > 18) {
                name = authorName.substr(0, 18) + '...';
            } else {
                name = authorName;
            }
            return (
                <div>
                    <div className="discussion-short-avatar">
                        <img src={authorAvatar} className="img-rounded" width="32" height="auto"/>
                    </div>&nbsp;
                    <div className="discussion-text-float">
                        <Link to={'/authors/' + authorId}>{name}</Link>
                    </div>
                    <div className="stars-end"></div>
                </div>
            )
        };
        let getUserName = (authorName) => {
            let name;
            if (authorName.length > 18) {
                name = authorName.substr(0, 18) + '...';
            } else {
                name = authorName;
            }
            return <div className="discussion-text">{name}</div>;
        };
        let getCommentText = (commentText) => {
            let text;
            if (commentText.length > 30) {
                text = commentText.substr(0, 30) + '...';
            } else {
                text = commentText;
            }
            return <div className="discussion-text">{text}</div>
        };
        let getCommentDate = (created) => {
            let date = new Date(created);
            return <div className="discussion-text">{formatDate(date, 'h:m Y-M-D')}</div>
        };

        return (
            <tbody>
                {this.state.comments.map((comment, key) => {
                    return <tr key={key}>
                        <td>{getBookName(comment.bookId, comment.bookName, comment.bookCover)}</td>
                        <td>{getAuthorName(comment.authorId, comment.authorFullName, comment.authorAvatar)}</td>
                        <td>{getUserName(comment.userFullName)}</td>
                        <td>{getCommentText(comment.comment)}</td>
                        <td>{getCommentDate(comment.created)}</td>
                    </tr>
                })}
            </tbody>
        )
    }

    render() {
        return (
            <div>
                <div className="col-sm-12 text-center">
                <Pagination
                    className={'shown'}
                    prev
                    next
                    first
                    last
                    ellipsis
                    boundaryLinks
                    items={this.state.totalPages}
                    maxButtons={3}
                    activePage={this.state.activePage}
                    onSelect={page => this.pageSelect(page)}/>
                </div>
                <div className="col-sm-12">
                    <table className="table table-hover">
                        <thead>
                            <tr>
                                <td>{getLocale(this.props.language)['Novel']}</td>
                                <td>{getLocale(this.props.language)['Author']}</td>
                                <td>{getLocale(this.props.language)['Comment author']}</td>
                                <td>{getLocale(this.props.language)['Comment']}</td>
                                <td>{getLocale(this.props.language)['Last comment date']}</td>
                            </tr>
                        </thead>
                        {this.renderComments()}
                    </table>
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
                        items={this.state.totalPages}
                        maxButtons={3}
                        activePage={this.state.activePage}
                        onSelect={page => this.pageSelect(page)}/>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAllComments: (page, callback) => {
            getAllComments(page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
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

export default connect(mapStateToProps, mapDispatchToProps)(DiscussionPage);