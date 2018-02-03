import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';
import { getLocale } from '../../locale.jsx';

import { buy } from '../../actions/BalanceActions.jsx';
import {
    createNotify
} from '../../actions/GlobalActions.jsx';
import { OperationType } from '../../utils.jsx';

/*
    props:
    - showRechargeBalanceForm - boolean
    - onClose - callback
*/
class RechargeBalanceForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            amount: 0
        };
    }

    onRecharge() {
        if (this.state.amount <= 0) {
            return;
        }

        let buyRequest = {
            operationType: OperationType.BALANCE_RECHARGE,
            purchaseId: this.state.amount * 100,  //in that case purchaseId is an amount of a balance
            sourceUserId: this.props.login,
            destUserId: ''
        };
        this.props.onBuy(buyRequest, this.props.token, () => this.onClose());
    }

    onShow() {

    }

    onClose() {
        this.setState({ amount: 0 });
        this.props.onClose();
    }

    onFieldChange(proxy) {
        switch (proxy.target.id) {
            case 'amount': this.setState({amount: proxy.target.value}); break;
        }
    }

    onSubmit(event) {
        event.preventDefault();
        //TODO
    }

    render() {
        return (
            <Modal show={this.props.showRechargeBalanceForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    Balance recharge
                </Modal.Header>
                <Modal.Body>
                    <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                        <div className="form-group">
                            <label className="control-label col-sm-3" htmlFor="amount">{getLocale(this.props.language)['Amount, cr.:']}</label>
                            <div className="col-sm-9">
                                <input value={this.state.amount} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="amount" placeholder={getLocale(this.props.language)['Enter the amount you want to add to your balance']} name="amount"/>
                            </div>
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <div className="btn-group">
                        <Button onClick={() => this.onRecharge()} className="btn btn-success">{getLocale(this.props.language)['Recharge']}</Button>
                        <Button onClick={() => this.onClose()} className="btn btn-default">{getLocale(this.props.language)['Close']}</Button>
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
        language: state.GlobalReducer.language
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
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(RechargeBalanceForm);