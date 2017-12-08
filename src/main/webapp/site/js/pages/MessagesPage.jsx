import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';

import ChatGroupList from '../components/messages/ChatGroupList.jsx';

import {
    getAuthorChatGroups
} from '../actions/AuthorActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

class MessagesPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            chatGroups: [],
            activePage: 1,
            totalPages: 1
        };
        ['updateChatGroups', 'pageSelect'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        let timer = setInterval(() => {
            if (this.props.login && this.props.token) {
                this.props.onGetAuthorChatGroups(this.props.login, this.props.token, 0, this.updateChatGroups);
                clearInterval(timer);
            }
        }, 1000);
    }

    updateChatGroups(page) {
        console.log(page);
        this.setState({
            chatGroups: page.content,
            totalPages: page.totalPages,
            activePage: page.number + 1
        });
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });
        this.props.onGetAuthorChatGroups(this.props.login, this.props.token, page, this.updateChatGroups);
    }

    isDataLoaded() {
        if (this.props.login && this.props.login) {
            return true;
        }
        return false;
    }

    render() {
        if (!this.isDataLoaded()) {
            return null;
        }

        return (
            <div>
                <div className="col-sm-12">
                    <button className="btn btn-danger">Write message</button>
                </div>
                <div className="col-sm-12"></div>
                <div className="col-sm-12">
                    <div className="input-group">
                        <input type="text" className="form-control" placeholder="Search" />
                        <div className="input-group-btn">
                            <button className="btn btn-default" type="submit">
                                <i className="glyphicon glyphicon-search"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <div className="col-sm-12">
                    <br/>
                    <Pagination
                        className={'shown'}
                        prev
                        next
                        first
                        last
                        ellipsis
                        boundaryLinks
                        items={this.state.totalPages}
                        maxButtons={3}
                        activePage={this.state.activePage}
                        onSelect={this.pageSelect}/>
                </div>
                <div className="col-sm-12">
                    <ChatGroupList groups={this.state.chatGroups}/>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthorChatGroups: (userId, token, page, callback) => {
            return getAuthorChatGroups(userId, token, page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
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

export default connect(mapStateToProps, mapDispatchToProps)(MessagesPage);