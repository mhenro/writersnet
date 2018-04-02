import React from 'react';
import { Link } from 'react-router-dom';

import { formatDate } from '../../utils.jsx';

/*
    props:
    - contests - array
    - onShowContestEditForm - callback
    - onJoin - callback
 */
class ContestList extends React.Component {
    getCreatorName(contest) {
        return <Link to={'/authors/' + contest.creatorId}>{contest.creatorFullName}</Link>
    }

    getCost(contest) {
        return parseFloat(contest.prizeFund / 100).toFixed(2);
    }

    getDate(contest) {
        let date = new Date(contest.created);
        return formatDate(date);
    }

    getTotalUsers(contest) {
        return contest.participantCount + contest.judgeCount;
    }

    getTrClass(contest) {
        if (contest.closed) {
            return 'active';
        }
        if (contest.started) {
            return 'success';
        }
        return '';
    }

    getStatus(contest) {
        if (contest.closed) {
            return 'Already closed';
        }
        if (contest.started) {
            return 'Already started';
        }
        return 'Not started yet';
    }

    onRowClick(id) {
        if (typeof this.props.onShowContestEditForm === 'function') {
            this.props.onShowContestEditForm(id);
        }
    }

    renderJoinButton(contest) {
        if (this.props.onJoin && !contest.accepted && !contest.closed) {
            return (
                <button className="btn btn-success" onClick={() => this.props.onJoin(contest.id)}>Join</button>
            );
        }
        return null;
    }

    renderTableBody() {
        return this.props.contests.map((contest, key) => {
            return (
                <tr key={key} onClick={() => this.onRowClick(contest.id)} className={this.getTrClass(contest)}>
                    <td>{contest.name}</td>
                    <td>{this.getCreatorName(contest)}</td>
                    <td>{this.getCost(contest)}</td>
                    <td>{this.getTotalUsers(contest)}</td>
                    <td>{this.getDate(contest)}</td>
                    <td>{this.getStatus(contest)}</td>
                    <td>{this.renderJoinButton(contest)}</td>
                </tr>
            )
        });
    }

    renderContests() {
        return (
            <table className="table table-hover clickable">
                <thead>
                    <tr>
                        <td>Name</td>
                        <td>Creator</td>
                        <td>Prize fund</td>
                        <td>Number of participants</td>
                        <td>Created</td>
                        <td>Status</td>
                        {this.props.onJoin ? <td>Action</td> : null}
                    </tr>
                </thead>
                <tbody>
                    {this.renderTableBody()}
                </tbody>
            </table>
        )
    }

    render() {
        return (
            <div>
                {this.renderContests()}
            </div>
        )
    }
}

export default ContestList;