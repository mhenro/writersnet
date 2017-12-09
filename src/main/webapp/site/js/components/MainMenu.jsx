import React from 'react';
import { Link } from 'react-router-dom';

/*
    props:
    - login - user id
 */
class MainMenu extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activeItem: 'My page',
            unreadMessages: 0,
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
        return <span>Messages <span className="badge">{this.state.unreadMessages}</span></span>
    }

    getFriendsCaption() {
        return <span>Friends <span className="badge">{this.state.newFriends}</span></span>
    }

    render() {
        return(
            <ul className="nav nav-pills nav-stacked">
                <li className={this.getActiveItem('My page')}><Link to={'/authors/' + this.props.login} onClick={() => this.onItemClick('My page')}>My page</Link></li>
                <li className={this.getActiveItem('News')}><Link to="/news" onClick={() => this.onItemClick('News')}>News</Link></li>
                <li className={this.getActiveItem('Messages')}><Link to="/messages" onClick={() => this.onItemClick('Messages')}>{this.getMessagesCaption()}</Link></li>
                <li className={this.getActiveItem('Friends')}><Link to="/friends" onClick={() => this.onItemClick('Friends')}>{this.getFriendsCaption()}</Link></li>
                {/*<li className={this.getActiveItem('Groups')}><Link to="/groups" onClick={() => this.onItemClick('Groups')}>Groups</Link></li>*/}
            </ul>
        )
    }
}

export default MainMenu;