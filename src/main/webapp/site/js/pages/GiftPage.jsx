import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';
import { getLocale } from '../locale.jsx';
import { getAllGifts } from '../actions/GiftActions.jsx';

import GiftList from '../components/gifts/GiftList.jsx';

class GiftPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            gifts: []
        };
    }

    componentDidMount() {
        this.props.onGetAllGifts(gifts => this.updateGifts(gifts));
    }

    updateGifts(gifts) {
        this.setState({
            gifts: gifts
        });
    }

    render() {
        return (
            <div>
                Gift page
                <GiftList gifts={this.state.gifts}/>
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
        onGetAllGifts: (callback) => {
            return getAllGifts().then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.message);
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

export default connect(mapStateToProps, mapDispatchToProps)(GiftPage);