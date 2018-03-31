import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';
import { getLocale } from '../locale.jsx';

import {
    getParticipantContests,
    getJudgeContests,
    getCreatorContests,
    showContestEditForm
} from '../actions/ContestActions.jsx';
import { createNotify } from '../actions/GlobalActions.jsx';

import ContestList from '../components/contests/ContestList.jsx';
import ContestEditForm from '../components/contests/ContestEditForm.jsx';

class MyContestsPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeTab: 'participants',
            searchPattern: '',
            contestId: null,
            contestsAsParticipant: [],
            contestsAsJudge: [],
            contestsAsCreator: [],
            asParticipant: 0,
            asJudge: 0,
            asCreator: 0,
            currentPage: 1,
            totalPages: 1
        };
    }

    componentDidMount() {
        let timer = setInterval(() => {
            if (this.props.login) {
                this.getContests('participants');   //not a good solution. It is for counting friend groups
                this.getContests('judges');         //not a good solution. It is for counting friend groups
                this.getContests('creators');       //not a good solution. It is for counting friend groups
                clearInterval(timer);
            }
        }, 1000);
    }

    getContests(activeTab = this.state.activeTab, page = this.state.currentPage) {
        switch(activeTab) {
            case 'participants': this.props.onGetParticipantContests(this.props.login, page - 1, contests => this.updateContests(contests, 'contestsAsParticipant', 'asParticipant')); break;
            case 'judges': this.props.onGetJudgeContests(this.props.login, page - 1, contests => this.updateContests(contests, 'contestsAsJudge', 'asJudge')); break;
            case 'creators': this.props.onGetCreatorContests(this.props.login, page - 1, contests => this.updateContests(contests, 'contestsAsCreator', 'asCreator')); break;
        }
    }

    onShowEditForm(id) {
        this.setState({
            contestId: id
        });
        this.props.onShowContestEditForm();
    }

    updateContests(contests, array, friendshipGroup) {
        let state = {
            currentPage: contests.number + 1,
            totalPages: contests.totalPages,
        };
        state[array] = contests.content;
        state[friendshipGroup] = contests.totalElements;
        this.setState(state);
    }

    pageSelect(page) {
        this.setState({
            currentPage: page
        });
        this.getContests(this.state.activeTab, page);
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
        this.getContests(tabName);
    }

    onSearchChange(event) {
        this.setState({
            searchPattern: event.target.value
        });
    }

    getTabCaption(tabName) {
        if (tabName === 'participants') {
            let count = this.state.asParticipant;
            return <span>{getLocale(this.props.language)['As participant']} <span className="counter">{count}</span></span>;
        } else if (tabName === 'judges') {
            let count = this.state.asJudge;
            return <span>{getLocale(this.props.language)['As judge']} <span className="counter">{count}</span></span>;
        } else if (tabName === 'creators') {
            let count = this.state.asCreator;
            return <span>{getLocale(this.props.language)['As creator']} <span className="counter">{count}</span></span>;
        }
    }

    getItems() {
        switch (this.state.activeTab) {
            case 'participants': return this.state.contestsAsParticipant; break;
            case 'judges': return this.state.contestsAsJudge; break;
            case 'creators': return this.state.contestsAsCreator; break;
        }
    }

    render() {
        return (
            <div>
                <div className="col-sm-12">
                    <ul className="nav nav-tabs">
                        <li className={this.getActiveClass('participants')}><a onClick={() => this.changeTab('participants')}>{this.getTabCaption('participants')}</a></li>
                        <li className={this.getActiveClass('judges')}><a onClick={() => this.changeTab('judges')}>{this.getTabCaption('judges')}</a></li>
                        <li className={this.getActiveClass('creators')}><a onClick={() => this.changeTab('creators')}>{this.getTabCaption('creators')}</a></li>
                    </ul>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <div className="input-group">
                        <input value={this.state.searchPattern} onChange={event => this.onSearchChange(event)} type="text" className="form-control" placeholder="Input contest name" />
                        <div className="input-group-btn">
                            <button className="btn btn-default" type="submit">
                                <i className="glyphicon glyphicon-search"></i>
                            </button>
                        </div>
                    </div>
                    <br/>
                </div>
                <div className="col-sm-12 text-center">
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
                        activePage={this.state.currentPage}
                        onSelect={page => this.pageSelect(page)}/>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <ContestList contests={this.getItems()} onShowContestEditForm={id => this.onShowEditForm(id)}/>
                    {/*<FriendList friends={this.getItems()}
                                sendMsgButton={this.getSendMsgButtonVisibility()}
                                addFriendButton={this.getAddFriendButtonVisibility()}
                                readNewsButton={this.getReadNewsButtonVisibility()}
                                removeFriendButton={this.getRemoveFriendButtonVisibility()}
                                onAddToFriends={friend => this.onAddToFriends(friend)}
                                onRemoveSubscription={friend => this.onRemoveFriend(friend)}
                                login={this.props.login}
                                token={this.props.token}
                                onGetGroupId={this.props.onGetGroupIdByRecipient}
                                language={this.props.language}/>
                    <br/>*/}
                </div>

                {/* Contest popup form */}
                <ContestEditForm contestId={this.state.contestId} onSave={() => this.componentDidMount()}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetParticipantContests: (userId, page, callback) => {
            return getParticipantContests(userId, page - 1).then(([response, json]) => {
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

        onGetJudgeContests: (userId, page, callback) => {
            return getJudgeContests(userId, page - 1).then(([response, json]) => {
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

        onGetCreatorContests: (userId, page, callback) => {
            return getCreatorContests(userId, page - 1).then(([response, json]) => {
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

        onShowContestEditForm: () => {
            dispatch(showContestEditForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(MyContestsPage);