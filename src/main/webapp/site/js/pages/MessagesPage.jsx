import React from 'react';
import { connect } from 'react-redux';

import ChatGroupList from '../components/messages/ChatGroupList.jsx';

import {
    getAuthorDetails,
    setAuthor
} from '../actions/AuthorActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

class MessagesPage extends React.Component {
    componentDidMount() {
        let timer = setInterval(() => {
            if (this.props.login) {
                this.props.onGetAuthorDetails(this.props.login);
                clearInterval(timer);
            }
        }, 1000);
    }

    isDataLoaded() {
        if (this.props.author && this.props.login) {
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
                    <ChatGroupList groups={this.props.author.chatGroups}/>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token,
        author: state.AuthorReducer.author
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthorDetails: (userId) => {
            return getAuthorDetails(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setAuthor(json));
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