import React from 'react';

/*
    props:
    - author
    - book
    - registered
    - login
 */
class BookSerieItem extends React.Component {
    render() {
        return(
            <div>
                <div className="row">
                    <div className="col-sm-4">
                        <img src="" className="img-rounded" width="200" height="300"/>
                    </div>
                    <div className="col-sm-8">
                        <div className="book-item-name">
                            {this.props.book.name}
                        </div>
                        <div>
                            <span className="glyphicon glyphicon-heart"></span>&nbsp;
                            4.95 * 145 votes
                        </div>
                        <div>
                            {this.props.book.description}
                        </div>
                        <br/>
                        <table className="table borderless">
                            <tbody>
                                <tr>
                                    <td>Size</td>
                                    <td>15 Mb (12 author sheets)</td>
                                </tr>
                                <tr>
                                    <td>Created date</td>
                                    <td>15.05.2016</td>
                                </tr>
                                <tr>
                                    <td>Last update</td>
                                    <td>16.05.2017</td>
                                </tr>
                            </tbody>
                        </table>
                        <hr/>
                        13000 views | 775 comments | 20 reviews
                        <hr/>
                        <div className="row">
                            <div className={'col-sm-4 ' + (this.props.registered && this.props.login === this.props.author.username ? '' : 'hidden')}>
                                <button className="btn btn-success btn-block">Remove!</button>
                            </div>
                            <div className={'col-sm-4 ' + (this.props.registered && this.props.login === this.props.author.username ? '' : 'hidden')}>
                                <button className="btn btn-success btn-block">Edit</button>
                            </div>
                            <div className="col-sm-4">
                                <button className="btn btn-success btn-block">Read</button>
                            </div>
                        </div>
                    </div>
                </div>
                <hr/>
            </div>
        )
    }
}

export default BookSerieItem;