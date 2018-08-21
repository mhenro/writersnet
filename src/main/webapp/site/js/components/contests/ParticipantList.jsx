import React from 'react';
import ReactStars from 'react-stars';

/*
    props:
        - participants - array of participants and their books
        - participantsOffset - page offset for numeration
*/
class ParticipantList extends React.Component {
    renderTableBody() {
        return this.props.participants.map((participant, key) => {
            return (
                <tr key={key}>
                    <td>{this.props.participantsOffset + key + 1}</td>
                    <td>{participant.name}</td>
                    <td width="150px">
                        <ReactStars count={5} size={18} color2={'orange'} edit={false} value={parseFloat(participant.rating).toFixed(2)} className="stars"/>
                        <span className="stars-end">{parseFloat(participant.rating).toFixed(2)}</span>
                    </td>
                    <td>action</td>
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