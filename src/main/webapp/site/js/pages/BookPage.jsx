import React from 'react';
import { connect } from 'react-redux';
import BookBriefList from '../components/books/BookBriefList.jsx';
import { Pagination } from 'react-bootstrap';

import AlphabetPagination from '../components/AlphabetPagination.jsx';
import BookFilter from '../components/books/BookFilter.jsx';

import {
    createNotify
} from '../actions/GlobalActions.jsx';
import {
    setBooks,
    getBooks,
    getGenres
} from '../actions/BookActions.jsx';

class BookPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentPage: 1,
            currentName: null,
            searchPattern: '',
            totalPages: 1,
            genres: [],
            filterGenre: {value: null, label: 'ALL'},
            filterLanguage: {value: null, label: 'ALL'},
            size: 5,
            sortBy: 'name'
        };
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        this.updateBooks(null, null, null, 1);
        this.props.onGetGenres(genres => this.updateGenres(genres));
    }

    updateBooks(name, genre, language, page, size, sort) {
        this.props.onGetBooks(name, genre, language, page - 1, size, sort, pageDetails => this.updatePaginator(pageDetails));
    }

    onLetterClick(letter) {
        this.setState({
            currentName: letter,
            currentPage: 1
        });
        this.updateBooks(letter, this.state.filterGenre.value, this.state.filterLanguage.value, 1, this.state.size, this.state.sortBy);
    }

    onSearchChange(event) {
        this.setState({
            searchPattern: event.target.value,
            currentPage: 1
        });
    }

    onKeyDown(key) {
        if (key.key === 'Enter') {
            this.updateBooks(this.state.searchPattern, this.state.filterGenre.value, this.state.filterLanguage.value, this.state.currentPage, this.state.size, this.state.sortBy);
        }
    }

    pageSelect(page) {
        this.setState({
            currentPage: page
        });
        this.updateBooks(this.state.currentName, this.state.filterGenre.value, this.state.filterLanguage.value, page, this.state.size, this.state.sortBy);
    }

    updatePaginator(pageDetails) {
        this.setState({
            totalPages: pageDetails.totalPages
        });
    }

    updateGenres(genres) {
        this.setState({
            genres: genres
        });
    }

    onGenreChange(genre) {
        this.setState({
            filterGenre: genre
        });
        this.updateBooks(this.state.searchPattern, genre.value, this.state.filterLanguage.value, this.state.currentPage, this.state.size, this.state.sortBy);
    }

    onLanguageChange(language) {
        this.setState({
            filterLanguage: language
        });
        this.updateBooks(this.state.searchPattern, this.state.filterGenre.value, language.value, this.state.currentPage, this.state.size, this.state.sortBy);
    }

    selectSortType(sortType) {
        this.setState({
            sortBy: sortType
        });
        this.updateBooks(this.state.searchPattern, this.state.filterGenre.value, this.state.filterLanguage.value, this.state.currentPage, this.state.size, sortType);
    }

    getSortClass(sortType) {
        if (this.state.sortBy === sortType) {
            return 'btn active btn-xs btn-default';
        }
        return 'btn btn-xs btn-default';
    }

    render() {
        return (
            <div>
                <div className="col-sm-12">
                    <AlphabetPagination onClick={letter => this.onLetterClick(letter)}/>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <div className="input-group">
                        <input value={this.state.searchPattern} onChange={event => this.onSearchChange(event)} onKeyDown={key => this.onKeyDown(key)} type="text" className="form-control" placeholder="Input book name" />
                        <div className="input-group-btn">
                            <button className="btn btn-default" type="submit">
                                <i className="glyphicon glyphicon-search"></i>
                            </button>
                        </div>
                    </div>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <BookFilter genres={this.state.genres}
                                language={this.props.language}
                                onGenreChange={genre => this.onGenreChange(genre)}
                                onLanguageChange={language => this.onLanguageChange(language)}/>
                </div>
                <div className="col-sm-12">
                    Sort by:&nbsp;
                    <div className="btn-group">
                        <button onClick={() => this.selectSortType('name')} className={this.getSortClass('name')}>Name</button>
                        <button onClick={() => this.selectSortType('totalRating')} className={this.getSortClass('totalRating')}>Rating</button>
                        <button onClick={() => this.selectSortType('lastUpdate')} className={this.getSortClass('lastUpdate')}>Last update</button>
                    </div>
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
                        activePage={this.state.currentPage}
                        onSelect={page => this.pageSelect(page)}/>
                    <br/>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <BookBriefList books={this.props.books} language={this.props.language}/>
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
                        activePage={this.state.currentPage}
                        onSelect={page => this.pageSelect(page)}/>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        books: state.BookReducer.books,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetBooks: (name, genre, language, page, size, sort, callback) => {
            return getBooks(name, genre, language, page, size, sort).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setBooks(json.content));
                    callback(json);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetGenres: (callback) => {
            return getGenres().then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(BookPage);