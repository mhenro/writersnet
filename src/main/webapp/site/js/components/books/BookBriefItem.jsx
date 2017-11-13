import React from 'react';
import { Link } from 'react-router-dom';
import {getLocale} from '../../locale.jsx';
import {formatDate} from '../../utils.jsx';

/*
    props:
    - book
    - language
 */
class BookBriefItem extends React.Component {
    getName() {
        let name = this.props.book.name;
        return name;
    }

    getAuthor() {
        let author = this.props.book.author;
        return (
            <Link to={'/authors/' + author.username}>{author.firstName + ' ' + author.lastName}</Link>
        )
    }

    getGenre() {
        let genre = getLocale(this.props.language)[this.props.book.genre];
        return genre;
    }

    getDescription() {
        let description = this.props.book.description;
        return description;
    }

    getCreated() {
        let created = new Date(this.props.book.created);
        return created.getFullYear();   //formatDate(created, 'D-M-Y');
    }

    render() {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <div className="row">
                        <div className="col-sm-3">
                            <img src={this.props.book.cover + '?date=' + new Date()} className="img-rounded" width="100%" height="auto"/>
                        </div>
                        <div className="col-sm-6">
                            <table className="table">
                                <tbody>
                                    <tr>
                                        <td>Название</td>
                                        <td>{this.getName()}</td>
                                    </tr>
                                    <tr>
                                        <td>Автор</td>
                                        <td>{this.getAuthor()}</td>
                                    </tr>
                                    <tr>
                                        <td>Жанр</td>
                                        <td>{this.getGenre()}</td>
                                    </tr>
                                    <tr>
                                        <td>Описание</td>
                                        <td>{this.getDescription()}</td>
                                    </tr>
                                    <tr>
                                        <td>Год</td>
                                        <td>{this.getCreated()}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div className="col-sm-3">
                            <img src={this.props.book.author.avatar + '?date=' + new Date()} className="img-rounded" width="100%" height="auto"/>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-12">
                        <div className="btn-toolbar" style={{margin: '0 0 10px 10px'}}>
                            <Link to={'/reader/' + this.props.book.id} className="btn btn-success btn-sm">Read</Link>
                            <Link to={'/authors/' + this.props.book.author.username} className="btn btn-success btn-sm">Go to writer</Link>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default BookBriefItem;