import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';
import { getLocale } from '../locale.jsx';
import { getAllGifts } from '../actions/GiftActions.jsx';
import {
    setPurchaseId,
    setOperationType,
    setGiftMessage,
    setGiftedUser,
    createNotify
} from '../actions/GlobalActions.jsx';
import { OperationType } from '../utils.jsx';
import { showConfirmPaymentForm } from '../actions/BalanceActions.jsx';

import GiftList from '../components/gifts/GiftList.jsx';

class GiftPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            gifts: [],
            giftedUser: null
        };
    }

    componentDidMount() {
        this.props.onGetAllGifts(gifts => this.updateGifts(gifts));
        this.setState({
            giftedUser: this.props.giftedUser
        });
    }

    updateGifts(gifts) {
        this.setState({
            gifts: gifts
        });
    }

    onBuyGift(giftId, message) {
        this.props.onSetPurchaseId(giftId);
        this.props.onSetOperationType(OperationType.GIFT);
        this.props.onSetGiftedUser(this.state.giftedUser);
        this.props.onSetGiftMessage(message);

        this.props.onShowPaymentForm();
    }

    render() {
        return (
            <div>
                <GiftList gifts={this.state.gifts} onBuyGift={(giftId, msg) => this.onBuyGift(giftId, msg)}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        books: state.BookReducer.books,
        language: state.GlobalReducer.language,
        giftedUser: state.GlobalReducer.giftedUser
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

        onSetPurchaseId: (id) => {
            dispatch(setPurchaseId(id));
        },

        onSetOperationType: (type) => {
            dispatch(setOperationType(type));
        },

        onSetGiftedUser: (userId) => {
            dispatch(setGiftedUser(userId));
        },

        onSetGiftMessage: (message) => {
            dispatch(setGiftMessage(message));
        },

        onShowPaymentForm: () => {
            dispatch(showConfirmPaymentForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(GiftPage);