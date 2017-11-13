import React from 'react';
import BookBriefItem from './BookBriefItem.jsx';

/*
    props:
    - books
    - language
 */
class BookBriefList extends React.Component {
    render() {
        return (
            <div>
                {this.props.books.map((book, key) => <BookBriefItem key={key} book={book} language={this.props.language}/>)}
            </div>
        )
    }
}

export default BookBriefList;