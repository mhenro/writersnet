import React from 'react';
import ReactStars from 'react-stars';
import Select from 'react-select';
import {Modal, Button, Tooltip, OverlayTrigger} from 'react-bootstrap';
import { clone } from '../../utils.jsx';

/*
    props:
        - participants - array of participants and their books
        - participantsOffset - page offset for numeration
        - getRatingDetails - callback(bookId, callback)
        - onSetEstimation - callback(estimationRequest, callback)
        - login - login of the current user
*/
class ParticipantList extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            bookRating: [],
            estimation: []
        }
        for (let i = 0; i < 100; i++) {
            this.state.estimation.push({value: 1, label: 1})
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

    onEstimationChange(key, estimation) {
        let newEstim = clone(this.state.estimation)
        newEstim[key] = estimation
        this.setState({
            estimation: newEstim
        });
    }

    sendEstimation(key, bookId) {
        let request = {
            bookId: bookId,
            estimation: this.state.estimation[key].value
        }
        this.props.onSetEstimation(request, () => this.props.getRatingDetails(bookId, rating => this.updateBookRating(rating)))
    }

    isParticipant(participantId) {
        return this.props.login === participantId
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

            let estimateButtonStyle = this.isParticipant(participant.participantId) ? {clear: 'both', display: 'none'} : {clear: 'both'}

            return (
                <OverlayTrigger placement="left" overlay={tooltip} key={key}>
                    <tr onMouseEnter={() => this.props.getRatingDetails(participant.bookId, rating => this.updateBookRating(rating))}>
                        <td width="10px">{this.props.participantsOffset + key + 1}</td>
                        <td>{participant.name}</td>
                        <td width="150px">
                            <ReactStars count={5} size={18} color2={'orange'} edit={false}
                                        value={parseFloat(participant.rating)} className="stars"/>
                            <span className="stars-end">{parseFloat(participant.rating).toFixed(2)}</span>
                        </td>
                        <td width="200px">
                            <div width="150px">
                                <Select value={this.state.estimation[key]}
                                        id="estimation"
                                        options={this.getComboboxItems()}
                                        onChange={estimation => this.onEstimationChange(key, estimation)}
                                        placeholder="Choose your estimation"
                                        wrapperStyle={{width: '30px', float: 'left'}}
                                />
                                &nbsp;
                                <button style={estimateButtonStyle} onClick={() => this.sendEstimation(key, participant.bookId)} className="btn btn-success">Estimate</button>
                            </div>
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