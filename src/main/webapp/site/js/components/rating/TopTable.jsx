import React from 'react';

/*
    props:
    - book
    - author
*/
class TopTable extends React.Component {
    getTableHeader() {
        if (this.props.author) {
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
        } else if (this.props.book) {
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