import React from 'react';
import { connect } from 'react-redux';
import {
    getAuthorDetails,
    setAuthor,
    checkFriendshipWith,
    subscribeOn
} from '../actions/AuthorActions.jsx';
import {
    getSeries,
    getBooksByAuthor,
    deleteBook,
    openBookPropsForm,
    openEditSeriesForm,
    openReviewForm
} from '../actions/BookActions.jsx';
import {
    createNotify,
    goToComments,
    setUserDetails
} from '../actions/GlobalActions.jsx';
import { setToken } from '../actions/AuthActions.jsx';
import { getHost } from '../utils.jsx';
import { getLocale } from '../locale.jsx';

import AuthorFile from '../components/section/AuthorFile.jsx';
import AuthorShortInfo from '../components/section/AuthorShortInfo.jsx';
import BookSerieList from '../components/section/BookSerieList.jsx';
import BookPropsForm from '../components/section/BookPropsForm.jsx';
import EditSeriesForm from '../components/section/EditSeriesForm.jsx';
import ReviewForm from '../components/section/ReviewForm.jsx';

/*
    props:
    - this.props.match.params.authorName - user id
* */
class SectionPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            friendship: null,
            series: [],
            books: []
        };
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        this.props.onGetAuthorDetails(this.props.match.params.authorName);
        this.setState({
            friendship: null,
            series: [],
            books: []
        });
        this.props.onGetSeries(this.props.match.params.authorName, series => this.updateSeries(series));
        this.props.onGetBooks(this.props.match.params.authorName, books => this.updateBooks(books));

        this.checkFriendship();
    }

    checkFriendship() {
        setTimeout(() => {
            if ((this.props.login !== 'Anonymous') && (this.props.token !== '')) {
                this.props.onCheckFriendshipWith(this.props.match.params.authorName, this.props.token, friendship => this.updateFriendshipRelation(friendship));
            } else {
                this.setState({
                    friendship: {
                        friend: false,
                        subscriber: false,
                        subscription: false
                    }
                });
            }
        }, 500);
    }

    updateFriendshipRelation(friendship) {
        this.setState({
            friendship: friendship
        });
    }

    updateSeries(series) {
        this.setState({
            series: series
        });
    }

    updateBooks(books) {
        this.setState({
            books: books
        });
    }

    onAddNewBook() {
        this.props.onOpenBookPropsForm();
    }

    onEditSeries() {
        this.props.onOpenEditSeriesForm();
    }

    onEditBook(book) {
        this.props.onOpenBookPropsForm(book);
    }

    onAddReview(book) {
        this.props.onOpenReviewForm(book);
    }

    onDeleteBook(bookId, token) {
        this.props.onDeleteBook(bookId, token, () => this.props.onGetBooks(this.props.match.params.authorName, books => this.updateBooks(books)));
    }

    onAddToFriends(friend) {
        this.props.onSubcribeOn(friend, this.props.token, () => this.props.onCheckFriendshipWith(this.props.match.params.authorName, this.props.token, friendship => this.updateFriendshipRelation(friendship)));
    }

    renderSectionToolbar() {
        if (this.props.registered && this.props.login === this.props.author.username) {
            return (
                <div
                    className="col-sm-12 panel panel-success">
                    <div className="panel-body btn-group">
                        <button className="btn btn-success" onClick={() => this.onAddNewBook()}>{getLocale(this.props.language)['Add new book']}</button>
                        <button className="btn btn-success" onClick={() => this.onEditSeries()}>{getLocale(this.props.language)['Edit series']}</button>
                    </div>
                </div>
            )
        } else {
            return null;
        }
    }

    renderCrown() {
        if (this.props.author.premium) {
            return <img src={getHost() + 'css/images/crown.png'} title="Premium author" width="32" height="auto"/>;
        }
    }

    isDataLoaded() {
        if (!this.props.author) {
            return false;
        }
        if (!this.state.friendship) {
            return false;
        }
        return true;
    }

    render() {
        if (!this.isDataLoaded()) {
            return null;
        }
        return (
            <div>
                <div className="row">
                    <div className="col-sm-11 text-center section-name">
                        {this.props.author.fullName}
                    </div>
                    <div className="col-sm-1 text-right">
                        {this.renderCrown()}
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-11 text-center section-author-name">
                        {this.props.author.section.name}
                    </div>
                    <div className="col-sm-1"></div>
                </div>
                <div className="row">
                    <div className="col-sm-12 col-lg-3">
                        <AuthorFile me={this.props.registered && this.props.login === this.props.author.username}
                                    author={this.props.author}
                                    registered={this.props.registered}
                                    login={this.props.login}
                                    onAddToFriends={friend => this.onAddToFriends(friend)}
                                    friendship={this.state.friendship}/>
                    </div>
                    <div className="col-sm-12 col-lg-9">
                        <AuthorShortInfo author={this.props.author} books={this.state.books}/>
                    </div>
                </div>
                <div className="col-sm-12 panel panel-success">
                    <div className="panel-body">
                        {this.props.author.section.description}
                    </div>
                </div>
                {this.renderSectionToolbar()}
                <hr/>
                <BookSerieList series={this.state.series}
                               books={this.state.books}
                               registered={this.props.registered}
                               login={this.props.login}
                               author={this.props.author}
                               onEditBook={book => this.onEditBook(book)}
                               onAddReview={book => this.onAddReview(book)}
                               onDeleteBook={(bookId, token) => this.onDeleteBook(bookId, token)}
                               token={this.props.token}
                               onGoToComments={this.props.onGoToComments}
                               language={this.props.language}/>

                {/* form for editing properties of the selected book */}
                <BookPropsForm onCloseUpdate={() => this.props.onGetBooks(this.props.match.params.authorName, books => this.updateBooks(books))}/>

                {/* Edit series form */}
                <EditSeriesForm onCloseUpdate={() => this.props.onGetSeries(this.props.match.params.authorName, series => this.updateSeries(series))}/>

                {/* Form for adding reviews */}
                <ReviewForm onCloseUpdate={() => this.props.onGetBooks(this.props.match.params.authorName, books => this.updateBooks(books))}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        author: state.AuthorReducer.author,
        registered: state.GlobalReducer.registered,
        token: state.GlobalReducer.token,
        login: state.GlobalReducer.user.login,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthorDetails: (userId) => {
            return getAuthorDetails(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setAuthor(json));
                    dispatch(setUserDetails(json));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onOpenBookPropsForm: (book) => {
            dispatch(openBookPropsForm(book));
        },

        onOpenEditSeriesForm: () => {
            dispatch(openEditSeriesForm());
        },

        onOpenReviewForm: (book) => {
            dispatch(openReviewForm(book));
        },

        onDeleteBook: (bookId, token, callback) => {
            return deleteBook(bookId, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Book was deleted successfully'));
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

        onGoToComments: (state) => {
            dispatch(goToComments(state));
        },

        onCheckFriendshipWith: (userId, token, callback) => {
            return checkFriendshipWith(userId, token).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.message);
                }
                else if (response.status === 500) {
                    if (json.message.includes('JWT expired at')) {
                        dispatch(setToken(''));
                    }
                    callback({
                        friend: false,
                        subscriber: false,
                        subscription: false
                    });
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSubcribeOn: (authorName, token, callback) => {
            return subscribeOn(authorName, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', json.message));
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

        onGetSeries: (userId, callback) => {
            return getSeries(userId).then(([response, json]) => {
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

        onGetBooks: (authorId, callback) => {
            return getBooksByAuthor(authorId).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.content);
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

export default connect(mapStateToProps, mapDispatchToProps)(SectionPage);