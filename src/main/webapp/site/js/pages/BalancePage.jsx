import React from 'react';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';
import { getLocale } from '../locale.jsx';
import { formatDate } from '../utils.jsx';

import {
    createNotify
} from '../actions/GlobalActions.jsx';

class BalancePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activePage: 1,
            totalPages: 1
        };
    }
    topUpBalance() {

    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        //this.props.onGetAuthors(this.state.currentName, page, this.state.size, this.state.sortBy, totalPages => this.setTotalPages(totalPages));
    }

    render() {
        return (
            <div>
                <div className="col-sm-6">
                    <div className="panel panel-default">
                        <div className="panel-body">
                            <p>Balance...................................<span>0.00 cr.</span></p>
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
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-12">
                                    <table className="table table-hover">
                                        <thead>
                                            <tr>
                                                <td>Type</td>
                                                <td>Date</td>
                                                <td>Amount</td>
                                                <td>Total balance</td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>PRIZE</td>
                                                <td>{formatDate(new Date())}</td>
                                                <td>-14.32 cr.</td>
                                                <td>132.11 cr.</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {}
};

export default connect(mapStateToProps, mapDispatchToProps)(BalancePage);