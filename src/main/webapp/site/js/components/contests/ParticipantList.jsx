import React from 'react';

/*
    props:
        - participants - array of participants and their books
*/
class ParticipantList extends React.Component {
    renderTableBody() {
        return this.props.participants.map((participant, key) => {
            return (
                <tr key={key}>
                    <td>{participant.name}</td>
                    <td>{this.getCreatorName(participant)}</td>
                    <td>{this.getCost(participant)}</td>
                    <td>{this.getTotalUsers(participant)}</td>
                    <td>{this.getDate(participant)}</td>
                    <td>{this.getStatus(participant)}</td>
                    <td>{this.renderActionButton(participant)}</td>
                </tr>
            )
        });
    }

    renderParticipants() {
        return (
            <table className="table table-hover clickable">
                <thead>
                    <tr>
                        <td>Place</td>
                        <td>Name</td>
                        <td>Rating</td>
                        <td>Action</td>
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
                {this.renderParticipants()}
            </div>
        )
    }
}

export default ParticipantList;