import React from 'react';
import { formatBytes } from '../../utils.jsx';
/*
    props:
    - author
 */
class AuthorShortInfo extends React.Component {
    getRating() {
        return this.props.author.rating.averageRating.toFixed(2) + ' * ' + this.props.author.rating.userCount;
    }

    getTotalSize() {
        let size = formatBytes(this.props.author.totalSize.totalSize),
            books = this.props.author.totalSize.totalBooks;
        return size + ' / ' + books + ' books';
    }

    render() {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <table className="table">
                        <tbody>
                            <tr>
                                <td>Birthday</td>
                                <td>{this.props.author.birthday}</td>
                            </tr>
                            <tr>
                                <td>City</td>
                                <td>{this.props.author.city}</td>
                            </tr>
                            <tr>
                                <td>Last update</td>
                                <td>{this.props.author.section.lastUpdated}</td>
                            </tr>
                            <tr>
                                <td>Value</td>
                                <td>{this.getTotalSize()}</td>
                            </tr>
                            <tr>
                                <td>Rating</td>
                                <td>{this.getRating()}</td>
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