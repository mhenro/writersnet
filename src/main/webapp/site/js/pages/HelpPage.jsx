import React from 'react';
import { connect } from 'react-redux';

import { sendFeedback } from '../actions/FeedbackActions.jsx';
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
            captcha: '',
            dt: new Date()
        };
    }

    clearScreen() {
        this.setState({
            name: '',
            email: '',
            subject: '',
            message: '',
            captcha: '',
            dt: new Date()
        });
    }

    updateCaptcha() {
        this.setState({
            dt: new Date()
        });
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
        let feedbackRequest = {
            name: this.state.name,
            email: this.state.email,
            subject: this.state. subject,
            message: this.state.message,
            captcha: this.state.captcha
        };
        this.props.onSendFeedback(feedbackRequest, () => this.clearScreen());
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
                            <div className="text-justify">
                                You can use the following contact form to ask any question about our project. We will try to answer on it asap.<br/>
                                <b>Note</b>: Before ask a question via contact form please be sure that there is no such question in the <b>FAQ</b>.
                                <br/><br/>
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
                                        <img src={'https://localhost/api/captcha?dt=' + this.state.dt} className="img-rounded" width="200" height="40"/>&nbsp;
                                        <button onClick={() => this.updateCaptcha()} type="button" className="btn btn-default glyphicon glyphicon-refresh"></button>
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
                                        <button type="submit" className="btn btn-success">Send message</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <br/>
                </div>

                <div className="col-sm-12">
                    <div className="panel panel-default">
                        <div className="panel-heading">
                            FAQ
                        </div>
                        <div className="panel-body">
                            <p className="faq-question">1. How to sign up?</p>
                            <div className="faq-answer">
                                <div>Find the following button on the navigation bar:</div>
                                <div><img src="https://localhost/css/images/faq/sign-up.png" width="100%" height="auto"/></div>
                                <div>The following popup form will appear:</div>
                                <div><img src="https://localhost/css/images/faq/sign-up-2.png" width="100%" height="auto"/></div>
                                <div>Fill all fields then set checkbox (1) if you agree with "User agreement" and then click on the Register button (2).</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">2. How to log in?</p>
                            <div className="faq-answer">
                                <div>Find the following button on the navigation bar:</div>
                                <div><img src="https://localhost/css/images/faq/log-in.png" width="100%" height="auto"/></div>
                                <div>The following popup form will appear:</div>
                                <div><img src="https://localhost/css/images/faq/log-in-2.png" width="100%" height="auto"/></div>
                                <div>Fill all fields then click on the Login button (2).</div>
                                <div>If you forgot your password click on "Forgot you password?" link.</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">3. How to setup my account?</p>
                            <div className="faq-answer">
                                <div><img src="https://localhost/css/images/faq/options.png" width="100%" height="auto"/></div>
                                <div>After you logged in to the site click on "My page" button in the left menu (1).</div>
                                <div>Then click on the "Options" button (2). The following popup form will appeared:</div>
                                <div><img src="https://localhost/css/images/faq/options-2.png" width="100%" height="auto"/></div>
                                <div>Fill all necessary fields and click on "Save" button.</div>
                                <div>Below you will see additional panel where you can change your avatar:</div>
                                <div><img src="https://localhost/css/images/faq/options-3.png" width="100%" height="auto"/></div>
                                <div>To choose a new avatar click on "Choose your avatar" button (1).</div>
                                <div>If you want to set the default avatar click "Restore default photo" button (2).</div>
                                <div>Also on this page you can change your password. You should enter your current password then set a new password and confirm it.</div>
                                <div>After that you should click on "Change password" button (3).</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">4. How can I add a new book?</p>
                            <span className="faq-answer">Trololo</span>
                            <br/><br/>
                            <p className="faq-question">5. How can I add a new serie?</p>
                            <span className="faq-answer">Trololo</span>
                            <br/><br/>
                            <p className="faq-question">6. How can I add a review to the book?</p>
                            <span className="faq-answer">Trololo</span>
                            <br/><br/>
                            <p className="faq-question">7. How to add author to my subscriptions?</p>
                            <span className="faq-answer">Trololo</span>
                            <br/><br/>
                            <p className="faq-question">8. Where can I see events which are happened with authors which I'm subscribed?</p>
                            <span className="faq-answer">Trololo</span>
                            <br/><br/>
                            <p className="faq-question">9. How can I write a message to my friend?</p>
                            <span className="faq-answer">Trololo</span>
                            <br/><br/>
                            <p className="faq-question">10. How to find a book?</p>
                            <span className="faq-answer">Trololo</span>
                            <br/><br/>
                            <p className="faq-question">11. How to find an author?</p>
                            <span className="faq-answer">Trololo</span>
                            <br/><br/>
                            <p className="faq-question">12. How to use ratings?</p>
                            <span className="faq-answer">Trololo</span>
                            <br/><br/>
                            <p className="faq-question">13. How to use Discussions page?</p>
                            <span className="faq-answer">Trololo</span>
                            <br/><br/>
                            <p className="faq-question">14. How to use Reviews page?</p>
                            <span className="faq-answer">Trololo</span>
                        </div>
                    </div>
                </div>

            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {

    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onSendFeedback: (feedbackRequest, callback) => {
            return sendFeedback(feedbackRequest).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Your message was sent successfully'));
                    callback();
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