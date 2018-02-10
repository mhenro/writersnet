import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button, Tooltip, OverlayTrigger } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { getLocale } from '../../locale.jsx';

import { getUserBalance, setUserBalance, closeConfirmPaymentForm, buy } from '../../actions/BalanceActions.jsx';
import { getGiftDetails } from '../../actions/GiftActions.jsx';
import {
    createNotify,
    setPurchaseId,
    setUserDetails
} from '../../actions/GlobalActions.jsx';
import {
    getAuthorDetails,
    setAuthor
} from '../../actions/AuthorActions.jsx';
import { getBookCost } from '../../actions/BookActions.jsx';
import { OperationType } from '../../utils.jsx';
/*
    props:
    - operationType - enum from utils.jsx
 */
class ConfirmPaymentForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            itemCost: 0,
            itemName: 'Item',
            itemDesc: '',
            balanceLoaded: false,
            purchaseLoaded: false
        };
    }

    onShow() {
        this.props.onGetUserBalance(this.props.token, () => this.balanceIsLoaded());
        if (this.props.operationType === OperationType.BOOK) {
            this.props.onGetBookCost(this.props.purchaseId, (item) => this.updateItem(item));
        } else {
            this.props.onGetGiftDetails(this.props.purchaseId, (item) => this.updateItem(item));
        }
    }

    balanceIsLoaded() {
        this.setState({
            balanceLoaded: true
        });
    }

    updateItem(item) {
        this.setState({
            itemCost: item.cost,
            itemName: item.name,
            itemDesc: item.description,
            purchaseLoaded: true
        });
    }

    onBuy() {
        let buyRequest = {
            operationType: this.props.operationType,
            purchaseId: this.props.purchaseId,
            sourceUserId: this.props.login,
            destUserId: '',
            sendMessage: ''
        };
        this.props.onBuy(buyRequest, this.props.token, () => this.onClose());
    }

    onClose() {
        this.setState({
            itemCost: 0,
            itemName: 'Item',
            itemDesc: '',
            balanceLoaded: false,
            purchaseLoaded: false
        });
        this.props.forgetPurchaseId();
        this.props.onClose();
        this.props.onGetAuthorDetails(this.props.login);
        this.props.onGetUserDetails(this.props.login);
    }

    getItemName() {
        let tooltip = (
            <Tooltip id="tooltip">{this.state.itemDesc}</Tooltip>
        );
        return (
            <OverlayTrigger placement="top" overlay={tooltip}>
                <span className="purchase-name">"{this.state.itemName}"</span>
            </OverlayTrigger>
        )
    }

    getItemCost() {
        let style = {color: 'red', fontWeight: 'bold'},
            cost = this.state.itemCost ? parseFloat(this.state.itemCost / 100).toFixed(2) : parseFloat(0).toFixed(2);
        return (
            <span style={style}>{cost}</span>
        )
    }

    getBalance() {
        let balance = this.props.balance ? this.props.balance : 0,
            style = {color: 'green', fontWeight: 'bold'};
        return (
            <span style={style}>{parseFloat(balance / 100).toFixed(2)}</span>
        )
    }

    getReducedBalance() {
        let style = {color: 'green', fontWeight: 'bold'},
            balance = this.props.balance ? this.props.balance : 0,
            itemCost = this.state.itemCost ? this.state.itemCost : 0,
            cost = parseFloat((balance - itemCost) / 100).toFixed(2);
        return (
            <span style={style}>{cost}</span>
        )
    }

    isBalanceEnough() {
        return this.props.balance >= this.state.itemCost;
    }

    renderFooterButtons() {
        if (!this.isDataLoaded()) {
            return null;
        }
        if (this.isBalanceEnough()) {
            return (
                <div className="btn-group">
                    <Button onClick={() => this.onBuy()}
                            className="btn btn-success">{getLocale(this.props.language)['Yes']}</Button>
                    <Button onClick={() => this.onClose()}
                            className="btn btn-default">{getLocale(this.props.language)['No']}</Button>
                </div>
            )
        } else {
            return (
                <Button onClick={() => this.onClose()}
                        className="btn btn-success">{getLocale(this.props.language)['Close']}</Button>
            )
        }
    }

    renderBody() {
        if (!this.isDataLoaded()) {
            return null;
        }
        if (this.isBalanceEnough()) {
            return (
                <Modal.Body>
                    You're going to buy {this.getItemName()} which costs {this.getItemCost()} credits. <br/>
                    Your current balance is {this.getBalance()} credits.<br/>
                    After the payment your balance will be reduced to {this.getReducedBalance()} credits<br/><br/>
                    <p>Do you want to buy this item?</p>
                </Modal.Body>
            )
        } else {
            return (
                <Modal.Body>
                    You're going to buy {this.getItemName()} which costs {this.getItemCost()} credits. <br/>
                    Your current balance is {this.getBalance()} credits.<br/>
                    You haven't enough credits to buy this item.<br/>
                    For top up the balance go to your <Link onClick={() => this.onClose()} to="/balance">balance page</Link>
                </Modal.Body>
            )
        }
    }

    isDataLoaded() {
        return this.state.balanceLoaded && this.state.purchaseLoaded;
    }

    render() {
        return (
            <Modal show={this.props.showConfirmPaymentForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    Confirm the payment
                </Modal.Header>
                {this.renderBody()}
                <Modal.Footer>
                    {this.renderFooterButtons()}
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
        balance: state.GlobalReducer.user.balance,
        showConfirmPaymentForm: state.GlobalReducer.showConfirmPaymentForm,
        purchaseId: state.GlobalReducer.purchaseId,
        operationType: state.GlobalReducer.balanceOperationType
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetUserBalance: (token, callback) => {
            return getUserBalance(token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setUserBalance(json.message.balance));
                    callback();
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetGiftDetails: (giftId, callback) => {
            return getGiftDetails(giftId).then(([response, json]) => {
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

        onGetAuthorDetails: (userId) => {
            return getAuthorDetails(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setAuthor(json));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetUserDetails: (userId) => {
            return getAuthorDetails(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setUserDetails(json));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetBookCost: (bookId, callback) => {
            return getBookCost(bookId).then(([response, json]) => {
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

        forgetPurchaseId: () => {
            dispatch(setPurchaseId(null));
        },

        onClose: () => {
            dispatch(closeConfirmPaymentForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ConfirmPaymentForm);