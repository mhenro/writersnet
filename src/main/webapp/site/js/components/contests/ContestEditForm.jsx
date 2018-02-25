import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button, Tooltip, OverlayTrigger } from 'react-bootstrap';
import { closeContestEditForm, getContest } from '../../actions/ContestActions.jsx';
import { createNotify } from '../../actions/GlobalActions.jsx';

/*
    props:
    - contestId
 */
class ContestEditForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            contest: null
        };
    }

    updateData(data) {
        this.setState({
            contest: data
        });
    }

    getCaption() {
        if (this.state.contest) {
            return 'Edit "' + this.state.contest.name + '"';
        }
        return 'Create new contest';
    }

    renderBody() {
        return (
            <Modal.Body>

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
        this.onClose();
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
        showContestEditForm: state.ContestReducer.showContestEditForm
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

        onCloseForm: () => {
            dispatch(closeContestEditForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ContestEditForm);