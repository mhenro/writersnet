import React from 'react';
import { connect } from 'react-redux';

import FriendList from '../components/friends/FriendList.jsx';

import {
    createNotify
} from '../actions/GlobalActions.jsx';

class FriendsPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeTab: 'friends'    //requests
        };
    }

    getActiveClass(tabName) {
        if (tabName === this.state.activeTab) {
            return 'active';
        }
        return null;
    }

    changeTab(tabName) {
        this.setState({
            activeTab: tabName
        });
    }

    render() {
        return (
            <div>
                <ul className="nav nav-tabs">
                    <li className={this.getActiveClass('friends')}><a onClick={() => this.changeTab('friends')}>My friends</a></li>
                    <li className={this.getActiveClass('requests')}><a onClick={() => this.changeTab('requests')}>Friend requests</a></li>
                </ul>
                <br/>
                <div className="input-group">
                    <input type="text" className="form-control" placeholder="Search" />
                        <div className="input-group-btn">
                            <button className="btn btn-default" type="submit">
                                <i className="glyphicon glyphicon-search"></i>
                            </button>
                        </div>
                </div>
                <FriendList/>
            </div>
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

    }
};

export default connect(mapStateToProps, mapDispatchToProps)(FriendsPage);