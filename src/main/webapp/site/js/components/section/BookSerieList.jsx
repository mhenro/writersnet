import React from 'react';
import BookSerieItem from './BookSerieItem.jsx';
import { getLocale } from '../../locale.jsx';

/*
    props:
    - author
    - series - array of series
    - books
    - registered
    - login - user id
    - onEditBook - callback function
    - onAddReview - callback
    - onDeleteBook - callback
    - language
    - token
    - onGoToComments - callback function
 */
class BookSerieList extends React.Component {
    getBooksForSerie(serie) {
        return this.props.books.filter(book => {
            if (serie && !book.bookSerie) {
                return false;
            }
            if (serie) {
                return book.bookSerie.id == serie.id;
            } else {
                return book.bookSerie === null;
            }
        });
    }

    isBookWithoutSerieExist() {
        return this.props.books.some(book => book.bookSerie === null);
    }

    render() {
        return (
            <div>
                {this.props.series.map((serie, key) => {
                    return (
                        <div className="row" key={key}>
                            <div className="col-sm-12">
                                <div className="panel panel-default">
                                    <div className="panel-heading">
                                        {serie.name}
                                    </div>
                                    <div className="panel-body">
                                        {this.getBooksForSerie(serie).map((book, key) => {
                                            return (
                                                <BookSerieItem key={key}
                                                               book={book}
                                                               registered={this.props.registered}
                                                               login={this.props.login}
                                                               author={this.props.author}
                                                               onEditBook={this.props.onEditBook}
                                                               onAddReview={this.props.onAddReview}
                                                               onDeleteBook={this.props.onDeleteBook}
                                                               token={this.props.token}
                                                               onGoToComments={this.props.onGoToComments}
                                                               language={this.props.language}/>
                                            )
                                        })}
                                    </div>
                                </div>
                            </div>
                        </div>
                    )
                })}
                {this.isBookWithoutSerieExist() ?
                    <div className="row">
                        <div className="col-sm-12">
                            <div className="panel panel-default">
                                <div className="panel-heading">
                                    {getLocale(this.props.language)['Books without series']}
                                </div>
                                <div className="panel-body">
                                    {this.getBooksForSerie(null).map((book, key) => {
                                        return (
                                            <BookSerieItem key={key}
                                                           book={book}
                                                           registered={this.props.registered}
                                                           login={this.props.login}
                                                           author={this.props.author}
                                                           onEditBook={this.props.onEditBook}
                                                           onDeleteBook={this.props.onDeleteBook}
                                                           token={this.props.token}
                                                           language={this.props.language}/>
                                        )
                                    })}
                                </div>
                            </div>
                        </div>
                    </div> : null
                }
            </div>
        )
    }
}

export default BookSerieList;