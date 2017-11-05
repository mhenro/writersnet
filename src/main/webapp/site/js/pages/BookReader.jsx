import React from 'react';
import { connect } from 'react-redux';

import {
    getBookDetails,
    setBook,
    addStar
} from '../actions/BookActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

import ReactStars from 'react-stars';

/*
    props:
    - book
 */
class BookReader extends React.Component {
    componentDidMount() {
        this.props.onGetBookDetails(this.props.match.params.bookId);

        ['updateReader', 'newVote'].map(fn => this[fn] = this[fn].bind(this));
    }

    getAverageRating() {
        return this.props.book.totalRating.averageRating.toFixed(2);
    }

    getTotalVotes() {
        return this.props.book.totalRating.userCount;
    }

    newVote(estimation) {
        this.props.onAddStar(this.props.book.id, estimation, this.updateReader);
    }

    isRatingAllowed() {
        return true;
    }

    updateReader() {
        this.props.onGetBookDetails(this.props.match.params.bookId);
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
                <div className="col-sm-12 section-name" style={{textAlign: 'center'}}>
                    {this.props.book.author.firstName + ' ' + this.props.book.author.lastName}
                </div>
                <div className="col-sm-12 section-author-name" style={{textAlign: 'center'}}>
                    {this.props.book.name}
                </div>
                <div className="col-sm-12">
                    <br/>
                    <hr/>
                    <br/>
                    <div className="col-sm-6">
                        <ReactStars count={5} size={18} color2={'orange'} half={false} value={this.getAverageRating()} onChange={this.newVote}/>
                        &nbsp;
                        <span><b>{this.getAverageRating() + ' * ' + this.getTotalVotes()}</b></span>
                        <br/>
                        <br/>
                        <br/>
                    </div>
                    <div className="col-sm-6"></div>
                </div>
                <div className="col-sm-12">
                    <div dangerouslySetInnerHTML={{ __html: this.props.book.bookText.text }} />
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        book: state.BookReducer.book
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetBookDetails: (bookId) => {
            return getBookDetails(bookId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setBook(json));
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
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(BookReader);