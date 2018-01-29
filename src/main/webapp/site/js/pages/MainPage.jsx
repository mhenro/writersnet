import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import Notifier from '../components/Notifier.jsx';
import { connect } from 'react-redux';
import { openLoginForm } from '../actions/GlobalActions.jsx';
import {
    setToken
} from '../actions/AuthActions.jsx';

import NavBar from '../components/NavBar.jsx';
import MainMenu from '../components/MainMenu.jsx';
import Footer from '../components/Footer.jsx';
import ScrollToTopButton from '../components/ScrollToTopButton.jsx';

import TitlePage from '../pages/TitlePage.jsx';
import AuthorPage from '../pages/AuthorPage.jsx';
import BookPage from '../pages/BookPage.jsx';
import BookReader from '../pages/BookReader.jsx';
import SectionPage from '../pages/SectionPage.jsx';
import LoginForm from '../components/LoginForm.jsx';
import OptionsPage from '../pages/OptionsPage.jsx';
import RatingPage from '../pages/RatingPage.jsx';
import FriendsPage from '../pages/FriendsPage.jsx';
import MessagesPage from '../pages/MessagesPage.jsx';
import ChatPage from '../pages/ChatPage.jsx';
import NewsPage from '../pages/NewsPage.jsx';
import ReviewPage from '../pages/ReviewPage.jsx';
import DiscussionPage from '../pages/DiscussionPage.jsx';
import HelpPage from '../pages/HelpPage.jsx';
import BalancePage from '../pages/BalancePage.jsx';
import ConfirmPaymentForm from '../components/balance/ConfirmPaymentForm.jsx';

class MainPage extends React.Component {
    constructor(props) {
        super(props);

        ['onLoginClick', 'onLogoutClick'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        window.scrollTo(0, 0);
    }

    onLoginClick(loginFormRegister) {
        this.props.onLoginClick(loginFormRegister);
    }

    onLogoutClick() {
        this.props.onLogoutClick();
    }

    renderMainMenu() {
        if (this.props.registered) {
            return (
                <MainMenu login={this.props.login} unreadMessages={this.props.unreadMessages} newFriends={this.props.newFriends} language={this.props.language}/>
            )
        }
        return null;
    }

    renderAds() {
        if (this.props.userDetails && this.props.userDetails.premium || this.props.login === 'Anonymous') {
            return null;
        }
        return (
            <div className="well hidden-xs">
                <p>ADS</p>
            </div>
        )
    }

    render() {
        return (
            <BrowserRouter basename="/">
                <div>
                    <NavBar onLoginClick={this.onLoginClick}
                            onLogoutClick={this.props.onLogoutClick}
                            registered={this.props.registered}
                            language={this.props.language}
                            balance={this.props.balance}/>
                    <div className="container">
                        <div className="row">
                            <div className="col-sm-12 col-lg-2">
                                {this.renderMainMenu()}
                                {this.renderAds()}
                            </div>
                            <div className="col-sm-12 col-lg-10">
                                <Route exact path="/" component={TitlePage}/>
                                <Route exact path="/authors" component={AuthorPage}/>
                                <Route exact path="/authors/:authorName" component={SectionPage}/>
                                <Route exact path="/books" component={BookPage}/>
                                <Route exact path="/ratings" component={RatingPage}/>
                                <Route exact path="/reviews/:bookId" component={ReviewPage}/>
                                <Route exact path="/reviews" component={ReviewPage}/>
                                <Route exact path="/discussions" component={DiscussionPage}/>
                                <Route exact path="/help" component={HelpPage}/>
                                <Route exact path="/balance" component={BalancePage}/>
                                <Route exact path="/reader/:bookId" component={BookReader}/>
                                <Route exact path="/options" component={this.props.registered ? OptionsPage : BookPage}/>
                                <Route exact path="/news" component={NewsPage}/>
                                <Route exact path="/friends" component={FriendsPage}/>
                                <Route exact path="/messages" component={MessagesPage}/>
                                <Route exact path="/chat/:groupId" component={ChatPage}/>
                            </div>
                        </div>
                    </div>
                    <ScrollToTopButton scrollStepInPx="50" delayInMs="16.66"/>
                    <Footer contentHeight={this.props.contentHeight}/>
                    <ConfirmPaymentForm/>
                    <LoginForm/>
                    <Notifier/>
                </div>
            </BrowserRouter>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        registered: state.GlobalReducer.registered,
        login: state.GlobalReducer.user.login,
        unreadMessages: state.GlobalReducer.unreadMessages,
        newFriends: state.GlobalReducer.newFriends,
        userDetails: state.GlobalReducer.user.details,
        language: state.GlobalReducer.language,
        balance: state.GlobalReducer.user.balance,
        contentHeight: state.GlobalReducer.contentHeight
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onLoginClick: (loginFormRegister) => {
            dispatch(openLoginForm(loginFormRegister));
        },

        onLogoutClick: () => {
            dispatch(setToken(''));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(MainPage);