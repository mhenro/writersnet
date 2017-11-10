import React from 'react';
import { formatBytes, formatDate } from '../../utils.jsx';
import ReactStars from 'react-stars';
/*
    props:
    - author
 */
class AuthorShortInfo extends React.Component {
    getBirthday() {
        let date = new Date(this.props.author.birthday);
        return formatDate(date, 'D-M-Y');
    }

    getLastUpdated() {
        let date = new Date(this.props.author.section.lastUpdated);
        return formatDate(date, 'D-M-Y');
    }

    getTotalSize() {
        let size = formatBytes(this.props.author.totalSize.totalSize),
            books = this.props.author.totalSize.totalBooks;
        return size + ' / ' + books + ' books';
    }

    getAverageRating() {
        return parseFloat(this.props.author.rating.averageRating.toFixed(2));
    }

    render() {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <table className="table">
                        <tbody>
                            <tr>
                                <td>Birthday</td>
                                <td>{this.getBirthday()}</td>
                            </tr>
                            <tr>
                                <td>City</td>
                                <td>{this.props.author.city}</td>
                            </tr>
                            <tr>
                                <td>Last update</td>
                                <td>{this.getLastUpdated()}</td>
                            </tr>
                            <tr>
                                <td>Value</td>
                                <td>{this.getTotalSize()}</td>
                            </tr>
                            <tr>
                                <td>Rating</td>
                                <td>
                                    <ReactStars count={5} size={18} color2={'orange'} edit={false} value={this.getAverageRating()} className="stars"/>
                                    <span className="stars-end"><b>{this.props.author.rating.averageRating.toFixed(2) + ' * ' + this.props.author.rating.userCount}</b></span>
                                </td>
                            </tr>
                            <tr>
                                <td>Visitors</td>
                                <td>{this.props.author.section.visitors}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}

export default AuthorShortInfo;