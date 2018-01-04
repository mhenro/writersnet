import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';

import UserAgreement from './UserAgreement.jsx';

import {
    closeLoginForm,
    createNotify,
    openUserPolicy
} from '../actions/GlobalActions.jsx';

import {
    sendLogin,
    sendRegister,
    setEmail,
    setLogin,
    setPassword,
    setPasswordConfirm,
    setToken
} from '../actions/AuthActions.jsx';

class LoginForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activeItem: 'sign-up',
            policyAgreed: false
        };

        ['close', 'onItemClick', 'onSubmit', 'onEmailChange', 'onLoginChange', 'onPasswordChange', 'onPasswordConfirmChange', 'policyClick'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            activeItem: nextProps.loginFormRegister ? 'sign-up' : 'log-in'
        });
    }

    onItemClick(activeItem) {
        this.setState({
            activeItem: activeItem
        })
    }

    policyClick() {
        this.setState(oldState => ({
            policyAgreed: !oldState.policyAgreed
        }));
    }

    onReadUserPolicy() {
        this.props.onOpenUserPolicy();
    }

    getActiveItem(activeItem) {
        return this.state.activeItem === activeItem ? 'active' : '';
    }

    close() {
        this.props.closeLoginForm();
        this.setState({
            policyAgreed: false
        });
    }

    onSubmit(event) {
        event.preventDefault();
        if (this.state.activeItem === 'sign-up') {
            if (!this.state.policyAgreed) {
                this.props.generateNotify('warning', 'Warning', 'You must agree with user agreement');
                return;
            }
            this.props.onSendRegister(this.props.email, this.props.login, this.props.password, this.props.passwordConfirm, this); //register new user
        } else {
            this.props.onSendLogin(this.props.login, this.props.password, this);    //login
        }
    }

    onEmailChange(event) {
        this.props.onSetEmail(event.target.value);
    }

    onLoginChange(event) {
        this.props.onSetLogin(event.target.value);
    }

    onPasswordChange(event) {
        this.props.onSetPassword(event.target.value);
    }

    onPasswordConfirmChange(event) {
        this.props.onSetPasswordConfirm(event.target.value);
    }

    render() {
        return (
            <div>
                <Modal show={this.props.showLoginForm} onHide={this.close}>
                    <Modal.Body>
                        <ul className="nav nav-tabs">
                            <li className={this.getActiveItem('sign-up')}><a href="#" onClick={() => this.onItemClick('sign-up')}>Sign up</a></li>
                            <li className={this.getActiveItem('log-in')}><a href="#" onClick={() => this.onItemClick('log-in')}>Log in</a></li>
                        </ul>
                        <br/>
                        <form onSubmit={this.onSubmit}>
                            {this.state.activeItem === 'sign-up' ?
                                <div className="form-group">
                                    <input type="text" className="form-control" placeholder="Email"
                                           onChange={this.onEmailChange}/>
                                </div> : null
                            }
                            <div className="form-group">
                                <input type="text" className="form-control" placeholder="Login" onChange={this.onLoginChange}/>
                            </div>
                            <div className="form-group">
                                <input type="password" className="form-control" placeholder="Password" onChange={this.onPasswordChange}/>
                            </div>
                            {this.state.activeItem === 'sign-up' ?
                                <div className="form-group">
                                    <input type="password" className="form-control" placeholder="Confirm password"
                                           onChange={this.onPasswordConfirmChange}/>
                                </div> : null
                            }
                            {this.state.activeItem === 'sign-up' ?
                                <div className="checkbox">
                                    <label><input type="checkbox" onClick={this.policyClick} checked={this.state.policyAgreed}/> I agree with <a href="#" onClick={() => this.onReadUserPolicy()}>user agreement</a></label>
                                </div>
                                : <a href="#">Forgot your password?</a>
                            }
                            <div className="text-center">
                                <button type="submit" className="btn btn-success">{this.state.activeItem === 'sign-up' ? 'Register' : 'Login'}</button>
                            </div>
                        </form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.close}>Close</Button>
                    </Modal.Footer>
                </Modal>
                <UserAgreement/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showLoginForm: state.GlobalReducer.showLoginForm,
        loginFormRegister: state.GlobalReducer.loginFormRegister,

        registered: state.GlobalReducer.registered,
        email: state.GlobalReducer.user.email,
        login: state.GlobalReducer.user.login,
        password: state.GlobalReducer.user.password,
        passwordConfirm: state.GlobalReducer.user.passwordConfirm,
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        closeLoginForm: () => {
            dispatch(closeLoginForm());
        },

        onOpenUserPolicy: () => {
            dispatch(openUserPolicy());
        },

        onSendLogin: (username, password, self) => {
            return sendLogin(username, password).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setToken(json.message));
                    sessionStorage.setItem('username', username);
                    dispatch(setPassword(''));
                    self.close();
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSendRegister: (email, username, password, passwordConfirm, self) => {
            if (password !== passwordConfirm) {
                dispatch(createNotify('warning', 'Warning', 'Password and its confirmation are not equal. Please check your password.'));
                return;
            }
            return sendRegister(email, username, password).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('info', 'Info', 'Registration was almost completed! Check your email for further instructions.'));
                    dispatch(setPassword(''));
                    self.close();
                }
                else if (response.status === 400) {
                    dispatch(createNotify('warning', 'Warning', 'User with such email or login already exist. Please choose another email and/or login.'));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSetEmail: (email) => {
            dispatch(setEmail(email));
        },

        onSetLogin: (login) => {
            dispatch(setLogin(login));
        },

        onSetPassword: (password) => {
            dispatch(setPassword(password));
        },

        onSetPasswordConfirm: (passwordConfirm) => {
            dispatch(setPasswordConfirm(passwordConfirm));
        },

        generateNotify: (type, header, message) => {
            dispatch(createNotify(type, header, message));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(LoginForm);