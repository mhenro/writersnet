import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button, Tooltip, OverlayTrigger } from 'react-bootstrap';

import { createNotify } from '../../actions/GlobalActions.jsx';
import { getContest, closeContestEstimationForm } from '../../actions/ContestActions.jsx';

/*
    props:
    - contestId
 */
class ContestEstimationForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            contest: null
        };
    }

    isDataLoaded() {
        return this.state.contest !== null && typeof this.state.contest !== 'undefined';
    }

    render() {
        return (
            <Modal show={this.props.showContestEstimationForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    {this.getCaption()}
                </Modal.Header>
                {this.renderBody()}
                <Modal.Footer>
                    {this.renderFooterButtons()}
                </Modal.Footer>
            </Modal>
        )
    }

    onShow() {
        if (this.props.contestId !== null) {
            this.props.onGetContestDetails(this.props.contestId, contest => this.updateContest(contest));
            //this.props.isContestReadyToStart(this.props.contestId, ready => this.setState({readyToStart: ready}));
        }
    }

    onClose() {
        this.props.onCloseForm();
        this.updateContest(null);
    }

    updateContest(contest) {
        this.setState({
            contest: contest
        });
    }

    renderBody() {
        return (
            <Modal.Body>
                <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                    <div className="form-group">
                        <label className="control-label col-sm-5" htmlFor="prizeFund">Prize fund, $:</label>
                        <div className="col-sm-7">
                            <input value={this.getPrizeFund()} readOnly="true" type="text" className="form-control" id="prizeFund" placeholder="Enter the prize fund amount" name="prizeFund"/>
                        </div>
                    </div>
                </form>
            </Modal.Body>
        )
    }

    getCaption() {
        if (this.isDataLoaded()) {
            return 'Contest "' + this.state.contest.name + '"';
        } else {
            return 'Contest is loading... Please wait';
        }
    }

    getPrizeFund() {
        if (this.isDataLoaded()) {
            return parseFloat(this.state.contest.prizeFund / 100).toFixed(2);
        } else {
            return parseFloat(0).toFixed(2);
        }
    }

    renderFooterButtons() {
        return (
            <div className="btn-group">
                <Button onClick={() => this.onClose()}
                        className="btn btn-default">Close</Button>
            </div>
        )
    }

    onSubmit(event) {
        event.preventDefault();
    }
}

const mapStateToProps = (state) => {
    return {
        showContestEstimationForm: state.ContestReducer.showContestEstimationForm
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetContestDetails: (id, callback) => {
            getContest(id).then(([response, json]) => {
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

        onCloseForm: () => {
            dispatch(closeContestEstimationForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ContestEstimationForm);