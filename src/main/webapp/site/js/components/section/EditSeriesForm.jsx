import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';
import Select from 'react-select';

import {
    closeEditSeriesForm
} from '../../actions/BookActions.jsx';

/*
    props:
    - showEditSeriesForm
    - userId
 */
class EditSeriesForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
        };

        ['onSubmit', 'onSeriesChange'].map(fn => this[fn] = this[fn].bind(this));
    }

    getSeriesItems() {
        let options = [];

        return options;
    }

    onSeriesChange() {

    }

    onShow() {

    }

    onClose() {
        this.props.onCloseEditSeriesForm();
        //this.props.onGetAuthorDetails(this.props.author.username);
    }

    onSave() {
        this.onClose();
    }

    onSubmit(event) {
        event.preventDefault();
    }

    render() {
        return (
            <Modal bsSize="large" show={this.props.showEditSeriesForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    Edit your series
                </Modal.Header>
                <Modal.Body>
                    <form className="form-horizontal" onSubmit={this.onSubmit}>
                        <div className="form-group col-sm-12">
                            <Button className="btn btn-success">Add new serie</Button>
                        </div>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="serie">Existed series:</label>
                            <div className="col-sm-6">
                                <Select value={this.state.language} id="serie" options={this.getSeriesItems()} onChange={this.onSeriesChange} placeholder="Choose the book serie"/>
                            </div>
                            <div className="col-sm-4 btn-group">
                                <Button className="btn btn-danger">Delete serie</Button>
                                <Button className="btn btn-default">Rename serie</Button>
                            </div>
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <div className="btn-group">
                        <Button onClick={() => this.onSave()} className="btn btn-success">Save</Button>
                        <Button onClick={() => this.onClose()} className="btn btn-default">Close</Button>
                    </div>
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showEditSeriesForm: state.BookReducer.showEditSeriesForm
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onCloseEditSeriesForm: () => {
            dispatch(closeEditSeriesForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(EditSeriesForm);