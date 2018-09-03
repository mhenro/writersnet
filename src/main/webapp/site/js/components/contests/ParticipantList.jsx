import React from 'react';
import ReactStars from 'react-stars';
import Select from 'react-select';
import {Modal, Button, Tooltip, OverlayTrigger} from 'react-bootstrap';

/*
    props:
        - participants - array of participants and their books
        - participantsOffset - page offset for numeration
        - getRatingDetails - callback(bookId, callback)
        - onSetEstimation - callback(estimationRequest, callback)
*/
class ParticipantList extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            bookRating: [],
            estimation: {value: 1, label: 1}
        }
    }

    updateBookRating(rating) {
        this.setState({
            bookRating: rating.content
        })
    }

    getComboboxItems() {
        let options = [];
        for (var i = 5; i > 0; i--) {
            options.push({
                value: i,
                label: i
            });
        }
        return options;
    }

    onEstimationChange(estimation) {
        this.setState({
            estimation: estimation
        });
    }

    sendEstimation(bookId) {
        let request = {
            bookId: bookId,
            estimation: this.state.estimation.value
        }
        this.props.onSetEstimation(request, () => this.props.getRatingDetails(bookId, rating => this.updateBookRating(rating)))
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
                        <td>
                            <Select value={this.state.estimation} id="estimation" options={this.getComboboxItems()} onChange={estimation => this.onEstimationChange(estimation)} placeholder="Choose your estimation"/>
                            &nbsp;
                            <button onClick={() => this.sendEstimation(participant.bookId)} className="btn btn-success">Estimate</button>
                        </td>
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