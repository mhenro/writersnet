import React from 'react';
import { formatBytes, formatDate } from '../../utils.jsx';
import ReactStars from 'react-stars';
import { getLocale } from '../../locale.jsx';
/*
    props:
    - author
    - books - array
    - language
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
        let totalSize = this.props.books.length > 0 ? this.props.books.map(book => book.size).reduce((a, b) => a + b) : 0,
            formatSize = formatBytes(totalSize),
            bookCount = this.props.books.length;
        return formatSize + ' / ' + bookCount + ' ' + getLocale(this.props.language)['books'];
    }

    getAverageRating() {
        return parseFloat(this.props.author.rating.averageRating.toFixed(2));
    }

    getTotalViews() {
        return this.props.author.views;
    }

    getComplaintCount() {
        let color = 'green';
        if (this.props.author.complaints > 0) color = 'yellow';
        if (this.props.author.complaints > 5) color = 'red';
        return (
            <span style={{color: color, fontWeight: 'bold'}}>{this.props.author.complaints}</span>
        )
    }

    render() {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <table className="table">
                        <tbody>
                            <tr>
                                <td>{getLocale(this.props.language)['Birthday']}</td>
                                <td>{this.getBirthday()}</td>
                            </tr>
                            <tr>
                                <td>{getLocale(this.props.language)['City']}</td>
                                <td>{this.getCity()}</td>
                            </tr>
                            <tr>
                                <td>{getLocale(this.props.language)['Last update']}</td>
                                <td>{this.getLastUpdated()}</td>
                            </tr>
                            <tr>
                                <td>{getLocale(this.props.language)['Preferred languages']}</td>
                                <td>{this.getPreferredLanguages()}</td>
                            </tr>
                            <tr>
                                <td>{getLocale(this.props.language)['Value']}</td>
                                <td>{this.getTotalSize()}</td>
                            </tr>
                            <tr>
                                <td>{getLocale(this.props.language)['Rating']}</td>
                                <td>
                                    <ReactStars count={5} size={18} color2={'orange'} edit={false} value={this.getAverageRating()} className="stars"/>
                                    <span className="stars-end"><b>{this.props.author.rating.averageRating.toFixed(2) + ' * ' + this.props.author.rating.userCount}</b></span>
                                </td>
                            </tr>
                            <tr>
                                <td>Complaint count</td>
                                <td>{this.getComplaintCount()}</td>
                            </tr>
                            <tr>
                                <td>{getLocale(this.props.language)['Visitors']}</td>
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