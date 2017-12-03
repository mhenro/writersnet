import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';

import {closeUserPolicy} from '../actions/GlobalActions.jsx';

class UserPolicy extends React.Component {
    onClose() {
        this.props.onCloseUserPolicy();
    }

    render() {
        return (
            <Modal bsSize="large" show={this.props.showUserPolicy} onHide={() => this.onClose()}>
                <Modal.Header>
                    User policy
                </Modal.Header>
                <Modal.Body>
                    <div>
                        User policy text.
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button onClick={() => this.onClose()} className="btn btn-default">Close</Button>
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showUserPolicy: state.GlobalReducer.showUserPolicy
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onCloseUserPolicy: () => {
            dispatch(closeUserPolicy());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(UserPolicy);