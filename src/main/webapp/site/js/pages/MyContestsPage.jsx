import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';
import { getLocale } from '../locale.jsx';

class MyContestsPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeTab: 'participants',
            searchPattern: '',
            contestsAsParticipant: [],
            contestAsJudge: [],
            contestAsCreator: [],
            currentPage: 1,
            totalPages: 1,
            asParticipant: 0,
            asJudge: 0,
            asCreator: 0
        };
    }

    pageSelect(page) {
        this.setState({
            currentPage: page
        });
        this.getFriendships(this.state.activeTab, page);
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
        this.getFriendships(tabName);
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
    return {}
};

export default connect(mapStateToProps, mapDispatchToProps)(MyContestsPage);