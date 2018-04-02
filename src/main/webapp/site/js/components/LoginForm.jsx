import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';
import { getLocale } from '../locale.jsx';

import UserAgreement from './UserAgreement.jsx';
import ForgotPasswordForm from './ForgotPasswordForm.jsx';

import {
    closeLoginForm,
    createNotify,
    openUserPolicy,
    openForgotPasswordForm,
    setUserDetails
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

import {
    getAuthorDetails
} from '../actions/AuthorActions.jsx';

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
            this.props.onSendLogin(this.props.login, this.props.password, this, () => this.props.onGetAuthorDetails(this.props.login));    //login
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
                            <li className={this.getActiveItem('sign-up')}><a href="#" onClick={() => this.onItemClick('sign-up')}>{getLocale(this.props.language)['Sign up']}</a></li>
                            <li className={this.getActiveItem('log-in')}><a href="#" onClick={() => this.onItemClick('log-in')}>{getLocale(this.props.language)['Log in']}</a></li>
                        </ul>
                        <br/>
                        <form onSubmit={this.onSubmit}>
                            {this.state.activeItem === 'sign-up' ?
                                <div className="form-group">
                                    <input type="text" className="form-control" placeholder={getLocale(this.props.language)['Email']}
                                           onChange={this.onEmailChange}/>
                                </div> : null
                            }
                            <div className="form-group">
                                <input type="text" className="form-control" placeholder={getLocale(this.props.language)['Login']} onChange={this.onLoginChange}/>
                            </div>
                            <div className="form-group">
                                <input type="password" className="form-control" placeholder={getLocale(this.props.language)['Password']} onChange={this.onPasswordChange}/>
                            </div>
                            {this.state.activeItem === 'sign-up' ?
                                <div className="form-group">
                                    <input type="password" className="form-control" placeholder={getLocale(this.props.language)['Confirm password']}
                                           onChange={this.onPasswordConfirmChange}/>
                                </div> : null
                            }
                            {this.state.activeItem === 'sign-up' ?
                                <div className="checkbox">
                                    <label><input type="checkbox" onClick={this.policyClick} checked={this.state.policyAgreed}/> {getLocale(this.props.language)['I agree with']} <a href="#" onClick={() => this.onReadUserPolicy()}>{getLocale(this.props.language)['user agreement']}</a></label>
                                </div>
                                : <a onClick={() => this.props.onOpenForgotPasswordForm()} href="#">{getLocale(this.props.language)['Forgot your password?']}</a>
                            }
                            <div className="text-center">
                                <button type="submit" className="btn btn-success">{getLocale(this.props.language)[this.state.activeItem === 'sign-up' ? 'Register' : 'Login']}</button>
                            </div>
                        </form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.close}>{getLocale(this.props.language)['Close']}</Button>
                    </Modal.Footer>
                </Modal>
                <UserAgreement/>
                <ForgotPasswordForm/>
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
        language: state.GlobalReducer.language
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

        onOpenForgotPasswordForm: () => {
            dispatch(openForgotPasswordForm());
        },

        onSendLogin: (username, password, self, callback) => {
            sendLogin(username, password).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setToken(json.message));
                    sessionStorage.setItem('username', username);
                    dispatch(setPassword(''));
                    self.close();
                    callback();
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
            sendRegister(email, username, password).then(([response, json]) => {
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

        onGetAuthorDetails: (userId) => {
            getAuthorDetails(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setUserDetails(json));
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