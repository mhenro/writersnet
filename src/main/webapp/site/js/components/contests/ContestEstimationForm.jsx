import React from 'react';
import {connect} from 'react-redux';
import {Modal, Button, Tooltip, OverlayTrigger, Pagination} from 'react-bootstrap';

import ParticipantList from './ParticipantList.jsx';

import {createNotify} from '../../actions/GlobalActions.jsx';
import {
    getContest,
    closeContestEstimationForm,
    getParticipantsRating,
    getParticipantsRatingDetails
} from '../../actions/ContestActions.jsx';

import {formatTimeInterval} from '../../utils.jsx';

/*
    props:
    - contestId
 */
class ContestEstimationForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            contest: null,
            activePage: 1,
            pageSize: 10,
            totalPages: 0,
            participants: []
        };
    }

    isDataLoaded() {
        return this.state.contest !== null && typeof this.state.contest !== 'undefined';
    }

    render() {
        return (
            <Modal show={this.props.showContestEstimationForm} onHide={() => this.onClose()}
                   onShow={() => this.onShow()}>
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
            this.props.onGetParticipantsRating(this.props.contestId, this.state.activePage, this.state.pageSize, rating => this.updateRatings(rating))
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

    updateRatings(rating) {
        this.setState({
            participants: rating.content,
            totalPages: rating.totalPages
        })
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        this.props.onGetParticipantsRating(this.props.contestId, page, this.state.pageSize, rating => this.updateRatings(rating));
    }

    renderBody() {
        return (
            <Modal.Body>
                <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                    <div className="form-group">
                        <label className="control-label col-sm-5" htmlFor="prizeFund">Prize fund, $:</label>
                        <div className="col-sm-7">
                            <input value={this.getPrizeFund()} readOnly="true" type="text" className="form-control"
                                   id="prizeFund" name="prizeFund"/>
                        </div>
                    </div>
                    <div className="form-group">
                        <label className="control-label col-sm-5" htmlFor="timeLimit">Time limit:</label>
                        <div className="col-sm-7">
                            <input value={this.getTimeLimit()} readOnly="true" type="text" className="form-control"
                                   id="timeLimit" name="timeLimit"/>
                        </div>
                    </div>
                    <hr/>
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
                    <div className="col-sm-12 text-center">
                        <ParticipantList participants={this.state.participants}
                                         participantsOffset={(this.state.activePage - 1) * this.state.pageSize}
                                         getRatingDetails={(bookId, callback) => this.props.onGetParticipantsRatingDetails(this.props.contestId, bookId, 0, 0, callback)}
                        />
                        <br/>
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

    getTimeLimit() {
        if (this.isDataLoaded() && this.state.contest.expirationDate) {
            let date1 = new Date(),
                date2 = new Date(this.state.contest.expirationDate),
                timeDiff = date2.getTime() - date1.getTime();

            if (timeDiff > 0) {
                return formatTimeInterval(timeDiff, 'Y years D days H hours and M minutes');
            } else {
                return 'Contest has been finished';
            }
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
        },

        onGetParticipantsRating: (contestId, page, size, callback) => {
            getParticipantsRating(contestId, page - 1, size).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetParticipantsRatingDetails: (contestId, bookId, page, size, callback) => {
            getParticipantsRatingDetails(contestId, bookId, page - 1, size).then(([response, json]) => {
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

export default connect(mapStateToProps, mapDispatchToProps)(ContestEstimationForm);