import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { getLocale } from '../locale.jsx';

/*
    props:
    - onLoginClick - function
    - onLogoutClick - function
    - language
    - balance
 */
class NavBar extends React.Component {
    static contextTypes = {
        router: PropTypes.shape({
            history: PropTypes.shape({
                push: PropTypes.func.isRequired,
                replace: PropTypes.func.isRequired
            }).isRequired,
            staticContext: PropTypes.object
        }).isRequired
    };

    constructor(props) {
        super(props);

        this.state = {
            activeItem: 'book'
        };

        ['onItemClick'].map(fn => this[fn] = this[fn].bind(this));
    }

    onItemClick(activeItem) {
        this.setState({
            activeItem: activeItem
        })
    }

    onTitleClick() {
        this.context.router.history.push('/');
    }

    getActiveItem(activeItem) {
        return this.state.activeItem === activeItem ? 'active' : '';
    }

    getBalance() {
        let balance = this.props.balance ? this.props.balance : 0;
        return parseFloat(balance / 100).toFixed(2);
    }

    renderSignupButton() {
        if (this.props.registered) {
           return (
               <li>
                   <Link to="/balance"><span className="glyphicon glyphicon-usd"></span> Balance: <span className="balance">{this.getBalance()}</span> cr.</Link>
               </li>
           )
        } else {
            return (
                <li>
                    <a onClick={() => this.props.onLoginClick(true)} style={{cursor: 'pointer'}}>
                        <span className="glyphicon glyphicon-user"></span> {getLocale(this.props.language)['Sign Up']}
                    </a>
                </li>
            )
        }
    }

    renderLoginButton() {
        if (this.props.registered) {
            return (
                <li><a onClick={() => this.props.onLogoutClick()} style={{cursor: 'pointer'}}><span className="glyphicon glyphicon-log-in"></span> {getLocale(this.props.language)['Log out']}</a></li>
            )
        }
        return (
            <li><a onClick={() => this.props.onLoginClick(false)} style={{cursor: 'pointer'}}><span className="glyphicon glyphicon-log-in"></span> {getLocale(this.props.language)['Log in']}</a></li>
        )
    }

    render() {
        return (
            <nav className="navbar navbar-inverse">
                <div className="container container-fluid">
                    <ul className="nav navbar-nav">
                        <li><img onClick={() => this.onTitleClick()} src="https://localhost/css/images/writersnets.png" className="clickable" width="auto" height="50"/></li>
                        <li className={this.getActiveItem('books')}><Link to="/books" onClick={() => this.onItemClick('books')}>{getLocale(this.props.language)['Books']}</Link></li>
                        <li className={this.getActiveItem('authors')}><Link to="/authors" onClick={() => this.onItemClick('authors')}>{getLocale(this.props.language)['Authors']}</Link></li>
                        <li className={this.getActiveItem('ratings')}><Link to="/ratings" onClick={() => this.onItemClick('ratings')}>{getLocale(this.props.language)['Ratings']}</Link></li>
                        <li className={this.getActiveItem('contests')}><Link to="/contests" onClick={() => this.onItemClick('contests')}>{getLocale(this.props.language)['Contests']}</Link></li>
                        <li className={this.getActiveItem('discussions')}><Link to="/discussions" onClick={() => this.onItemClick('discussions')}>{getLocale(this.props.language)['Discussions']}</Link></li>
                        <li className={this.getActiveItem('reviews')}><Link to="/reviews" onClick={() => this.onItemClick('reviews')}>{getLocale(this.props.language)['Reviews']}</Link></li>
                        <li className={this.getActiveItem('help')}><Link to="/help" onClick={() => this.onItemClick('help')}>{getLocale(this.props.language)['Help']}</Link></li>
                    </ul>
                    <ul className="nav navbar-nav navbar-right">
                        {this.renderSignupButton()}
                        {this.renderLoginButton()}
                    </ul>
                </div>
            </nav>
        )
    }
}

export default NavBar;