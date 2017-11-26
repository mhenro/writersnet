import React from 'react';
import { formatBytes, formatDate } from '../../utils.jsx';
import ReactStars from 'react-stars';
import { getLocale } from '../../locale.jsx';
/*
    props:
    - author
 */
class AuthorShortInfo extends React.Component {
    getBirthday() {
        let date = new Date(this.props.author.birthday);
        return formatDate(date, 'D-M-Y');
    }

    getCity() {
        let city = this.props.author.city;
        return city;
    }

    getLastUpdated() {
        let date = new Date(this.props.author.section.lastUpdated);
        return formatDate(date, 'D-M-Y');
    }

    getPreferredLanguages() {
        if (!this.props.author.preferredLanguages) {
            return '';
        }
        let array = this.props.author.preferredLanguages.split(';'),
            result = array.map(lang => getLocale(lang).label).join(', ');

        return result;
    }

    getTotalSize() {
        let size = formatBytes(this.props.author.totalSize.totalSize),
            books = this.props.author.totalSize.totalBooks;
        return size + ' / ' + books + ' books';
    }

    getAverageRating() {
        return parseFloat(this.props.author.rating.averageRating.toFixed(2));
    }

    getTotalViews() {
        return this.props.author.views;
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
                                <td>{this.getCity()}</td>
                            </tr>
                            <tr>
                                <td>Last update</td>
                                <td>{this.getLastUpdated()}</td>
                            </tr>
                            <tr>
                                <td>Preferred languages</td>
                                <td>{this.getPreferredLanguages()}</td>
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
                                <td>{this.getTotalViews()}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}

export default AuthorShortInfo;