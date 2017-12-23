import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';
import Select from 'react-select';

import {
    createNotify
} from '../../actions/GlobalActions.jsx';
import {
    getSeries,
    saveSerie,
    deleteSerie,
    closeEditSeriesForm
} from '../../actions/BookActions.jsx';
import { setToken } from '../../actions/AuthActions.jsx';

/*
    props:
    - showEditSeriesForm
    - userId
    - onCloseUpdate - callback
 */
class EditSeriesForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            series: [],
            selectedSerie: {
                value: null,
                label: ''
            },
            newSerieName: null,
            showConfirmDialog: false,
            showAddSerieDialog: false,
            showEditSerieDialog: false
        };
    }

    setSeries(series) {
        this.setState({
            series: series,
            selectedSerie: series.length > 0 ? {value: series[0], label: series[0].name} : {name: null}
        });
    }

    getSeriesItems() {
        let options = [];
        this.state.series.forEach(serie => {
            options.push({
                value: serie,
                label: serie.name
            });
        });
        return options;
    }

    onSeriesChange(serie) {
        this.setState({
            selectedSerie: serie
        });
    }

    onShow() {
        this.state = {
            series: [],
            selectedSerie: {
                value: null,
                label: ''
            },
            showConfirmDialog: false,
            showAddSerieDialog: false,
            showEditSerieDialog: false
        };
        this.props.onGetSeries(this.props.userId, series => this.setSeries(series));
    }

    onClose() {
        this.props.onCloseEditSeriesForm();
        this.props.onCloseUpdate();
    }

    onAddSerie() {
        if (!this.state.newSerieName) {
            return;
        }
        let serie = {
            name: this.state.newSerieName
        };
        this.props.onSaveSerie(serie, this.props.token, () => this.props.onGetSeries(this.props.userId, series => this.setSeries(series)));
        this.onCloseAddSerieDialog();
    }

    onRenameSerie() {
        if (!this.state.selectedSerie.value) {
            this.onCloseAddSerieDialog();
            return;
        }
        let serie = {
            id: this.state.selectedSerie.value.id,
            name: this.state.newSerieName
        };
        this.props.onSaveSerie(serie, this.props.token, () => this.props.onGetSeries(this.props.userId, series => this.setSeries(series)));
        this.onCloseAddSerieDialog();
    }

    onDeleteSerie() {
        if (!this.state.selectedSerie.value) {
            this.onCloseConfirmDialog();
            return;
        }
        let id = this.state.selectedSerie.value.id;
        this.props.onDeleteSerie(id, this.props.token, () => this.props.onGetSeries(this.props.userId, series => this.setSeries(series)));
        this.onCloseConfirmDialog();
    }

    onSubmit(event) {
        event.preventDefault();
    }

    onShowConfirmDialog() {
        this.setState({
            showConfirmDialog: true
        });
    }

    onCloseConfirmDialog() {
        this.setState({
            showConfirmDialog: false
        });
    }

    onShowAddSerieDialog() {
        this.setState({
            showAddSerieDialog: true,
            showEditSerieDialog: false,
            newSerieName: ''
        });
    }

    onShowEditSerieDialog() {
        if (!this.state.selectedSerie.value) {
            return;
        }
        this.setState({
            showAddSerieDialog: false,
            showEditSerieDialog: true,
            newSerieName: this.state.selectedSerie.value.name
        });
    }

    onSerieNameChange(event) {
        this.setState({
            newSerieName: event.target.value
        });
    }

    onCloseAddSerieDialog() {
        this.setState({
            showAddSerieDialog: false,
            showEditSerieDialog: false
        });
    }

    render() {
        return (
            <div>
                <Modal bsSize="large" show={this.props.showEditSeriesForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                    <Modal.Header>
                        Edit your series
                    </Modal.Header>
                    <Modal.Body>
                        <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                            <div className="form-group col-sm-12">
                                <Button onClick={() => this.onShowAddSerieDialog()} className="btn btn-success">Add new serie</Button>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="serie">Existed series:</label>
                                <div className="col-sm-6">
                                    <Select value={this.state.selectedSerie} id="serie" options={this.getSeriesItems()} onChange={serie => this.onSeriesChange(serie)} placeholder="Choose the book serie"/>
                                </div>
                                <div className="col-sm-4 btn-group">
                                    <Button onClick={() => this.onShowConfirmDialog()} className="btn btn-danger">Delete serie</Button>
                                    <Button onClick={() => this.onShowEditSerieDialog()} className="btn btn-default">Rename serie</Button>
                                </div>
                            </div>
                        </form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={() => this.onClose()} className="btn btn-default">Close</Button>
                    </Modal.Footer>
                </Modal>

                {/* confirmation dialog */}
                <Modal show={this.state.showConfirmDialog}>
                    <Modal.Header>
                        Attention
                    </Modal.Header>
                    <Modal.Body>
                        {'Are you sure you want to delete "' + this.state.selectedSerie.label + '" serie?'}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={() => this.onDeleteSerie()} className="btn btn-danger">Yes</Button>
                        <Button onClick={() => this.onCloseConfirmDialog()} className="btn btn-default">No</Button>
                    </Modal.Footer>
                </Modal>

                {/* add new serie dialog */}
                <Modal show={this.state.showAddSerieDialog || this.state.showEditSerieDialog}>
                    <Modal.Header>
                        {this.state.showAddSerieDialog ? 'Type the name of the new serie' : 'Edit the name of the serie'}
                    </Modal.Header>
                    <Modal.Body>
                        <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                            <div className="form-group">
                                <div className="col-sm-12">
                                    <input type="text" onChange={event => this.onSerieNameChange(event)} value={this.state.newSerieName} className="form-control" placeholder="Enter the serie's name"/>
                                </div>
                            </div>
                        </form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.state.showAddSerieDialog ? () => this.onAddSerie() : () => this.onRenameSerie()} className="btn btn-success">Save</Button>
                        <Button onClick={() => this.onCloseAddSerieDialog()} className="btn btn-default">Close</Button>
                    </Modal.Footer>
                </Modal>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showEditSeriesForm: state.BookReducer.showEditSeriesForm,
        userId: state.AuthorReducer.author.username,
        token: state.GlobalReducer.token
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetSeries: (userId, callback) => {
            return getSeries(userId).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.content);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSaveSerie: (bookSerie, token, callback) => {
            return saveSerie(bookSerie, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Serie was added successfully'));
                    callback();
                }
                else if (json.message.includes('JWT expired at')) {
                    dispatch(setToken(''));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onDeleteSerie: (id, token, callback) => {
            return deleteSerie(id, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Serie was deleted successfully'));
                    callback();
                }
                else if (json.message.includes('JWT expired at')) {
                    dispatch(setToken(''));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onCloseEditSeriesForm: () => {
            dispatch(closeEditSeriesForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(EditSeriesForm);