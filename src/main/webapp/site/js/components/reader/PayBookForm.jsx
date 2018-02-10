import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button, Tooltip, OverlayTrigger } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { getLocale } from '../../locale.jsx';
import { OperationType } from '../../utils.jsx';
import { setOperationType, setPurchaseId, createNotify } from '../../actions/GlobalActions.jsx';
import { closePayBookForm } from '../../actions/BookActions.jsx';
import { showConfirmPaymentForm } from '../../actions/BalanceActions.jsx';

class PayBookForm extends React.Component {
    constructor(props) {
        super(props);
        //this.state = {
        //};
    }

    onShow() {

    }

    onBuy() {
        this.props.onSetPurchase(0);    //0 is always PREMIUM_ACCOUNT
        this.props.onSetOperationType(OperationType.BOOK);
        this.props.onShowPaymentForm();
    }

    onClose() {
        this.props.onClose();
    }

    render() {
        return (
            <Modal show={this.props.showPayBookForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    Do you want to buy this book?
                </Modal.Header>
                <Modal.Body>
                    This is a paid book, but you didn't buy it yet.<br/>
                    If you want to read it you have to pay for it to the author of this book.<br/>
                    Once you buy this book you will have unlimited access to it.
                </Modal.Body>
                <Modal.Footer>
                    <div className="btn-group">
                        <Button onClick={() => this.onBuy()}
                                className="btn btn-success">{getLocale(this.props.language)['Buy']}</Button>
                        <Button onClick={() => this.onClose()}
                                className="btn btn-default">{getLocale(this.props.language)['Close']}</Button>
                    </div>
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        token: state.GlobalReducer.token,
        login: state.GlobalReducer.user.login,
        language: state.GlobalReducer.language,
        showPayBookForm: state.BookReducer.showPayBookForm
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onBuy: (buyRequest, token, callback) => {
            return buy(buyRequest, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'The operation was completed successfully'));
                    callback();
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onShowPaymentForm: () => {
            dispatch(showConfirmPaymentForm());
        },

        onSetPurchase: (purchaseId) => {
            dispatch(setPurchaseId(purchaseId));
        },

        onSetOperationType: (operationType) => {
            dispatch(setOperationType(operationType));
        },

        onClose: () => {
            dispatch(closePayBookForm());
            window.history.back();
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PayBookForm);