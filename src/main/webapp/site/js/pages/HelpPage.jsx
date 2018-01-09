import React from 'react';
import { connect } from 'react-redux';

import { getCaptcha } from '../actions/CaptchaActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

/*
 props:
 - authors - list of authors
 */
class HelpPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            email: '',
            subject: '',
            message: '',
            captcha: ''
        };
    }

    onFieldChange(proxy) {
        switch (proxy.target.id) {
            case 'name': this.setState({name: proxy.target.value}); break;
            case 'email': this.setState({email: proxy.target.value}); break;
            case 'subject': this.setState({subject: proxy.target.value}); break;
            case 'message': this.setState({message: proxy.target.value}); break;
            case 'captcha': this.setState({captcha: proxy.target.value}); break;
        }
    }

    onSubmit(event) {
        event.preventDefault();
    }

    render() {
        return (
            <div>
                <div className="col-sm-12">
                    <div className="panel panel-default">
                        <div className="panel-heading">
                            Contact form
                        </div>
                        <div className="panel-body">
                            <div>

                            </div>
                            <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                                <div className="form-group text-center">
                                    Your personal info
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-sm-2" htmlFor="name">Name:</label>
                                    <div className="col-sm-10">
                                        <input required value={this.state.name} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="name" placeholder="Enter your name" name="name"/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-sm-2" htmlFor="email">Email:</label>
                                    <div className="col-sm-10">
                                        <input required value={this.state.email} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="email" placeholder="Enter your email" name="email"/>
                                    </div>
                                </div>
                                <div className="form-group text-center">
                                    Your message
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-sm-2" htmlFor="subject">Subject:</label>
                                    <div className="col-sm-10">
                                        <input required value={this.state.subject} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="subject" placeholder="Enter the subject" name="subject"/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-sm-2" htmlFor="message">Message:</label>
                                    <div className="col-sm-10">
                                        <textarea required value={this.state.message} onChange={proxy => this.onFieldChange(proxy)} rows="5" className="form-control" id="message" placeholder="Enter your message" name="message"/>
                                    </div>
                                </div>
                                <div className="form-group text-center">
                                    Security
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-2"></div>
                                    <div className="col-sm-10">
                                        <img src="https://localhost/api/captcha" className="img-rounded" width="200" height="40"/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-sm-2" htmlFor="captcha">Captcha:</label>
                                    <div className="col-sm-10">
                                        <input required value={this.state.captcha} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="captcha" placeholder="Enter the code from the picture above" name="captcha"/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-12 text-center">
                                        <button type="submit" className="btn btn-success">Save</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <br/>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        authors: state.AuthorReducer.authors
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetCaptcha: (callback) => {
            return getCaptcha().then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.message);
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

export default connect(mapStateToProps, mapDispatchToProps)(HelpPage);