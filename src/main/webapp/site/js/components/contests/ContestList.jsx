import React from 'react';
import { Link } from 'react-router-dom';

import { formatDate } from '../../utils.jsx';

/*
    props:
    - contests - array
    - onShowContestEditForm - callback
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

    onRowClick(id) {
        this.props.onShowContestEditForm(id);
    }

    renderTableBody() {
        return this.props.contests.map((contest, key) => {
            return (
                <tr key={key} onClick={() => this.onRowClick(contest.id)}>
                    <td>{contest.name}</td>
                    <td>{this.getCreatorName(contest)}</td>
                    <td>{this.getCost(contest)}</td>
                    <td>???</td>
                    <td>{this.getDate(contest)}</td>
                    <td>???</td>
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
                        <td>Edit</td>
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