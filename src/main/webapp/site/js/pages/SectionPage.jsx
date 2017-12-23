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
    getBooks,
    deleteBook,
    openBookPropsForm,
    openEditSeriesForm
} from '../actions/BookActions.jsx';
import {
    createNotify,
    goToComments
} from '../actions/GlobalActions.jsx';
import { setToken } from '../actions/AuthActions.jsx';

import AuthorFile from '../components/section/AuthorFile.jsx';
import AuthorShortInfo from '../components/section/AuthorShortInfo.jsx';
import BookSerieList from '../components/section/BookSerieList.jsx';
import BookPropsForm from '../components/section/BookPropsForm.jsx';
import EditSeriesForm from '../components/section/EditSeriesForm.jsx';

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
        this.props.onGetBooks(this.props.match.params.authorName, 0, books => this.updateBooks(books));

        this.checkFriendship();
    }

    checkFriendship() {
        setTimeout(() => {
            if ((this.props.login !== 'Anonymous') && (this.props.token !== '')) {
                this.props.onCheckFriendshipWith(this.props.login, this.props.token, friendship => this.updateFriendshipRelation(friendship));
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

    onDeleteBook(bookId, token) {
        this.props.onDeleteBook(bookId, token, () => this.props.onGetAuthorDetails(this.props.match.params.authorName));
    }

    onAddToFriends(user, friend) {
        this.props.onSubcribeOn(friend, this.props.token, () => this.props.onGetAuthorDetails(this.props.match.params.authorName));
    }

    renderSectionToolbar() {
        if (this.props.registered && this.props.login === this.props.author.username) {
            return (
                <div
                    className="col-sm-12 panel panel-success">
                    <div className="panel-body btn-group">
                        <button className="btn btn-success" onClick={() => this.onAddNewBook()}>Add new book</button>
                        <button className="btn btn-success" onClick={() => this.onEditSeries()}>Edit series</button>
                    </div>
                </div>
            )
        } else {
            return null;
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
                    <div className="col-sm-12 section-name">
                        {this.props.author.firstName + ' ' + this.props.author.lastName}
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-12 section-author-name">
                        {this.props.author.section.name}
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-12 col-lg-3">
                        <AuthorFile me={this.props.registered && this.props.login === this.props.author.username}
                                    author={this.props.author}
                                    registered={this.props.registered}
                                    login={this.props.login}
                                    onAddToFriends={(user, friend) => this.onAddToFriends(user, friend)}
                                    friendship={this.state.friendship}/>
                    </div>
                    <div className="col-sm-12 col-lg-9">
                        <AuthorShortInfo author={this.props.author}/>
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
                               onDeleteBook={(bookId, token) => this.onDeleteBook(bookId, token)}
                               token={this.props.token}
                               onGoToComments={this.props.onGoToComments}
                               language={this.props.language}/>

                {/* form for editing properties of the selected book */}
                <BookPropsForm/>

                {/* Edit series form */}
                <EditSeriesForm onCloseUpdate={() => this.props.onGetSeries(this.props.match.params.authorName, series => this.updateSeries(series))}/>
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

        onGetBooks: (name, page, callback) => {
            return getBooks(name, page, 100).then(([response, json]) => {
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