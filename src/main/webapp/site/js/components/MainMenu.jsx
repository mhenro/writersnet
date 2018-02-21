import React from 'react';
import { Link } from 'react-router-dom';
import { getLocale } from '../locale.jsx';

/*
    props:
    - login - user id
    - unreadMessages - count of unread messages
    - newFriends - count of subscribers
    - language
 */
class MainMenu extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activeItem: 'My page',
            newFriends: 0
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

    getMessagesCaption() {
        if (this.props.unreadMessages > 0) {
            return <span>{getLocale(this.props.language)['Messages']} <span className="badge">{this.props.unreadMessages}</span></span>
        }
        return <span>{getLocale(this.props.language)['Messages']}</span>;
    }

    getFriendsCaption() {
        if (this.props.newFriends > 0) {
            return <span>{getLocale(this.props.language)['Friends']} <span className="badge">{this.props.newFriends}</span></span>
        }
        return <span>{getLocale(this.props.language)['Friends']}</span>
    }

    getMyContestsCaption() {
        //TODO: add counter here...
        //if (this.props.newFriends > 0) {
        //    return <span>{getLocale(this.props.language)['Friends']} <span className="badge">{this.props.newFriends}</span></span>
        //}
        return <span>{getLocale(this.props.language)['My contests']}</span>
    }

    render() {
        return(
            <ul className="nav nav-pills nav-stacked">
                <li className={this.getActiveItem('My page')}><Link to={'/authors/' + this.props.login} onClick={() => this.onItemClick('My page')}>{getLocale(this.props.language)['My page']}</Link></li>
                <li className={this.getActiveItem('News')}><Link to="/news" onClick={() => this.onItemClick('News')}>{getLocale(this.props.language)['News']}</Link></li>
                <li className={this.getActiveItem('Messages')}><Link to="/messages" onClick={() => this.onItemClick('Messages')}>{this.getMessagesCaption()}</Link></li>
                <li className={this.getActiveItem('Friends')}><Link to="/friends" onClick={() => this.onItemClick('Friends')}>{this.getFriendsCaption()}</Link></li>
                <li className={this.getActiveItem('My contests')}><Link to="/mycontests" onClick={() => this.onItemClick('My contests')}>{this.getMyContestsCaption()}</Link></li>
                {/*<li className={this.getActiveItem('Groups')}><Link to="/groups" onClick={() => this.onItemClick('Groups')}>Groups</Link></li>*/}
            </ul>
        )
    }
}

export default MainMenu;