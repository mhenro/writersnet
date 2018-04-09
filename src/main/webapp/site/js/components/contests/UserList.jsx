import React from 'react';
import { Pagination } from 'react-bootstrap';

/*
    props:
    - listName - string
    - contestId
    - me - boolean
    - closed - boolean
    - started - boolean
    - onAddNewMember - callback
    - onGetUsers(page, callback) - callback
    - onRemoveUser(judgeId, callback) - callback
 */
class UserList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activePage: 1,
            totalPages: 1,
            users: []
        };
    }

    componentDidMount() {
        if (this.props.contestId === null || typeof this.props.contestId === 'undefined') {
            return;
        }

        this.props.onGetUsers(this.state.activePage, data => this.updateUsers(data));
        this.updateTimer = setInterval(() => this.props.onGetUsers(this.state.activePage, data => this.updateUsers(data)), 5000);
    }

    componentWillUnmount() {
        clearInterval(this.updateTimer);
    }

    pageSelect(page) {
        if (this.props.contestId === null || typeof this.props.contestId === 'undefined') {
            return;
        }

        this.setState({
            activePage: page
        });

        this.props.onGetUsers(page, data => this.updateUsers(data));
    }

    updateUsers(data) {
        this.setState({
            users: data.content,
            totalPages: data.totalPages
        });
    }

    renderUsers() {
        return this.state.users.map((user, key) => {
            let accepted = user.accepted ? <span style={{color: 'green'}}>Accepted</span> : <span style={{color: 'red'}}>Not accepted yet</span>
            let name = user.bookName ? user.bookName + ' (' + user.userName + ')' : user.userName;
            let id = user.bookId || user.userId;
            return (
                <tr key={key}>
                    <td>{name}</td>
                    <td>{accepted}</td>
                    <td>
                        <button onClick={() => this.props.onRemoveUser(id, () => this.componentDidMount())}
                                className={'btn btn-default btn-xs glyphicon glyphicon-remove ' + (this.props.me && !this.props.closed && !this.props.started ? '' : 'hidden')}
                                title="Remove this member from the list"></button>
                    </td>
                </tr>
            )
        });
    }

    render() {
        return (
            <div className="col-sm-12">
                <fieldset className="scheduler-border">
                    <legend className="scheduler-border">{this.props.listName}</legend>
                    <div>
                        <div className={'col-sm-12 text-center ' + (this.props.me && !this.props.closed && !this.props.started ? '' : 'hidden')}>
                            <button onClick={() => this.props.onAddNewMember()}
                                    className="btn btn-primary">Add new member</button>
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
                            <br/>
                            <br/>
                        </div>
                        <div className="col-sm-12 text-center">
                            <table className="table table-hover">
                                <tbody>
                                    {this.renderUsers()}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </fieldset>
            </div>
        )
    }
}

export default UserList;