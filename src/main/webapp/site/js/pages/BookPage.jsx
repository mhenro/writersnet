import React from 'react';
import { connect } from 'react-redux';
import BookBriefList from '../components/books/BookBriefList.jsx';
import { Pagination } from 'react-bootstrap';

import AlphabetPagination from '../components/AlphabetPagination.jsx';

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
            totalPages: 1
        };
        ['handleSelect'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        this.updateBooks();
    }

    updateBooks() {
        this.props.onGetBooks(this.state.currentPage - 1);
    }

    handleSelect(eventKey) {
        this.setState({
            currentPage: eventKey
        });
        this.updateBooks();
    }

    render() {
        return (
            <div>
                <div className="col-sm-12">
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
                        onSelect={this.handleSelect}/>
                </div>
                <div className="col-sm-12">
                    <AlphabetPagination/>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <BookBriefList books={this.props.books} language={this.props.language}/>
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
        onGetBooks: (page) => {
            return getBooks(page).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setBooks(json.content));
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