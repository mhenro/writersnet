import React from 'react';
import { connect } from 'react-redux';

import { Pagination } from 'react-bootstrap';
import TopSelector from '../components/rating/TopSelector.jsx';
import BookTopTable from '../components/rating/BookTopTable.jsx';
import AuthorTopTable from '../components/rating/AuthorTopTable.jsx';
import { getLocale } from '../locale.jsx';

import {
    createNotify
} from '../actions/GlobalActions.jsx';
import {
    getTopAuthorsByRating,
    getTopAuthorsByBookCount,
    getTopAuthorsByComments,
    getTopAuthorsByViews,

    getTopBooksByNovelty,
    getTopBooksByRating,
    getTopBooksByVolume,
    getTopBooksByComments,
    getTopBooksByViews
} from '../actions/RatingActions.jsx';

class RatingPage extends React.Component {
    constructor(props) {
        super(props);
        ['onTopAuthorClick', 'onTopBookClick', 'updateTopAuthors', 'updateTopBooks', 'topAuthorSelect', 'topBookSelect'].map(fn => this[fn] = this[fn].bind(this));

        this.state = {
            topAuthors: {content: []},
            topBooks: {content: []},
            activeAuthorPage: 1,
            totalAuthorPages: 1,
            activeBookPage: 1,
            totalBookPages: 1,
            authorTop: '',
            bookTop: ''
        };
    }

    componentDidMount() {
        this.onTopAuthorClick('authorsTopByRating');
        this.onTopBookClick('booksTopByRating');
    }

    topAuthorSelect(eventKey) {
        this.setState({
            activeAuthorPage: eventKey
        });

        switch(this.state.authorTop) {
            case 'authorsTopByRating':
                this.props.onGetTopAuthorsByRating(eventKey, this.updateTopAuthors);
                break;
            case 'authorsTopByNovelsCount':
                this.props.onGetTopAuthorsByBookCount(eventKey, this.updateTopAuthors);
                break;
            case 'authorsTopByCommentsCount':
                this.props.onGetTopAuthorsByComments(eventKey, this.updateTopAuthors);
                break;
            case 'authorsTopByViewsCount':
                this.props.onGetTopAuthorsByViews(eventKey, this.updateTopAuthors);
                break;
        }
    }

    topBookSelect(eventKey) {
        this.setState({
            activeBookPage: eventKey
        });

        switch(this.state.bookTop) {
            case 'booksTopByNovelty':
                this.props.onGetTopBooksByNovelty(eventKey, this.updateTopBooks);
                break;
            case 'booksTopByRating':
                this.props.onGetTopBooksByRating(eventKey, this.updateTopBooks);
                break;
            case 'booksTopByNovelVolume':
                this.props.onGetTopBooksByVolume(eventKey, this.updateTopBooks);
                break;
            case 'booksTopByCommentsCount':
                this.props.onGetTopBooksByComments(eventKey, this.updateTopBooks);
                break;
            case 'booksTopByViewsCount':
                this.props.onGetTopBooksByViews(eventKey, this.updateTopBooks);
                break;
        }
    }

    updateTopAuthors(topAuthors, totalPages) {
        this.setState({
            topAuthors: topAuthors,
            totalAuthorPages: totalPages
        });
    }

    updateTopBooks(topBooks, totalPages) {
        this.setState({
            topBooks: topBooks,
            totalBookPages: totalPages
        });
    }

    onTopAuthorClick(topName) {
        this.setState({
            authorTop: topName
        });
        switch(topName) {
            case 'authorsTopByRating':
                this.props.onGetTopAuthorsByRating(this.state.activeAuthorPage, this.updateTopAuthors);
                break;
            case 'authorsTopByNovelsCount':
                this.props.onGetTopAuthorsByBookCount(this.state.activeAuthorPage, this.updateTopAuthors);
                break;
            case 'authorsTopByCommentsCount':
                this.props.onGetTopAuthorsByComments(this.state.activeAuthorPage, this.updateTopAuthors);
                break;
            case 'authorsTopByViewsCount':
                this.props.onGetTopAuthorsByViews(this.state.activeAuthorPage, this.updateTopAuthors);
                break;
        }
    }

