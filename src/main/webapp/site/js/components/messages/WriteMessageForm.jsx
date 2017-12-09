import React from 'react';
import { connect } from 'react-redux';
import Select from 'react-select';
import { Modal, Button } from 'react-bootstrap';

import {
    closeWriteMessageForm,
    createNotify
} from '../../actions/GlobalActions.jsx';
import { getFriends } from '../../actions/AuthorActions.jsx';

/*
    props:
    - showWriteMessageForm
    - login
    - token
*/
class WriteMessageForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            friend: {
                value: null,
                label: null
            },
            message: ""
        };
        ['loadFriends', 'onFriendChange', 'onFieldChange'].map(fn => this[fn] = this[fn].bind(this));
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

    onSend() {
        this.onClose();
        //TODO:
    }

    render() {
        return (
            <Modal show={this.props.showWriteMessageForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    Send message
                </Modal.Header>
                <Modal.Body>
                    <form className="form-horizontal" onSubmit={this.onSubmit}>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="friend">Friend:</label>
                            <div className="col-sm-10">
                                <Select.Async value={this.state.friend}
                                              id="friend"
                                              loadOptions={this.loadFriends}
                                              onChange={this.onFriendChange}
                                              noResultsText="Nothing found"
                                              loadingPlaceholder="Searching..."
                                              placeholder="Select a friend"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="message">Message:</label>
                            <div className="col-sm-10">
                                <input value={this.state.message} onChange={this.onFieldChange} type="text" className="form-control" id="message" placeholder="Write a message" name="message"/>
                            </div>
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <div className="btn-group">
                        <Button onClick={() => this.onSend()} className="btn btn-default">Send</Button>
                        <Button onClick={() => this.onClose()} className="btn btn-default">Close</Button>
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
        token: state.GlobalReducer.token
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetFriends: (userId, matcher, token, page/*, callback*/) => {
            return getFriends(userId, matcher, token, page - 1).then(([response, json]) => {
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