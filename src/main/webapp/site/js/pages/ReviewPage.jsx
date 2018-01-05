import React from 'react';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';
import Select from 'react-select';

import ReviewList from '../components/review/ReviewList.jsx';

import { createNotify } from '../actions/GlobalActions.jsx';
import { setToken } from '../actions/AuthActions.jsx';
import { getReviews } from '../actions/ReviewActions.jsx';

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
        this.props.onGetReviews(this.state.book.value, this.state.activePage, page => this.updatePage(page));
    }

    loadBooks(value) {
        if (!value || value === '') {
            return Promise.resolve({
                options: []
            });
        }
        return this.props.onGetFriends(this.props.login, value, this.props.token, 1);
    }

    onBookChange(book) {
        this.setState({
            book: book
        });
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
                            <label className="control-label col-sm-2" htmlFor="book">Book:</label>
                            <div className="col-sm-10">
                                <Select.Async value={this.state.book}
                                              id="book"
                                              loadOptions={value => this.loadBooks(value)}
                                              onChange={book => this.onBookChange(book)}
                                              noResultsText="Nothing found"
                                              loadingPlaceholder="Searching..."
                                              placeholder="Select a book"/>
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
                    <ReviewList reviews={this.state.reviews}/>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        authors: state.AuthorReducer.authors
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetFriends: (userId, matcher, token, page/*, callback*/) => {
            return getFriends(userId, matcher, token, page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    let options = [],
                        friends = json.content;
                    friends.forEach(friend => {
                        options.push({
                            value: friend.friendId,
                            label: friend.fullName
                        });
                    });
                    return {options};
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

        onGetReviews: (bookId, page, callback) => {
            return getReviews(bookId, page - 1).then(([response, json]) => {
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

export default connect(mapStateToProps, mapDispatchToProps)(ReviewPage);