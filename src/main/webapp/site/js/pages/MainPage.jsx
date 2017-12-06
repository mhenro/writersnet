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
import AuthorPage from '../pages/AuthorPage.jsx';
import BookPage from '../pages/BookPage.jsx';
import BookReader from '../pages/BookReader.jsx';
import SectionPage from '../pages/SectionPage.jsx';
import LoginForm from '../components/LoginForm.jsx';
import OptionsPage from '../pages/OptionsPage.jsx';
import RatingPage from '../pages/RatingPage.jsx';
import FriendsPage from '../pages/FriendsPage.jsx';
import MessagesPage from '../pages/MessagesPage.jsx';
import ScrollToTopButton from '../components/ScrollToTopButton.jsx';

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
                <MainMenu login={this.props.login}/>
            )
        }
        return null;
    }

    render() {
        return (
            <BrowserRouter basename="/">
                <div>
                    <NavBar onLoginClick={this.onLoginClick} onLogoutClick={this.props.onLogoutClick} registered={this.props.registered}/>
                    <div className="container">
                        <div className="row">
                            <div className="col-sm-12 col-lg-2">
                                {this.renderMainMenu()}
                            </div>
                            <div className="col-sm-12 col-lg-10">
                                <Route exact path="/authors" component={AuthorPage}/>
                                <Route exact path="/authors/:authorName" component={SectionPage}/>
                                <Route exact path="/books" component={BookPage}/>
                                <Route exact path="/ratings" component={RatingPage}/>
                                <Route exact path="/reader/:bookId" component={BookReader}/>
                                <Route exact path="/options" component={this.props.registered ? OptionsPage : BookPage}/>
                                <Route exact path="/friends" component={FriendsPage}/>
                                <Route exact path="/messages" component={MessagesPage}/>
                            </div>
                        </div>
                    </div>
                    <ScrollToTopButton scrollStepInPx="50" delayInMs="16.66"/>
                    <Footer/>
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
        login: state.GlobalReducer.user.login
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