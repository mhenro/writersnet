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
    getBooks
} from '../actions/BookActions.jsx';

class BookPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentPage: 1,
            currentName: null,
            searchPattern: '',
            totalPages: 1
        };
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        this.updateBooks(null, 1);
    }

    updateBooks(name, page) {
        this.props.onGetBooks(name, page - 1, pageDetails => this.updatePaginator(pageDetails));
    }

    onLetterClick(letter) {
        this.setState({
            currentName: letter,
            currentPage: 1
        });
        this.updateBooks(letter, 1);
    }

    onSearchChange(event) {
        this.setState({
            searchPattern: event.target.value,
            currentPage: 1
        });
    }

    onKeyDown(key) {
        if (key.key === 'Enter') {
            this.updateBooks(this.state.searchPattern, this.state.currentPage);
        }
    }

    pageSelect(page) {
        this.setState({
            currentPage: page
        });
        this.updateBooks(this.state.currentName, page);
    }

    updatePaginator(pageDetails) {
        this.setState({
            totalPages: pageDetails.totalPages
        });
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
                    <BookFilter/>
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
        onGetBooks: (name, page, callback) => {
            return getBooks(name, page).then(([response, json]) => {
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
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(BookPage);