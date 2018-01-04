import React from 'react';
import { connect } from 'react-redux';
import Slider from 'react-slick';

import { getAuthorsCount } from '../actions/AuthorActions.jsx';
import { getBooksCount } from '../actions/BookActions.jsx';
import { createNotify, getSessionsCount, openLoginForm } from '../actions/GlobalActions.jsx';

class TitlePage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            authorsCount: 0,
            booksCount: 0,
            onlineCount: 0
        };
    }

    componentDidMount() {
        this.props.onGetAuthorsCount(count => this.updateAuthorsCount(count));
        this.props.onGetBooksCount(count => this.updateBooksCount(count));
        this.props.onGetSessionsCount(count => this.updateSessionsCount(count));
    }

    updateAuthorsCount(authorsCount) {
        this.setState({
            authorsCount: authorsCount
        });
    }

    updateBooksCount(booksCount) {
        this.setState({
            booksCount: booksCount
        });
    }

    updateSessionsCount(onlineCount) {
        this.setState({
            onlineCount: onlineCount
        });
    }

    getTotalAuthors() {
        return this.state.authorsCount;
    }

    getTotalBooks() {
        return this.state.booksCount;
    }

    getTotalOnline() {
        return this.state.onlineCount;
    }

    wheel(e) {
        let delta = e.deltaY;
        if (delta > 0) {
            this.slider.slickNext();
        } else {
            this.slider.slickPrev();
        }
    }

    getScreenHeight() {
        return window.innerHeight - 200;
    }

    onLoginClick(loginFormRegister) {
        this.props.onLoginClick(loginFormRegister);
    }

    render() {
        let settings = {
            dots: true,
            vertical: true,
            adaptiveHeight: false,
            infinite: false
        };
        return (
            <div onWheel={(e) => this.wheel(e)}>
                <Slider {...settings} ref={(s) => this.slider = s}>
                    <div style={{height: this.getScreenHeight() + 'px'}}>
                        <h3 className="text-center">Welcome to WritersNets.com!</h3>
                        <br/><br/>
                        <div className="row">
                            <div className="col-sm-4">
                                <img className="img-responsive center-block" width="200" height="auto" src="https://localhost/css/images/author.png"/><br/>
                            </div>
                            <div className="col-sm-4">
                                <img className="img-responsive center-block" width="200" height="auto" src="https://localhost/css/images/books.png"/><br/>
                            </div>
                            <div className="col-sm-4">
                                <img className="img-responsive center-block" width="200" height="auto" src="https://localhost/css/images/online.jpg"/><br/>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-4 text-center">
                                We are already <span className="big-counter">{this.getTotalAuthors()}</span>!
                            </div>
                            <div className="col-sm-4 text-center">
                                We have <span className="big-counter">{this.getTotalBooks()}</span> books!
                            </div>
                            <div className="col-sm-4 text-center">
                                <span className="big-counter">{this.getTotalOnline()}</span> authors online!
                            </div>
                        </div>
                    </div>
                    <div style={{height: this.getScreenHeight() + 'px'}}>
                        <h3 className="text-center">You can become our author or reader or even both!</h3>
                        <br/><br/>
                        <div className="row">
                            <div className="col-sm-6">
                                <img className="img-responsive center-block" width="200" height="auto" src="https://localhost/css/images/author.png"/><br/><br/>
                            </div>
                            <div className="col-sm-6">
                                <img className="img-responsive center-block" width="200" height="auto" src="https://localhost/css/images/books.png"/><br/><br/>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                If you become our <span className="big-counter">author</span> you can easily publish your novels on <span className="big-counter">WritersNets.com</span> and let our readers to read them.<br/><br/>
                                So you will assemble your target audience and people will know about you even before you publish your book on paper.<br/><br/>
                                Among our readers you can meet publishers and this will open the way to paper publication!<br/><br/>
                            </div>
                            <div className="col-sm-6">
                                As our <span className="big-counter">reader</span> you get access to thousands of exclusive novels which are not published yet and you can't see them in stores yet.<br/><br/>
                                You can chat with author of the novel you like and even enter into a discussion with him about some scenes. Maybe this will affect the final version of the novel and you will be an accomplice to this work!<br/><br/>
                                Also you can estimate every novel which you like or dislike and help other people to navigate in a variety of novels, creating the ratings of the best novels together with them!<br/><br/>
                            </div>
                        </div>
                    </div>
                    <div style={{height: this.getScreenHeight() + 'px'}}>
                        <h3 className="text-center">Join to us!</h3>
                        <br/><br/>
                        It's simple!<br/><br/>
                        Just click on <a onClick={() => this.onLoginClick(true)} style={{cursor: 'pointer'}}>Sign Up</a> if it's your first time on <span className="big-counter">WritersNets.com</span> or <a onClick={() => this.onLoginClick(false)} style={{cursor: 'pointer'}}>Log In</a> if you're already with us ;)<br/><br/>
                    </div>
                </Slider>
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
        onGetAuthorsCount: (callback) => {
            return getAuthorsCount().then(([response, json]) => {
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

        onGetBooksCount: (callback) => {
            return getBooksCount().then(([response, json]) => {
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

        onGetSessionsCount: (callback) => {
            return getSessionsCount().then(([response, json]) => {
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

        onLoginClick: (loginFormRegister) => {
            dispatch(openLoginForm(loginFormRegister));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(TitlePage);