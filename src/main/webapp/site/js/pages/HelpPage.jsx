import React from 'react';
import { connect } from 'react-redux';
import { getLocale } from '../locale.jsx';

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
                            {getLocale(this.props.language)['Contact form']}
                        </div>
                        <div className="panel-body">
                            <div className="text-justify">
                                {getLocale(this.props.language)['You can use the following contact form to ask any question about our project. We will try to answer on it asap.']}<br/>
                                <b>{getLocale(this.props.language)['Note']}</b>: {getLocale(this.props.language)['Before ask a question via contact form please be sure that there is no such question in the']} <b>FAQ</b>.
                                <br/><br/>
                            </div>
                            <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                                <div className="form-group text-center">
                                    {getLocale(this.props.language)['Your personal info']}
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-sm-2" htmlFor="name">{getLocale(this.props.language)['Name:']}</label>
                                    <div className="col-sm-10">
                                        <input required value={this.state.name} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="name" placeholder="Enter your name" name="name"/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-sm-2" htmlFor="email">{getLocale(this.props.language)['Email:']}</label>
                                    <div className="col-sm-10">
                                        <input required value={this.state.email} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="email" placeholder="Enter your email" name="email"/>
                                    </div>
                                </div>
                                <div className="form-group text-center">
                                    {getLocale(this.props.language)['Your message']}
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-sm-2" htmlFor="subject">{getLocale(this.props.language)['Subject:']}</label>
                                    <div className="col-sm-10">
                                        <input required value={this.state.subject} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="subject" placeholder="Enter the subject" name="subject"/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-sm-2" htmlFor="message">{getLocale(this.props.language)['Message:']}</label>
                                    <div className="col-sm-10">
                                        <textarea required value={this.state.message} onChange={proxy => this.onFieldChange(proxy)} rows="5" className="form-control" id="message" placeholder="Enter your message" name="message"/>
                                    </div>
                                </div>
                                <div className="form-group text-center">
                                    {getLocale(this.props.language)['Security']}
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
                                        <input required value={this.state.captcha} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="captcha" placeholder={getLocale(this.props.language)['Enter the code from the picture above']} name="captcha"/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-sm-12 text-center">
                                        <button type="submit" className="btn btn-success">{getLocale(this.props.language)['Send message']}</button>
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
                            <div className="faq-answer">
                                <div><img src="https://localhost/css/images/faq/new-book.png" width="100%" height="auto"/></div>
                                <div>You can add a new book only if you are registered. After you logged in click on "My page" button (1).</div>
                                <div>After you click "Add new book" button (2) the following popup form will appear:</div>
                                <div><img src="https://localhost/css/images/faq/new-book-2.png" width="100%" height="auto"/></div>
                                <div>Fill all necessary fields and click on "Save" button</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">5. How can I add a new serie?</p>
                            <div className="faq-answer">
                                <div><img src="https://localhost/css/images/faq/new-serie.png" width="100%" height="auto"/></div>
                                <div>You can add a new serie only if you are registered. After you logged in click on "My page" button (1).</div>
                                <div>After you click "Edit series" button (2) the following popup form will appear:</div>
                                <div><img src="https://localhost/css/images/faq/new-serie-2.png" width="100%" height="auto"/></div>
                                <div>To add new serie click on "Add new serie" button (1).</div>
                                <div>To delete serie select it in "Existed series" combobox then click on "Delete serie" button (2).</div>
                                <div>To edit serie select it in "Existed series" combobox then click on "Rename serie" button (3).</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">6. How can I add a review to the book?</p>
                            <div className="faq-answer">
                                <div>You can add a new review to the book only if you are registered.</div>
                                <div>Move to any author page you want, find a necessary book and click on "Add review" button (1) as shown below:</div>
                                <div><img src="https://localhost/css/images/faq/add-review.png" width="100%" height="auto"/></div>
                                <div>The following popup form will appear:</div>
                                <div><img src="https://localhost/css/images/faq/add-review-2.png" width="100%" height="auto"/></div>
                                <div>Enter a text into the "Name" field.</div>
                                <div>Choose score from "Score" combobox. It can be "very bad", "bad", "normal", "good" and "great".</div>
                                <div>In the last field enter a review text. This field allows some html tags such as headers, quotes and so on.</div>
                                <div>After you write all necessary data click on "Save" button.</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">7. How to add author to my subscriptions?</p>
                            <div className="faq-answer">
                                <div><img src="https://localhost/css/images/faq/subscription.png" width="100%" height="auto"/></div>
                                <div>If you want to add author to your subscription list go to the Authors page (1).</div>
                                <div>Then choose an author which you want and click on "Author page" button (2).</div>
                                <div>In the new screen click on "Add to friends" button (1):</div>
                                <div><img src="https://localhost/css/images/faq/subscription-2.png" width="100%" height="auto"/></div>
                                <div>If all is ok then you should see something like this:</div>
                                <div><img src="https://localhost/css/images/faq/subscription-3.png" width="100%" height="auto"/></div>
                                <div>"Add to friends" button" should change on "You are already subscribed" button (1).</div>
                                <div>Success popup should appear (2).</div>
                                <div>If you want to delete author from your subscription list you should go on the "Friends" page (1):</div>
                                <div><img src="https://localhost/css/images/faq/subscription-4.png" width="100%" height="auto"/></div>
                                <div>Then go to the "Subscriptions" tab (2).</div>
                                <div>Find the needed author and click on "Remove" button (3).</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">8. How to add author to my friends?</p>
                            <div className="faq-answer">
                                <div>Sometimes you can see a gray circle with number near the "Friends" link (1):</div>
                                <div><img src="https://localhost/css/images/faq/subscription-5.png" width="100%" height="auto"/></div>
                                <div>Click on "Friends" link (1) and go to the "Subscribers" tab (2).</div>
                                <div>You will see the list of authors which are subscribed on you.</div>
                                <div>Choose a needed author and click on "Add to friends" button (3).</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">9. Where can I see events which are happened with authors on which I'm subscribed?</p>
                            <div className="faq-answer">
                                <div>If you have subscriptions or friends you can see the events which are happened with them.</div>
                                <div><img src="https://localhost/css/images/faq/news.png" width="100%" height="auto"/></div>
                                <div>Click on "News" link (1).</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">10. How can I write a message to my friend?</p>
                            <div className="faq-answer">
                                <div>You can chat with your friends in two ways. First way is to go on "Friends" page (1):</div>
                                <div><img src="https://localhost/css/images/faq/message.png" width="100%" height="auto"/></div>
                                <div>Then go to the "Friends" tab and click on "Send message" button (2).</div>
                                <div>The second way is to go on "Messages" link (1):</div>
                                <div><img src="https://localhost/css/images/faq/message-3.png" width="100%" height="auto"/></div>
                                <div>On this screen you can see the list of the chat groups (2). You can click on any group to enter the chat.</div>
                                <div>If you don't see the needed chat group you can click on "Write message" button (3). The following popup form will appear:</div>
                                <div><img src="https://localhost/css/images/faq/message-4.png" width="100%" height="auto"/></div>
                                <div>Choose your friend in "Friend" combobox then enter a message in "Message" field then click on "Send" button.</div>
                                <div>In both ways you will be moved to the following screen:</div>
                                <div><img src="https://localhost/css/images/faq/message-5.png" width="100%" height="auto"/></div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">11. How to find a book?</p>
                            <div className="faq-answer">
                                <div>To see all books which existed on the site click on "Books" page in the top menu. The following screen will appear:</div>
                                <div><img src="https://localhost/css/images/faq/books-page.png" width="100%" height="auto"/></div>
                                <div>To filter books by alphabet click on (1) button.</div>
                                <div>If you know the name of the book or its part you can use the search field (2).</div>
                                <div>Also you can use additional filters by genre (3) and language (4).</div>
                                <div>To clear all filters click on "Clear filters" button (5).</div>
                                <div>To read the book click on "Read" button (6).</div>
                                <div>To move on the author page click on "Go to writer" button (7).</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">12. How to find an author?</p>
                            <div className="faq-answer">
                                <div>To see all authors which existed on the site click on "Authors" page in the top menu (1). The following screen will appear:</div>
                                <div><img src="https://localhost/css/images/faq/authors-page.png" width="100%" height="auto"/></div>
                                <div>To filter authors by alphabet click on (2) button.</div>
                                <div>If you know the name of the author or its part you can use the search field (3).</div>
                                <div>To move on the author page click on "Author page" button (4).</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">13. How to use ratings?</p>
                            <div className="faq-answer">
                                <div>If you click on "Ratings" button in the top menu (1) the following screen will appear:</div>
                                <div><img src="https://localhost/css/images/faq/ratings.png" width="100%" height="auto"/></div>
                                <div>This screen is separated on the two parts. The upper part is represented author ratings.</div>
                                <div>You can choose between several rating types (2) to find author which you want.</div>
                                <div>When you've found the needed author you can click on his name (3) to move on his page.</div>
                                <div>The bottom part is represented book ratings. You can choose between several rating types (4) to find book which you want.</div>
                                <div>When you've found the needed book you can click on its name (5) to move on its page.</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">14. How to use Discussions page?</p>
                            <div className="faq-answer">
                                <div>If you click on "Discussions" button in the top menu (1) the following screen will appear:</div>
                                <div><img src="https://localhost/css/images/faq/discussions.png" width="100%" height="auto"/></div>
                                <div>On this screen you can see the last comments from each book which exist on this site.</div>
                                <div>If you want to read a book click on (2) button.</div>
                                <div>If you want to move on the author page click (3) button.</div>
                            </div>
                            <br/><br/>
                            <p className="faq-question">15. How to use Reviews page?</p>
                            <div className="faq-answer">
                                <div>If you click on "Reviews" button in the top menu (1) the following screen will appear:</div>
                                <div><img src="https://localhost/css/images/faq/reviews.png" width="100%" height="auto"/></div>
                                <div>On this screen you can see all reviews which were left by other users.</div>
                                <div>If you want to see the reviews which are related to specific book enter the book name in the "Book" field (2).</div>
                                <div>In the below list you can see the reviews.</div>
                                <div>If you want to read the book instead of review you can click on (3) link.</div>
                                <div>If you want to move on the author page click on (5) link.</div>
                                <div>If you want to read a review click on "Read" button (4). The following popup form will appear:</div>
                                <div><img src="https://localhost/css/images/faq/reviews-2.png" width="100%" height="auto"/></div>
                                <div>If you want to like this review click on "Like" button (1).</div>
                                <div>If you want to dislike this review click on "Dislike" button (2).</div>
                                <div>For closing the popup form click on "Close" button (3).</div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        language: state.GlobalReducer.language
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