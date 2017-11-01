import React from 'react';
import { Link } from 'react-router-dom';
import { Modal, Button } from 'react-bootstrap';
import { locale, getLocale } from '../../locale.jsx';
import { formatBytes } from '../../utils.jsx';

/*
    props:
    - author
    - book
    - registered
    - login
    - onEditBook - callback function
    - onDeleteBook - callback
    - language
    - token
 */
class BookSerieItem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            confirmDialogShow: false
        };
    }

    getRating() {
        let ratings = this.props.book.rating;
        if (ratings && ratings.length > 0) {
            let sum = ratings.map(rating => rating.ratingId.estimation * rating.userCount).reduce((prev, cur) => prev + cur),
                totalVotes = ratings.map(rating => rating.userCount).reduce((prev, cur) => prev + cur),
                avgEstimation = parseFloat(sum / totalVotes).toFixed(2);
            if (totalVotes > 0) {
                return avgEstimation + ' * ' + totalVotes;
            }
        }
        return '0.00 * 0';
    }

    getAuthorLists() {
        return parseInt(this.props.book.size / 40000);
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

    render() {
        return(
            <div>
                <div className="row">
                    <div className="col-sm-4">
                        <img src={this.props.book.cover + '?date=' + new Date()} className="img-rounded" width="200" height="300"/>
                    </div>
                    <div className="col-sm-8">
                        <div className="book-item-name">
                            {this.props.book.name}
                        </div>
                        <div>
                            <span className="glyphicon glyphicon-heart"></span>&nbsp;
                            {this.getRating()}
                        </div>
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
                                    <td>{this.props.book.created}</td>
                                </tr>
                                <tr>
                                    <td>Last update</td>
                                    <td>{this.props.book.lastUpdate}</td>
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
                        13000 views | 775 comments | 20 reviews
                        <hr/>
                        <div className="row">
                            <div className={'col-sm-4 ' + (this.props.registered && this.props.login === this.props.author.username ? '' : 'hidden')}>
                                <button onClick={() => this.onConfirm()} className="btn btn-danger btn-block">Remove</button>
                            </div>
                            <div className={'col-sm-4 ' + (this.props.registered && this.props.login === this.props.author.username ? '' : 'hidden')}>
                                <button onClick={() => this.props.onEditBook(this.props.book)} className="btn btn-success btn-block">Edit</button>
                            </div>
                            <div className="col-sm-4">
                                <Link to={'/reader/' + this.props.book.id} className="btn btn-success btn-block">Read</Link>
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