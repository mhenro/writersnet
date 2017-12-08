import React from 'react';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';
import MessageList from '../components/chat/MessageList.jsx';

import {
    getMessagesByGroup,
    addMessageToGroup
} from '../actions/MessageActions.jsx';

import {
    createNotify
} from '../actions/GlobalActions.jsx';

class ChatPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            messages: [],
            activePage: 1,
            totalPages: 1,
            firstUpdate: true,
            text: ""
        };
        ['pageSelect', 'updateMessages', 'onEditMessage', 'onKeyDown'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        setTimeout(() => {
            if (this.props.login && this.props.token) {
                this.props.onGetMessagesByGroup(this.props.login, this.props.match.params.groupId, this.props.token, this.state.activePage, this.updateMessages);
            }
        }, 500);
        this.timer = setInterval(() => {
            if (this.props.login && this.props.token) {
                this.props.onGetMessagesByGroup(this.props.login, this.props.match.params.groupId, this.props.token, this.state.activePage, this.updateMessages);
            }
        }, 3000);
    }

    componentWillUnmount() {
        clearInterval(this.timer);
    }

    updateMessages(page) {
        console.log(page);
        let firstUpdate = this.state.firstUpdate;
        this.setState({
            messages: page.content,
            totalPages: page.totalPages,
            activePage: firstUpdate ? page.totalPages : page.number + 1,
            firstUpdate: false
        });
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });
        this.props.onGetMessagesByGroup(this.props.login, this.props.match.params.groupId, this.props.token, page, this.updateMessages);
    }

    onEditMessage(event) {
        this.setState({
            text: event.target.value
        });
    }

    onKeyDown(key) {
        if (key.key === 'Enter') {
            this.sendMessage();
        }
    }

    sendMessage() {
        this.props.onAddMessageToGroup(this.props.login, this.props.match.params.groupId, this.state.text, this.props.token,
            () => this.props.onGetMessagesByGroup(this.props.login, this.props.match.params.groupId, this.props.token, this.state.activePage, this.updateMessages));
        this.setState({
            text: ''
        });
    }

    getGroupName() {
        return <h4>{'group #' + this.props.match.params.groupId}</h4>;
    }

    isDataLoaded() {
        if (this.props.token && this.props.login) {
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
                <div className="col-sm-12 text-right">
                    <button className="btn glyphicon glyphicon-cog"></button>
                </div>
                <div className="col-sm-12 text-center">
                    {this.getGroupName()}
                </div>
                <div className="col-sm-12">
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
                    <MessageList messages={this.state.messages}/>
                </div>
                <div className="col-sm-12">
                    <hr/>
                    <div className="input-group">
                        <input value={this.state.text} onChange={this.onEditMessage} onKeyDown={this.onKeyDown} type="text" className="form-control" placeholder="Write" />
                        <div className="input-group-btn">
                            <button onClick={() => this.sendMessage()} className="btn btn-default" type="submit">
                                <i className="glyphicon glyphicon-send"></i>
                            </button>
                        </div>
                    </div>
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
        onGetMessagesByGroup: (userId, groupId, token, page, callback) => {
            return getMessagesByGroup(userId, groupId, token, page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onAddMessageToGroup: (userId, groupId, text, token, callback) => {
            return addMessageToGroup(userId, groupId, text, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Your message was added successfully'));
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

export default connect(mapStateToProps, mapDispatchToProps)(ChatPage);