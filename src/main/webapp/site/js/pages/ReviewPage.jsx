import React from 'react';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';
import Select from 'react-select';

import ReviewList from '../components/review/ReviewList.jsx';
import ReviewReaderForm from '../components/review/ReviewReaderForm.jsx';
import { getLocale } from '../locale.jsx';

import { createNotify } from '../actions/GlobalActions.jsx';
import { getReviews } from '../actions/ReviewActions.jsx';
import { getBooks } from '../actions/BookActions.jsx';

import {
    openReviewReaderForm
} from '../actions/ReviewActions.jsx';

class ReviewPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            book: {
                value: -1,
                label: 'ALL'
            },
            activePage: 1,
            totalPages: 1,
            reviews: []
        };
    }

    componentDidMount() {
        let bookId = this.props.match.params.bookId ? this.props.match.params.bookId : this.state.book.value;
        this.props.onGetReviews(bookId, this.state.activePage, page => this.updatePage(page));
    }

    loadBooks(value) {
        if (!value || value === '') {
            return Promise.resolve({
                options: []
            });
        }
        return this.props.onGetBooks(value, 1);
    }

    onBookChange(book) {
        this.setState({
            book: book
        });
        this.props.onGetReviews(book.value, 0, page => this.updatePage(page));
    }

    filterOption(option, filter) {
        if (option.label === 'ALL' && option.value === -1) {
            return true;
        }
        return option.label.toLowerCase().indexOf(filter.toLowerCase()) !== -1;
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        this.props.onGetReviews(this.state.book.value, page, page => this.updatePage(page));
    }

    updatePage(page) {
        this.setState({
            totalPages: page.totalPages,
            activePage: page.number + 1,
            reviews: page.content
        });
    }

    onSubmit(event) {
        event.preventDefault();
    }

    render() {
        return (
            <div>
                <div className="col-sm-12">
                    <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="book">{getLocale(this.props.language)['Book:']}</label>
                            <div className="col-sm-10">
                                <Select.Async value={this.state.book}
                                              id="book"
                                              loadOptions={value => this.loadBooks(value)}
                                              onChange={book => this.onBookChange(book)}
                                              filterOption={(option, filter) => this.filterOption(option, filter)}
                                              noResultsText={getLocale(this.props.language)['Nothing found']}
                                              loadingPlaceholder={getLocale(this.props.language)['Searching...']}
                                              placeholder={getLocale(this.props.language)['Select a book']}/>
                            </div>
                        </div>
                    </form>
                    <br/>
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
                    <br/>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <ReviewList reviews={this.state.reviews}
                                onOpenReview={review => this.props.onOpenReviewReaderForm(review)}/>
                </div>

                {/* popup form for reading book review */}
                <ReviewReaderForm/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        authors: state.AuthorReducer.authors,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetBooks: (name, page) => {
            getBooks(name, page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    let options = [{value: -1, label: 'ALL'}],
                        books = json.content;
                    books.forEach(book => {
                        options.push({
                            value: book.id,
                            label: book.name
                        });
                    });
                    return {options};
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetReviews: (bookId, page, callback) => {
            getReviews(bookId, page - 1).then(([response, json]) => {
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

        onOpenReviewReaderForm: (review) => {
            dispatch(openReviewReaderForm(review));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ReviewPage);