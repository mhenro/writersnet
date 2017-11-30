import React from 'react';

/*
    props:
    - book
    - author
*/
class TopTable extends React.Component {
    getBigAuthorHeader() {
        return (
            <thead>
                <tr>
                    <th>№</th>
                    <th>Author</th>
                    <th>Rating</th>
                    <th>Views</th>
                    <th>Comments</th>
                    <th>Books</th>
                </tr>
            </thead>
        )
    }

    getSmallAuthorHeader() {
        return (
            <thead>
                <tr>
                    <th>№</th>
                    <th>Author</th>
                    <th>Rating</th>
                </tr>
            </thead>
        )
    }

    getAuthorHeader() {
        if (window.screen.availWidth > 770) {
            return this.getBigAuthorHeader();
        } else {
            return this.getSmallAuthorHeader();
        }
    }

    getBigBookHeader() {
        return (
            <thead>
                <tr>
                    <th>№</th>
                    <th>Novel</th>
                    <th>Rating</th>
                    <th>Views</th>
                    <th>Comments</th>
                </tr>
            </thead>
        )
    }

    getSmallBookHeader() {
        return (
            <thead>
                <tr>
                    <th>№</th>
                    <th>Novel</th>
                    <th>Rating</th>
                </tr>
            </thead>
        )
    }

    getBookHeader() {
        if (window.screen.availWidth > 770) {
            return this.getBigBookHeader();
        } else {
            return this.getSmallBookHeader();
        }
    }


    getTableHeader() {
        if (this.props.author) {
            return this.getAuthorHeader();
        } else if (this.props.book) {
            return this.getBookHeader();
        }
    }

    getTableBody() {
        return (
            <tbody>

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

export default TopTable;