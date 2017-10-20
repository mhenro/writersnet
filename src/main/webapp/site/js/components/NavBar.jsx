import React from 'react';
import { Link } from 'react-router-dom';

/*
    props: onLoginClick - function
           onLogoutClick - function
 */
class NavBar extends React.Component {
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

    getActiveItem(activeItem) {
        return this.state.activeItem === activeItem ? 'active' : '';
    }

    renderLoginButton() {
        if (this.props.registered) {
            return (
                <li><a onClick={() => this.props.onLogoutClick()} style={{cursor: 'pointer'}}><span className="glyphicon glyphicon-log-in"></span> Log out</a></li>
            )
        }
        return (
            <li><a onClick={() => this.props.onLoginClick(false)} style={{cursor: 'pointer'}}><span className="glyphicon glyphicon-log-in"></span> Log in</a></li>
        )
    }

    render() {
        return (
            <nav className="navbar navbar-inverse">
                <div className="container container-fluid">
                    <ul className="nav navbar-nav">
                        <li className={this.getActiveItem('books')}><Link to="/books" onClick={() => this.onItemClick('books')}>Books</Link></li>
                        <li className={this.getActiveItem('authors')}><Link to="/authors" onClick={() => this.onItemClick('authors')}>Authors</Link></li>
                        <li className={this.getActiveItem('ratings')}><Link to="/ratings" onClick={() => this.onItemClick('ratings')}>Ratings</Link></li>
                        <li className={this.getActiveItem('discussions')}><Link to="/discussions" onClick={() => this.onItemClick('discussions')}>Discussions</Link></li>
                        <li className={this.getActiveItem('reviews')}><Link to="/reviews" onClick={() => this.onItemClick('reviews')}>Reviews</Link></li>
                        <li className={this.getActiveItem('help')}><Link to="/help" onClick={() => this.onItemClick('help')}>Help</Link></li>
                    </ul>
                    <ul className="nav navbar-nav navbar-right">
                        <li><a onClick={() => this.props.onLoginClick(true)} style={{cursor: 'pointer'}}><span className="glyphicon glyphicon-user"></span> Sign Up</a></li>
                        {this.renderLoginButton()}
                    </ul>
                </div>
            </nav>
        )
    }
}

export default NavBar;