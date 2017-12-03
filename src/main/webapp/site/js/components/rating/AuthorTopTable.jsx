import React from 'react';
import ReactStars from 'react-stars';
import { Link } from 'react-router-dom';

/*
    props:
    - authors
    - top - name of the top
 */
class AuthorTopTable extends React.Component {
    getAuthorRatingHeader() {
        return (
            <thead>
            <tr>
                <th>#</th>
                <th>Author</th>
                <th>Rating</th>
                <th>Total votes</th>
            </tr>
            </thead>
        )
    }

    getAuthorBookCountHeader() {
        return (
            <thead>
            <tr>
                <th>#</th>
                <th>Author</th>
                <th>Novels count</th>
            </tr>
            </thead>
        )
    }

    getAuthorCommentsHeader() {
        return (
            <thead>
            <tr>
                <th>#</th>
                <th>Author</th>
                <th>Comments count</th>
            </tr>
            </thead>
        )
    }

    getAuthorViewsHeader() {
        return (
            <thead>
            <tr>
                <th>#</th>
                <th>Author</th>
                <th>Views</th>
            </tr>
            </thead>
        )
    }

    getTableHeader() {
        switch(this.props.top) {
            case 'authorsTopByRating': return this.getAuthorRatingHeader(); break;
            case 'authorsTopByNovelsCount': return this.getAuthorBookCountHeader(); break;
            case 'authorsTopByCommentsCount': return this.getAuthorCommentsHeader(); break;
            case 'authorsTopByViewsCount': return this.getAuthorViewsHeader(); break;
        }
    }

    getAverageRating(author, fixed) {
        let estimation = author.totalEstimation,
            votes = author.totalVotes;

        if (votes == 0) {
            return fixed ? parseFloat(0).toFixed(2) : parseFloat(0);
        }
        return fixed ? parseFloat(estimation / votes).toFixed(2) : parseFloat(estimation / votes);
    }

    getAuthorLink(author) {
        return (
            <Link to={'/authors/' + author.username}>{author.firstName + ' ' + author.lastName}</Link>
        )
    }

    getAuthorRatingTableColumns(author, key, row) {
        let column = (
            <tr key={key}>
                <th>{row}</th>
                <th>{this.getAuthorLink(author)}</th>
                <th><ReactStars count={5} size={18} color2={'orange'} edit={false} value={this.getAverageRating(author, false)} className="stars"/> {this.getAverageRating(author, true)}</th>
                <th>{author.totalVotes}</th>
            </tr>
        );
        return column;
    }

    getAuthorBookCountTableColumns(author, key, row) {
        let column = (
            <tr key={key}>
                <th>{row}</th>
                <th>{this.getAuthorLink(author)}</th>
                <th>{author.bookCount}</th>
            </tr>
        );
        return column;
    }

    getAuthorCommentsTableColumns(author, key, row) {
        let column = (
            <tr key={key}>
                <th>{row}</th>
                <th>{this.getAuthorLink(author)}</th>
                <th>{author.commentsCount}</th>
            </tr>
        );
        return column;
    }

    getAuthorViewsTableColumns(author, key, row) {
        let column = (
            <tr key={key}>
                <th>{row}</th>
                <th>{this.getAuthorLink(author)}</th>
                <th>{author.views}</th>
            </tr>
        );
        return column;
    }

    getTableBody() {
        let pageParams = this.props.authors,
            offset = pageParams.number * pageParams.size,
            row = offset;
        return (
            <tbody>
            {
                this.props.authors.content.map((author, key) => {
                    ++row;
                    switch(this.props.top) {
                        case 'authorsTopByRating': return this.getAuthorRatingTableColumns(author, key, row); break;
                        case 'authorsTopByNovelsCount': return this.getAuthorBookCountTableColumns(author, key, row); break;
                        case 'authorsTopByCommentsCount': return this.getAuthorCommentsTableColumns(author, key, row); break;
                        case 'authorsTopByViewsCount': return this.getAuthorViewsTableColumns(author, key, row); break;
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

export default AuthorTopTable;