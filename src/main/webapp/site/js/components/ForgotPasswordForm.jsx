import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';
import { createNotify, closeForgotPasswordForm, confirmPasswordChanging } from '../actions/GlobalActions.jsx';

class ForgotPasswordForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: ''
        };
    }

    onFieldChange(proxy) {
        switch (proxy.target.id) {
            case 'email': this.setState({email: proxy.target.value}); break;
        }
    }

    onConfirmPasswordChanging() {
        this.props.onConfirmPasswordChanging(this.state.email, () => this.onClose());
    }

    onSubmit(event) {
        event.preventDefault();
    }

    onClose() {
        this.setState({
            email: ''
        });
        this.props.onClose();
    }

    render() {
        return (
            <Modal show={this.props.showForgotPasswordForm} onHide={() => this.onClose()}>
                <Modal.Body>
                    <div className="col-sm-12">
                        If you forgot your password you can write your email into the field below. We will send your further instructions to that email. Thank you!
                        <br/><br/>
                    </div>
                    <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="email">Email:</label>
                            <div className="col-sm-10">
                                <input value={this.state.email} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="email" placeholder="Enter your email" name="email"/>
                            </div>
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <div className="btn-group">
                        <Button onClick={() => this.onConfirmPasswordChanging()} className="btn btn-success">Confirm password changing</Button>
                        <Button onClick={() => this.onClose()}>Close</Button>
                    </div>
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showForgotPasswordForm: state.GlobalReducer.showForgotPasswordForm,
        login: state.GlobalReducer.user.login
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onConfirmPasswordChanging: (email, callback) => {
            return confirmPasswordChanging(email).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', json.message));
                    callback();
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onClose: () => {
            dispatch(closeForgotPasswordForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ForgotPasswordForm);