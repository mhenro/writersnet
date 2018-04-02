import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';
import { getLocale } from '../locale.jsx';

import ChatGroupList from '../components/messages/ChatGroupList.jsx';
import WriteMessageForm from '../components/messages/WriteMessageForm.jsx';

import {
    getAuthorChatGroups,
    getAuthorDetails,
    setAuthor
} from '../actions/AuthorActions.jsx';
import {
    openWriteMessageForm,
    createNotify
} from '../actions/GlobalActions.jsx';
import { setToken } from '../actions/AuthActions.jsx';

class MessagesPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            chatGroups: [],
            activePage: 1,
            totalPages: 1,
            firstUpdate: true,
            searchPattern: ''
        };
        ['updateChatGroups', 'pageSelect', 'onSearchChange'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        setTimeout(() => {
            if (this.props.login && this.props.token) {
                this.props.onGetAuthorChatGroups(this.props.login, this.props.token, this.state.activePage, this.updateChatGroups);
                this.props.onGetAuthorDetails(this.props.login);
            }
        }, 500);
        this.timer = setInterval(() => {
            if (this.props.login && this.props.token) {
                this.props.onGetAuthorChatGroups(this.props.login, this.props.token, this.state.activePage, this.updateChatGroups);
                this.props.onGetAuthorDetails(this.props.login);
            }
        }, 3000);
    }

    componentWillUnmount() {
        clearInterval(this.timer);
    }

    updateChatGroups(page) {
        let firstUpdate = this.state.firstUpdate;
        this.setState(oldState => ({
            chatGroups: page.content.filter(group => (oldState.searchPattern ? group.primaryRecipientFullName.toLowerCase().includes(oldState.searchPattern.toLowerCase()) : true)),
            totalPages: page.totalPages,
            activePage: firstUpdate ? page.totalPages : page.number + 1,
            firstUpdate: false
        }));
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });
        this.props.onGetAuthorChatGroups(this.props.login, this.props.token, page, this.updateChatGroups);
    }

    onSearchChange(event) {
        this.setState({
            searchPattern: event.target.value
        });
    }

    onWriteMessage() {
        this.props.onOpenWriteMessageForm();
    }

    isDataLoaded() {
        if (this.props.author && this.props.login && this.props.login) {
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
                    <button onClick={() => this.onWriteMessage()} className="btn btn-danger">{getLocale(this.props.language)['Write message']}</button>
                </div>
                <div className="col-sm-12"></div>
                <div className="col-sm-12">
                    <div className="input-group">
                        <input type="text" value={this.state.searchPattern} onChange={this.onSearchChange} className="form-control" placeholder={getLocale(this.props.language)['Input chat name']} />
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
                    <ChatGroupList groups={this.state.chatGroups} author={this.props.author}/>
                </div>
                <WriteMessageForm/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token,
        author: state.AuthorReducer.author,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthorDetails: (userId) => {
            getAuthorDetails(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setAuthor(json));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetAuthorChatGroups: (userId, token, page, callback) => {
            getAuthorChatGroups(userId, token, page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
                }
                else if (json.message.includes('JWT expired at')) {
                    dispatch(setToken(''));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onOpenWriteMessageForm: () => {
            dispatch(openWriteMessageForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(MessagesPage);