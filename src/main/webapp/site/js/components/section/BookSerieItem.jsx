import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { Modal, Button } from 'react-bootstrap';
import { locale, getLocale } from '../../locale.jsx';
import { formatBytes, formatDate } from '../../utils.jsx';

import ReactStars from 'react-stars';

/*
    props:
    - author
    - book
    - registered
    - login
    - onEditBook - callback function
    - onAddReview - callback
    - onDeleteBook - callback
    - language
    - token
    - onGoToComments - callback function
 */
class BookSerieItem extends React.Component {
    static contextTypes = {
        router: PropTypes.shape({
            history: PropTypes.shape({
                push: PropTypes.func.isRequired,
                replace: PropTypes.func.isRequired
            }).isRequired,
            staticContext: PropTypes.object
        }).isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            confirmDialogShow: false
        };
    }

    getAverageRating() {
        return parseFloat(this.props.book.totalRating.averageRating.toFixed(2));
    }

    getCreated() {
        let date = new Date(this.props.book.created);
        return formatDate(date, 'D-M-Y');
    }

    getLastUpdated() {
        let date = new Date(this.props.book.lastUpdate);
        return formatDate(date, 'D-M-Y');
    }

    getTotalVotes() {
        return this.props.book.totalRating.userCount;
    }

    getAuthorLists() {
        return parseInt(this.props.book.size / 40000);
    }

    renderCounters() {
        return (
            <div>
                {this.props.book.views} views |&nbsp;
                <Link to={'/reader/' + this.props.book.id} onClick={() => this.props.onGoToComments(true)}>{this.props.book.commentsCount} comments</Link> |&nbsp;
                <Link to={'/reader/' + this.props.book.id} onClick={() => this.props.onGoToComments(true)}>{this.props.book.reviewCount} reviews</Link>
            </div>
        )
    }

    onConfirm() {
        this.setState({
            confirmDialogShow: true
        });
    }

    onDelete() {
        this.props.onDeleteBook(this.props.book.id, this.props.token);
        this.onCancel();
    }

    onCancel() {
        this.setState({
            confirmDialogShow: false
        });
    }

    onBookClick() {
        this.context.router.history.push('/reader/' + this.props.book.id);
    }

    render() {
        return(
            <div>
                <div className="row">
                    <div className="col-sm-12 col-lg-4">
                        <img src={this.props.book.cover /*+ '?date=' + new Date()*/} onClick={() => this.onBookClick()} className="img-rounded clickable" width="200" height="auto"/>
                    </div>
                    <div className="col-sm-12 col-lg-8">
                        <div className="book-item-name">
                            {this.props.book.name}
                        </div>
                        <div>
                            <ReactStars count={5} size={18} color2={'orange'} edit={false} value={this.getAverageRating()} className="stars"/>
                            <span><b>{this.getAverageRating() + ' * ' + this.getTotalVotes()}</b></span>
                        </div>
                        <br/>
                        <div>
                            {this.props.book.description}
                        </div>
                        <br/>
                        <table className="table borderless">
                            <tbody>
                                <tr>
                                    <td>Size</td>
                                    <td>{formatBytes(this.props.book.size) + ' (' + this.getAuthorLists() + ' author sheets)'}</td>
                                </tr>
                                <tr>
                                    <td>Created date</td>
                                    <td>{this.getCreated()}</td>
                                </tr>
                                <tr>
                                    <td>Last update</td>
                                    <td>{this.getLastUpdated()}</td>
                                </tr>
                                <tr>
                                    <td>Genre</td>
                                    <td>{getLocale(this.props.language)[this.props.book.genre]}</td>
                                </tr>
                                <tr>
                                    <td>Language</td>
                                    <td>{locale[this.props.book.language || 'EN'].label}</td>
                                </tr>
                            </tbody>
                        </table>
                        <hr/>
                        {this.renderCounters()}
                        <hr/>
                        <div className="row">
                            <div className={'col-sm-12 col-md-3 ' + (this.props.registered && this.props.login === this.props.author.username ? '' : 'hidden')}>
                                <button onClick={() => this.onConfirm()} className="btn btn-danger btn-block">Remove</button>
                            </div>
                            <div className={'col-sm-12 col-md-3 ' + (this.props.registered && this.props.login === this.props.author.username ? '' : 'hidden')}>
                                <button onClick={() => this.props.onEditBook(this.props.book)} className="btn btn-success btn-block">Edit</button>
                            </div>
                            <div className="col-sm-12 col-md-3">
                                <Link to={'/reader/' + this.props.book.id} className="btn btn-success btn-block">Read</Link>
                            </div>
                            <div className={'col-sm-12 col-md-3 ' + (this.props.registered ? '' : 'hidden')}>
                                <button onClick={() => this.props.onAddReview(this.props.book)} className="btn btn-success btn-block">Add review</button>
                            </div>
                        </div>
                    </div>
                </div>
                <hr/>

                {/* delete confirmation dialog */}
                <Modal show={this.state.confirmDialogShow} onHide={() => this.onCancel()}>
                    <Modal.Header>
                        Attention!
                    </Modal.Header>
                    <Modal.Body>
                        {'Are you sure you want to delete "' + this.props.book.name + '"?'}
                    </Modal.Body>
                    <Modal.Footer>
                        <div className="btn-group">
                            <Button onClick={() => this.onDelete()} className="btn btn-danger">Delete</Button>
                            <Button onClick={() => this.onCancel()} className="btn btn-default">Cancel</Button>
                        </div>
                    </Modal.Footer>
                </Modal>
            </div>
        )
    }
}

export default BookSerieItem;