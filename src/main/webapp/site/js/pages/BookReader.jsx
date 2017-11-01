import React from 'react';
import { connect } from 'react-redux';

import {
    getBookDetails,
    setBook
} from '../actions/BookActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

class BookReader extends React.Component {
    componentDidMount() {
        this.props.onGetBookDetails(this.props.match.params.bookId, this.updateReader);

        ['updateReader'].map(fn => this[fn] = this[fn].bind(this));
    }

    updateReader() {

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
        onGetBookDetails: (bookId, callback) => {
            return getBookDetails(bookId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setBook(json));
                    callback();
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

export default connect(mapStateToProps, mapDispatchToProps)(BookReader);