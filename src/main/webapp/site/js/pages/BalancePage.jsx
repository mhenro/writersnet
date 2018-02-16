import React from 'react';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';
import { getLocale } from '../locale.jsx';
import { formatDate } from '../utils.jsx';

import RechargeBalanceForm from '../components/balance/RechargeBalanceForm.jsx';

import { getUserBalance, setUserBalance, getUserPaymentHistory } from '../actions/BalanceActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

class BalancePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activePage: 1,
            totalPages: 1,
            operations: [],
            showRechargeBalanceForm: false
        };
    }

    componentDidMount() {
        setTimeout(() => {
            if (this.props.token !== '') {
                this.props.onGetUserBalance(this.props.token);
                this.props.onGetUserPaymentHistory(this.props.token, this.state.activePage, (page) => this.updatePaymentHistory(page));
            }
        }, 500);
    }

    topUpBalance() {
        this.setState({
            showRechargeBalanceForm: true
        });
    }

    closeBalanceForm() {
        this.setState({
            showRechargeBalanceForm: false
        });
        this.props.onGetUserBalance(this.props.token);
        this.props.onGetUserPaymentHistory(this.props.token, this.state.activePage, (page) => this.updatePaymentHistory(page));
    }

    updatePaymentHistory(page) {
        this.setState({
            totalPages: page.totalPages,
            operations: page.content
        });
    }

    renderPaymentHistory() {
        let getDate = (timestamp) => {
            let date = new Date(timestamp);
            return formatDate(date);
        };
        let renderMoney = (money) => {
            let amount = money ? money : 0,
                result = parseFloat(amount / 100).toFixed(2),
                style = {color: result >= 0 ? 'green' : 'red', fontWeight: 'bold'};
            return <span style={style}>{result} cr.</span>
        };

        return this.state.operations.map((operation, key) => {
            return (
                <tr key={key}>
                    <td>{operation.operationType}</td>
                    <td>{getDate(operation.operationDate)}</td>
                    <td>{renderMoney(operation.operationCost)}</td>
                    <td>{renderMoney(operation.balance)}</td>
                </tr>
            )
        });
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        this.props.onGetUserPaymentHistory(this.props.token, page, (page) => this.updatePaymentHistory(page));
    }

    getBalance() {
        let balance = this.props.balance ? this.props.balance : 0;
        return parseFloat(balance / 100).toFixed(2);
    }

    render() {
        return (
            <div>
                <div className="col-sm-6">
                    <div className="panel panel-default">
                        <div className="panel-body">
                            <p>Balance...................................<span>{this.getBalance()} cr.</span></p>
                            <button onClick={() => this.topUpBalance()} className="btn btn-success">Top up the balance</button>
                        </div>
                    </div>
                </div>
                <div className="col-sm-6">
                    <div className="panel panel-default">
                        <div className="panel-body">
                            <div className="row">
                                <div className="col-sm-12 text-center">
                                    Payment history
                                    <br/><br/>
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
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-12">
                                    <table className="table table-hover">
                                        <thead>
                                            <tr>
                                                <td>Op. type</td>
                                                <td>Date</td>
                                                <td>Op. cost</td>
                                                <td>Balance</td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {this.renderPaymentHistory()}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* popup for recharging the balance */}
                <RechargeBalanceForm showRechargeBalanceForm={this.state.showRechargeBalanceForm} onClose={() => this.closeBalanceForm()}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        token: state.GlobalReducer.token,
        language: state.GlobalReducer.language,
        balance: state.GlobalReducer.user.balance
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetUserBalance: (token) => {
            return getUserBalance(token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setUserBalance(json.message.balance));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetUserPaymentHistory: (token, page, callback) => {
            return getUserPaymentHistory(token, page - 1).then(([response, json]) => {
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

export default connect(mapStateToProps, mapDispatchToProps)(BalancePage);