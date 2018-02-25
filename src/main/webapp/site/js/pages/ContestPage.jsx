import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';

import { getAllContests } from '../actions/ContestActions.jsx';
import { createNotify } from '../actions/GlobalActions.jsx';

import ContestEditForm from '../components/contests/ContestEditForm.jsx';
import ContestManagementForm from '../components/contests/ContestManagementForm.jsx';
import ContestList from '../components/contests/ContestList.jsx';

class ContestPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activePage: 1,
            totalPages: 1,
            contests: []
        };
    }

    componentDidMount() {
        this.props.onGetAllContests(this.state.activePage, data => this.updateContests(data));
    }

    updateContests(data) {
        this.setState({
            totalPages: data.totalPages,
            contests: data.content
        });
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        this.props.onGetAllContests(page, data => this.updateContests(data));
    }

    render() {
        return (
            <div>
                <div className="col-sm-12 text-center">
                    <button className="btn btn-success">Create new contest</button>
                    <br/>
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
                        activePage={this.state.activePage}
                        onSelect={page => this.pageSelect(page)}/>
                </div>
                <div className="col-sm-12 text-center">
                    <ContestList contests={this.state.contests}/>
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
                        activePage={this.state.activePage}
                        onSelect={page => this.pageSelect(page)}/>
                </div>

                {/* Contest popup form */}
                <ContestEditForm/>

                {/* Contest management popup form */}
                <ContestManagementForm/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        author: state.AuthorReducer.author,
        registered: state.GlobalReducer.registered,
        token: state.GlobalReducer.token,
        login: state.GlobalReducer.user.login,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAllContests: (page, callback) => {
            return getAllContests(page - 1).then(([response, json]) => {
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
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ContestPage);