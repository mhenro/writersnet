import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';
import { getLocale } from '../locale.jsx';

class GiftPage extends React.Component {
    render() {
        return (
            <div>
                Gift page
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
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(GiftPage);