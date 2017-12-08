import React from 'react';
import { connect } from 'react-redux';
import Select from 'react-select';
import { Modal, Button } from 'react-bootstrap';

import {
    closeWriteMessageForm,
    createNotify
} from '../../actions/GlobalActions.jsx';
import { getFriends } from '../../actions/MessageActions.jsx';

/*
    props:
    - showWriteMessageForm
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
        ['onFriendChange', 'onFieldChange'].map(fn => this[fn] = this[fn].bind(this));
    }

    onShow() {

    }

    onClose() {
        this.props.onClose();
    }

    getFriends() {
        let options = [];
       /* this.props.genres.forEach(genre => {
            options.push({
                value: genre,
                label: getLocale(this.props.language)[genre]
            });
        });*/
        return options;
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
                    Header
                </Modal.Header>
                <Modal.Body>
                    <form className="form-horizontal" onSubmit={this.onSubmit}>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="friend">Friend:</label>
                            <div className="col-sm-10">
                                <Select value={this.state.friend} id="friend" options={this.getFriends()} onChange={this.onFriendChange} placeholder="Select a friend"/>    //TODO: change to Select.Async for getFriends()
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
        showWriteMessageForm: state.GlobalReducer.showWriteMessageForm
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetFriends: (userId, token, page, callback) => {
            return getFriends(userId, token, page - 1).then(([response, json]) => {
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

        onClose: () => {
            dispatch(closeWriteMessageForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(WriteMessageForm);