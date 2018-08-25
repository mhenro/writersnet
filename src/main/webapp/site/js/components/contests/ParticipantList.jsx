import React from 'react';
import ReactStars from 'react-stars';
import {Modal, Button, Tooltip, OverlayTrigger} from 'react-bootstrap';

/*
    props:
        - participants - array of participants and their books
        - participantsOffset - page offset for numeration
        - getRatingDetails - callback(bookId, callback)
        - onSetEstimation - callback(estimationRequest)
*/
class ParticipantList extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            bookRating: []
        }
    }

    updateBookRating(rating) {
        this.setState({
            bookRating: rating.content
        })
    }

    renderTableBody() {
        return this.props.participants.map((participant, key) => {
            let tooltip = (
                <Tooltip id="tooltip">
                    <table className="table table-hover clickable">
                        <thead>
                        <tr>
                            <td>Judge name</td>
                            <td>Estimation</td>
                        </tr>
                        </thead>
                        <tbody>
                        {this.state.bookRating.map((detail, key) => {
                            return (
                                <tr key={key}>
                                    <td>{detail.judgeName}</td>
                                    <td>{detail.estimation}</td>
                                </tr>
                            )
                        })}
                        </tbody>
                    </table>
                </Tooltip>
            );

            return (
                <OverlayTrigger placement="bottom" overlay={tooltip} key={key}>
                    <tr onMouseEnter={() => this.props.getRatingDetails(participant.bookId, rating => this.updateBookRating(rating))}>
                        <td>{this.props.participantsOffset + key + 1}</td>
                        <td>{participant.name}</td>
                        <td width="150px">
                            <ReactStars count={5} size={18} color2={'orange'} edit={false}
                                        value={parseFloat(participant.rating)} className="stars"/>
                            <span className="stars-end">{parseFloat(participant.rating).toFixed(2)}</span>
                        </td>
                        <td>action</td>
                    </tr>
                </OverlayTrigger>
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