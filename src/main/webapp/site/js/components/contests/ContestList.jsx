import React from 'react';
import { Link } from 'react-router-dom';

import { formatDate } from '../../utils.jsx';

/*
    props:
    - contests - array
    - onShowContestEditForm - callback
    - onJoin - callback
    - onRefuse - callback
    - onExit - callback
 */
class ContestList extends React.Component {
    getCreatorName(contest) {
        return <Link to={'/authors/' + contest.creatorId}>{contest.creatorFullName}</Link>
    }

    getBookName(contest) {
        return <Link to={'/reader/' + contest.bookId}>{contest.bookName}</Link>
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

    isRenderBook() {
        return this.props.contests.find(c => c.bookId !== -1 && c.bookName && c.bookName.length > 0);
    }

    renderActionButton(contest) {
        if (!this.props.onJoin || contest.closed || contest.accepted === null) {
            return null;
        }
        if (!contest.accepted) {
            return (
                <div className="btn-group">
                    <button className="btn btn-success btn-sm" onClick={() => this.props.onJoin(contest.id, contest.bookId)}>Join</button>
                    <button className="btn btn-danger btn-sm" onClick={() => this.props.onExit(contest.id, contest.bookId)}>Exit</button>
                </div>
            );
        } else {
            return (
                <div className="btn-group">
                    <button className="btn btn-warning btn-sm" onClick={() => this.props.onRefuse(contest.id, contest.bookId)}>Refuse</button>
                    <button className="btn btn-danger btn-sm" onClick={() => this.props.onExit(contest.id, contest.bookId)}>Exit</button>
                </div>
            );
        }
    }

    renderTableBody() {
        return this.props.contests.map((contest, key) => {
            return (
                <tr key={key} onClick={() => this.onRowClick(contest.id)} className={this.getTrClass(contest)}>
                    <td>{contest.name}</td>
                    {this.isRenderBook() ? <td>{this.getBookName(contest)}</td> : null}
                    <td>{this.getCreatorName(contest)}</td>
                    <td>{this.getCost(contest)}</td>
                    <td>{this.getTotalUsers(contest)}</td>
                    <td>{this.getDate(contest)}</td>
                    <td>{this.getStatus(contest)}</td>
                    <td>{this.renderActionButton(contest)}</td>
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
                        {this.isRenderBook() ? <td>Book</td> : null}
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