    onTopBookClick(topName) {
        this.setState({
            bookTop: topName
        });
        switch(topName) {
            case 'booksTopByNovelty':
                this.props.onGetTopBooksByNovelty(this.state.activeBookPage, this.updateTopBooks);
                break;
            case 'booksTopByRating':
                this.props.onGetTopBooksByRating(this.state.activeBookPage, this.updateTopBooks);
                break;
            case 'booksTopByNovelVolume':
                this.props.onGetTopBooksByVolume(this.state.activeBookPage, this.updateTopBooks);
                break;
            case 'booksTopByCommentsCount':
                this.props.onGetTopBooksByComments(this.state.activeBookPage, this.updateTopBooks);
                break;
            case 'booksTopByViewsCount':
                this.props.onGetTopBooksByViews(this.state.activeBookPage, this.updateTopBooks);
                break;
        }
    }

    render() {
        return (
            <div>
                <h4>{getLocale(this.props.language)['Author tops']}</h4>
                <Pagination
                    className={'shown'}
                    prev
                    next
                    first
                    last
                    ellipsis
                    boundaryLinks
                    items={this.state.totalAuthorPages}
                    maxButtons={3}
                    activePage={this.state.activeAuthorPage}
                    onSelect={this.topAuthorSelect}/>
                <div className="col-sm-12">
                    <TopSelector author={true} onTopClick={this.onTopAuthorClick} language={this.props.language}/>
                </div>
                <div className="col-sm-12">
                    <br/>
                </div>
                <div className="col-sm-12">
                    <AuthorTopTable authors={this.state.topAuthors} top={this.state.authorTop} language={this.props.language}/>
                </div>

                <h4>{getLocale(this.props.language)['Book tops']}</h4>
                <Pagination
                    className={'shown'}
                    prev
                    next
                    first
                    last
                    ellipsis
                    boundaryLinks
                    items={this.state.totalBookPages}
                    maxButtons={3}
                    activePage={this.state.activeBookPage}
                    onSelect={this.topBookSelect}/>
                <div className="col-sm-12">
                    <TopSelector book={true} onTopClick={this.onTopBookClick} language={this.props.language}/>
                </div>
                <div className="col-sm-12">
                    <br/>
                </div>
                <div className="col-sm-12">
                    <BookTopTable books={this.state.topBooks} top={this.state.bookTop} language={this.props.language}/>
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
    let processHelper = (response, json, callback) => {
        if (response.status === 200) {
            callback(json, json.totalPages);
        }
        else {
            dispatch(createNotify('danger', 'Error', json.message));
        }
    };
    let errorHelper = (error) => {
        dispatch(createNotify('danger', 'Error', error.message));
    };

    return {
        /* author tops */
        onGetTopAuthorsByRating: (page, callback) => {
            return getTopAuthorsByRating(page - 1).then(([response, json]) => {
                processHelper(response, json, callback);
            }).catch(errorHelper);
        },

        onGetTopAuthorsByBookCount: (page, callback) => {
            return getTopAuthorsByBookCount(page - 1).then(([response, json]) => {
                processHelper(response, json, callback);
            }).catch(errorHelper);
        },

        onGetTopAuthorsByComments: (page, callback) => {
            return getTopAuthorsByComments(page - 1).then(([response, json]) => {
                processHelper(response, json, callback);
            }).catch(errorHelper);
        },

        onGetTopAuthorsByViews: (page, callback) => {
            return getTopAuthorsByViews(page - 1).then(([response, json]) => {
                processHelper(response, json, callback);
            }).catch(errorHelper);
        },

        /* book tops */
        onGetTopBooksByNovelty: (page, callback) => {
            return getTopBooksByNovelty(page - 1).then(([response, json]) => {
                processHelper(response, json, callback);
            }).catch(errorHelper);
        },

        onGetTopBooksByRating: (page, callback) => {
            return getTopBooksByRating(page - 1).then(([response, json]) => {
                processHelper(response, json, callback);
            }).catch(errorHelper);
        },

        onGetTopBooksByVolume: (page, callback) => {
            return getTopBooksByVolume(page - 1).then(([response, json]) => {
                processHelper(response, json, callback);
            }).catch(errorHelper);
        },

        onGetTopBooksByComments: (page, callback) => {
            return getTopBooksByComments(page - 1).then(([response, json]) => {
                processHelper(response, json, callback);
            }).catch(errorHelper);
        },

        onGetTopBooksByViews: (page, callback) => {
            return getTopBooksByViews(page - 1).then(([response, json]) => {
                processHelper(response, json, callback);
            }).catch(errorHelper);
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(RatingPage);