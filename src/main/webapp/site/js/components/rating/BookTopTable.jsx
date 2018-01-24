import React from 'react';
import ReactStars from 'react-stars';
import { Link } from 'react-router-dom';
import { formatDate, formatBytes } from '../../utils.jsx';
import { getLocale } from '../../locale.jsx';

/*
    props:
    - books
    - top - name of the top
    - language
 */
class BookTopTable extends React.Component {
    getBookNoveltyHeader() {
        return (
            <thead>
            <tr>
                <th>#</th>
                <th>{getLocale(this.props.language)['Novel']}</th>
                <th>{getLocale(this.props.language)['Last update']}</th>
            </tr>
            </thead>
        )
    }

    getBookRatingHeader() {
        return (
            <thead>
            <tr>
                <th>#</th>
                <th>{getLocale(this.props.language)['Novel']}</th>
                <th>{getLocale(this.props.language)['Rating']}</th>
                <th>{getLocale(this.props.language)['Total votes']}</th>
            </tr>
            </thead>
        )
    }

    getBookVolumeHeader() {
        return (
            <thead>
            <tr>
                <th>#</th>
                <th>{getLocale(this.props.language)['Novel']}</th>
                <th>{getLocale(this.props.language)['Volume']}</th>
            </tr>
            </thead>
        )
    }

    getBookCommentsHeader() {
        return (
            <thead>
            <tr>
                <th>#</th>
                <th>{getLocale(this.props.language)['Novel']}</th>
                <th>{getLocale(this.props.language)['Comments count']}</th>
            </tr>
            </thead>
        )
    }

    getBookViewsHeader() {
        return (
            <thead>
            <tr>
                <th>#</th>
                <th>{getLocale(this.props.language)['Novel']}</th>
                <th>{getLocale(this.props.language)['Views']}</th>
            </tr>
            </thead>
        )
    }

    getTableHeader() {
        switch(this.props.top) {
            case 'booksTopByNovelty': return this.getBookNoveltyHeader(); break;
            case 'booksTopByRating': return this.getBookRatingHeader(); break;
            case 'booksTopByNovelVolume': return this.getBookVolumeHeader(); break;
            case 'booksTopByCommentsCount': return this.getBookCommentsHeader(); break;
            case 'booksTopByViewsCount': return this.getBookViewsHeader(); break;
        }
    }

    getAverageRating(book, fixed) {
        let estimation = book.totalRating,
            votes = book.count;

        if (votes == 0) {
            return fixed ? parseFloat(0).toFixed(2) : parseFloat(0);
        }
        return fixed ? parseFloat(estimation / votes).toFixed(2) : parseFloat(estimation / votes);
    }

    getBookLink(book) {
        return (
            <Link to={'/reader/' + book.id}>{book.name}</Link>
        )
    }

    getBookNoveltyTableColumns(book, key, row) {
        let column = (
            <tr key={key}>
                <th>{row}</th>
                <th>{this.getBookLink(book)}</th>
                <th>{formatDate(new Date(book.lastUpdate), 'D-M-Y h:m:s')}</th>
            </tr>
        );
        return column;
    }

    getBookRatingTableColumns(book, key, row) {
        let column = (
            <tr key={key}>
                <th>{row}</th>
                <th>{this.getBookLink(book)}</th>
                <th><ReactStars count={5} size={18} color2={'orange'} edit={false} value={this.getAverageRating(book, false)} className="stars"/> {this.getAverageRating(book, true)}</th>
                <th>{book.count}</th>
            </tr>
        );
        return column;
    }

    getBookVolumeTableColumns(book, key, row) {
        let column = (
            <tr key={key}>
                <th>{row}</th>
                <th>{this.getBookLink(book)}</th>
                <th>{formatBytes(book.volume)}</th>
            </tr>
        );
        return column;
    }

    getBookCommentsTableColumns(book, key, row) {
        let column = (
            <tr key={key}>
                <th>{row}</th>
                <th>{this.getBookLink(book)}</th>
                <th>{book.count}</th>
            </tr>
        );
        return column;
    }

    getBookViewsTableColumns(book, key, row) {
        let column = (
            <tr key={key}>
                <th>{row}</th>
                <th>{this.getBookLink(book)}</th>
                <th>{book.views}</th>
            </tr>
        );
        return column;
    }

    getTableBody() {
        let pageParams = this.props.books,
            offset = pageParams.number * pageParams.size,
            row = offset;
        return (
            <tbody>
            {
                this.props.books.content.map((book, key) => {
                    ++row;
                    switch(this.props.top) {
                        case 'booksTopByNovelty': return this.getBookNoveltyTableColumns(book, key, row); break;
                        case 'booksTopByRating': return this.getBookRatingTableColumns(book, key, row); break;
                        case 'booksTopByNovelVolume': return this.getBookVolumeTableColumns(book, key, row); break;
                        case 'booksTopByCommentsCount': return this.getBookCommentsTableColumns(book, key, row); break;
                        case 'booksTopByViewsCount': return this.getBookViewsTableColumns(book, key, row); break;
                    }
                })
            }
            </tbody>
        )
    }

    render() {
        return (
            <div>
                <table className="table table-hover">
                    {this.getTableHeader()}
                    {this.getTableBody()}
                </table>
            </div>
        )
    }
}

export default BookTopTable;