import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button, Tooltip, OverlayTrigger } from 'react-bootstrap';
import { closeContestEditForm, getContest, saveContest } from '../../actions/ContestActions.jsx';
import { createNotify } from '../../actions/GlobalActions.jsx';
import { getLocale } from '../../locale.jsx';

import {
    showContestDonateForm,
    setContestIdForDonate,
    showSearchAuthorForm,
    addJudgesToContest,
    getJudgesFromContest,
    addParticipantsToContest,
    getParticipantsFromContest
} from '../../actions/ContestActions.jsx';

import UserList from './UserList.jsx';
import ContestDonateForm from './ContestDonateForm.jsx';
import SearchAuthorForm from './SearchAuthorForm.jsx';

/*
    props:
    - contestId
    - onSave - callback
 */
class ContestEditForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            contest: null,
            name: '',
            prizeFund: 0,
            revenue1: 0,
            revenue2: 0,
            revenue3: 0,
            addUserCallback: null,
            getSelectedAuthors: null
        };
    }

    updateData(data) {
        if (data) {
            this.setState({
                contest: data,
                name: data.name,
                prizeFund: parseFloat(data.prizeFund / 100).toFixed(2),
                revenue1: data.firstPlaceRevenue,
                revenue2: data.secondPlaceRevenue,
                revenue3: data.thirdPlaceRevenue
            });
        } else {
            this.setState({
                contest: null,
                name: '',
                prizeFund: 0,
                revenue1: 0,
                revenue2: 0,
                revenue3: 0
            });
        }
    }

    getCaption() {
        if (this.state.contest) {
            return 'Edit "' + this.state.contest.name + '"';
        }
        return 'Create new contest';
    }

    onFieldChange(proxy) {
        switch (proxy.target.id) {
            case 'name': this.setState({name: proxy.target.value}); break;
            case 'prizeFund': this.setState({prizeFund: proxy.target.value}); break;
            case 'revenue1': this.setState({revenue1: proxy.target.value}); break;
            case 'revenue2': this.setState({revenue2: proxy.target.value}); break;
            case 'revenue3': this.setState({revenue3: proxy.target.value}); break;
        }
    }

    onDonate() {
        if (!this.state.contest || !this.state.contest.id) {
            return;
        }
        this.props.onSetContestIdForDonate(this.state.contest.id);
        this.props.onShowDonateForm();
    }

    onSelectAuthors() {
        this.setState({
            addUserCallback: (addJudgeRequest, token, callback) => this.props.onAddParticipants(addJudgeRequest, token, callback),
            getSelectedAuthors: (contestId, callback) => this.props.onGetParticipants(contestId, callback)
        });
        setTimeout(() => {
            this.props.onShowSearchAuthorForm();
        }, 500);
    }

    onSelectJudges() {
        this.setState({
            addUserCallback: (addJudgeRequest, token, callback) => this.props.onAddJudges(addJudgeRequest, token, callback),
            getSelectedAuthors: (contestId, callback) => this.props.onGetJudges(contestId, callback)
        });
        setTimeout(() => {
            this.props.onShowSearchAuthorForm();
        }, 500);
    }

    onSubmit(event) {
        event.preventDefault();
        //TODO
    }

    renderBody() {
        return (
            <Modal.Body>
                <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                    <div className="form-group">
                        <label className="control-label col-sm-5" htmlFor="name">{getLocale(this.props.language)['Name:']}</label>
                        <div className="col-sm-7">
                            <input value={this.state.name} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="name" placeholder="Enter the contest name" name="name"/>
                        </div>
                    </div>
                    <div className="form-group">
                        <label className="control-label col-sm-5" htmlFor="prizeFund">Prize fund, $:</label>
                        <div className="col-sm-5">
                            <input value={this.state.prizeFund} readOnly="true" type="text" className="form-control" id="prizeFund" placeholder="Enter the prize fund amount" name="prizeFund"/>
                        </div>
                        <div className="col-sm-2">
                            <button onClick={() => this.onDonate()} className="btn btn-success">Donate</button>
                        </div>
                    </div>
                    <div className="form-group">
                        <label className="control-label col-sm-5" htmlFor="revenue1">Revenue for 1st place, %:</label>
                        <div className="col-sm-7">
                            <input value={this.state.revenue1} onChange={proxy => this.onFieldChange(proxy)} type="number" min="0" max="100" className="form-control" id="revenue1" placeholder="0-100%" name="revenue1"/>
                        </div>
                    </div>
                    <div className="form-group">
                        <label className="control-label col-sm-5" htmlFor="revenue2">Revenue for 2nd place, %:</label>
                        <div className="col-sm-7">
                            <input value={this.state.revenue2} onChange={proxy => this.onFieldChange(proxy)} type="number" min="0" max="100" className="form-control" id="revenue2" placeholder="0-100%" name="revenue2"/>
                        </div>
                    </div>
                    <div className="form-group">
                        <label className="control-label col-sm-5" htmlFor="revenue3">Revenue for 3rd place, %:</label>
                        <div className="col-sm-7">
                            <input value={this.state.revenue3} onChange={proxy => this.onFieldChange(proxy)} type="number" min="0" max="100" className="form-control" id="revenue3" placeholder="0-100%" name="revenue3"/>
                        </div>
                    </div>
                    <div className="form-group">
                        <UserList listName="Participants" onAddNewMember={() => this.onSelectAuthors()}/>
                    </div>
                    <div className="form-group">
                        <UserList listName="Judges" onAddNewMember={() => this.onSelectJudges()}/>
                    </div>
                </form>

                {/* popup form for set amount of donate */}
                <ContestDonateForm onUpdateAfterClosing={() => this.onShow()}/>

                {/* popup form for selecting authors */}
                <SearchAuthorForm onAddUsers={this.state.addUserCallback}
                                  onGetSelectedAuthors={this.state.getSelectedAuthors}
                                  token={this.props.token}
                                  contestId={this.props.contestId}/>
            </Modal.Body>
        )
    }

    renderFooterButtons() {
        let btnCreateCaption = this.state.contest ? 'Edit' : 'Create';
        return (
            <div className="btn-group">
                <Button onClick={() => this.onCreate()}
                        className="btn btn-success">{btnCreateCaption}</Button>
                <Button onClick={() => this.onClose()}
                        className="btn btn-default">Cancel</Button>
            </div>
        )
    }

    onCreate() {
        let contestRequest = {
            name: this.state.name,
            creatorId: this.props.login,
            prizeFund: Math.ceil(this.state.prizeFund * 100),
            firstPlaceRevenue: this.state.revenue1,
            secondPlaceRevenue: this.state.revenue2,
            thirdPlaceRevenue: this.state.revenue3
        };
        if (this.state.contest) {
            contestRequest.id = this.state.contest.id;
        }

        this.props.onSaveContest(contestRequest, this.props.token, () => this.props.onSave(), () => this.onClose());
    }

    onShow() {
        if (this.props.contestId !== null) {
            this.props.onGetContestDetails(this.props.contestId, data => this.updateData(data));
        }
    }

    onClose() {
        this.props.onCloseForm();
        this.updateData(null);
    }

    render() {
        return (
            <Modal show={this.props.showContestEditForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
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
}

const mapStateToProps = (state) => {
    return {
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token,
        showContestEditForm: state.ContestReducer.showContestEditForm,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetContestDetails: (id, callback) => {
            return getContest(id).then(([response, json]) => {
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

        onSaveContest: (contestRequest, token, onSave, onClose) => {
            return saveContest(contestRequest, token).then(([response, json]) => {
                if (response.status === 200) {
                    onSave();
                    onClose();
                    dispatch(createNotify('success', 'Success', 'Contest was successfully updated'));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onAddJudges: (addJudgeRequest, token, callback) => {
            return addJudgesToContest(addJudgeRequest, token).then(([response, json]) => {
                if (response.status === 200) {
                    callback();
                    dispatch(createNotify('success', 'Success', 'Mailings for judges were successfully sent out'));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetJudges: (contestId, callback) => {
            return getJudgesFromContest(contestId).then(([response, json]) => {
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

        onAddParticipants: (addJudgeRequest, token, callback) => {
            return addParticipantsToContest(addJudgeRequest, token).then(([response, json]) => {
                if (response.status === 200) {
                    callback();
                    dispatch(createNotify('success', 'Success', 'Mailings for participants were successfully sent out'));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetParticipants: (contestId, callback) => {
            return getParticipantsFromContest(contestId).then(([response, json]) => {
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

        onShowDonateForm: () => {
            dispatch(showContestDonateForm());
        },

        onShowSearchAuthorForm: () => {
            dispatch(showSearchAuthorForm());
        },

        onSetContestIdForDonate: (contestId) => {
            dispatch(setContestIdForDonate(contestId));
        },

        onCloseForm: () => {
            dispatch(closeContestEditForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ContestEditForm);