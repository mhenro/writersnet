import React from 'react';
import { connect } from 'react-redux';
import Select from 'react-select';
import { Modal, Button } from 'react-bootstrap';
import PropTypes from 'prop-types';

import {
    closeWriteMessageForm,
    createNotify
} from '../../actions/GlobalActions.jsx';
import { getFriends } from '../../actions/AuthorActions.jsx';
import { addMessageToGroup } from '../../actions/MessageActions.jsx';
import { setToken } from '../../actions/AuthActions.jsx';
import { getLocale } from '../../locale.jsx';

/*
    props:
    - showWriteMessageForm
    - login
    - token
*/
class WriteMessageForm extends React.Component {
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
            friend: {
                value: null,
                label: null
            },
            message: ""
        };
        ['loadFriends', 'onFriendChange', 'onFieldChange', 'redirectToChat'].map(fn => this[fn] = this[fn].bind(this));
    }

    onShow() {

    }

    onClose() {
        this.props.onClose();
    }

    loadFriends(value) {
        if (!value || value === '') {
            return Promise.resolve({
                options: []
            });
        }
        return this.props.onGetFriends(this.props.login, value, this.props.token, 1);
    }

    onFriendChange(friend) {
        this.setState({
            friend: friend
        });
    }

    onFieldChange(proxy) {
        switch (proxy.target.id) {
            case 'message': this.setState({message: proxy.target.value}); break;
        }
    }

    onSubmit(event) {
        event.preventDefault();
    }

    redirectToChat(groupId) {
        this.context.router.history.push('/chat/' + groupId);
    }

    onSend() {
        this.props.onAddMessageToGroup(this.props.login, null, this.state.friend.value, this.state.message, this.props.token, this.redirectToChat);
        this.onClose();
    }

    render() {
        return (
            <Modal show={this.props.showWriteMessageForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    {getLocale(this.props.language)['Send message']}
                </Modal.Header>
                <Modal.Body>
                    <form className="form-horizontal" onSubmit={this.onSubmit}>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="friend">{getLocale(this.props.language)['Friend:']}</label>
                            <div className="col-sm-10">
                                <Select.Async value={this.state.friend}
                                              id="friend"
                                              loadOptions={this.loadFriends}
                                              onChange={this.onFriendChange}
                                              noResultsText="Nothing found"
                                              loadingPlaceholder="Searching..."
                                              placeholder={getLocale(this.props.language)['Select a friend']}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="message">{getLocale(this.props.language)['Message:']}</label>
                            <div className="col-sm-10">
                                <input value={this.state.message} onChange={this.onFieldChange} type="text" className="form-control" id="message" placeholder={getLocale(this.props.language)['Write a message']} name="message"/>
                            </div>
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <div className="btn-group">
                        <Button onClick={() => this.onSend()} className="btn btn-default">{getLocale(this.props.language)['Send']}</Button>
                        <Button onClick={() => this.onClose()} className="btn btn-default">{getLocale(this.props.language)['Close']}</Button>
                    </div>
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showWriteMessageForm: state.GlobalReducer.showWriteMessageForm,
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetFriends: (userId, matcher, token, page/*, callback*/) => {
            getFriends(userId, matcher, token, page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    let options = [],
                        friends = json.content;
                    friends.forEach(friend => {
                        options.push({
                            value: friend.friendId,
                            label: friend.fullName
                        });
                    });
                    return {options};
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

        onAddMessageToGroup: (userId, groupId, recipientId, text, token, callback) => {
            addMessageToGroup(userId, groupId, recipientId, text, token).then(([response, json]) => {
                if (response.status === 200) {
                    if (json.code === 0) {
                        callback(json.message);
                    }
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

        onClose: () => {
            dispatch(closeWriteMessageForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(WriteMessageForm);