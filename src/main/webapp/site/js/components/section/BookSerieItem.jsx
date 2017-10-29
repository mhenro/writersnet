import React from 'react';
import { locale } from '../../locale.jsx';
import { formatBytes } from '../../utils.jsx';

/*
    props:
    - author
    - book
    - registered
    - login
    - onEditBook - callback function
 */
class BookSerieItem extends React.Component {
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

    render() {
        return(
            <div>
                <div className="row">
                    <div className="col-sm-4">
                        <img src="" className="img-rounded" width="200" height="300"/>
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
                                    <td>{formatBytes(this.props.book.size) + ' (? author sheets)'}</td>
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
                                    <td>{this.props.book.genre}</td>
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
                                <button className="btn btn-success btn-block">Remove!</button>
                            </div>
                            <div className={'col-sm-4 ' + (this.props.registered && this.props.login === this.props.author.username ? '' : 'hidden')}>
                                <button onClick={() => this.props.onEditBook(this.props.book)} className="btn btn-success btn-block">Edit</button>
                            </div>
                            <div className="col-sm-4">
                                <button className="btn btn-success btn-block">Read</button>
                            </div>
                        </div>
                    </div>
                </div>
                <hr/>
            </div>
        )
    }
}

export default BookSerieItem;