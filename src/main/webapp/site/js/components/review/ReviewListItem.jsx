import React from 'react';
import PropTypes from 'prop-types'
import { Link } from 'react-router-dom';

/*
    props:
    - review - object
*/
class ReviewListItem extends React.Component {
    static contextTypes = {
        router: PropTypes.shape({
            history: PropTypes.shape({
                push: PropTypes.func.isRequired,
                replace: PropTypes.func.isRequired
            }).isRequired,
            staticContext: PropTypes.object
        }).isRequired
    };

    getReviewName() {
        return this.props.review.name;
    }

    getBookCover() {
        return <img src={this.props.review.bookCover} onClick={() => this.onBookClick()} className="img-rounded clickable" width="100%" height="auto"/>
    }

    getBookName() {
        return <Link to={'/reader/' + this.props.review.bookId}>{this.props.review.bookName}</Link>;
    }

    getAuthorName() {
        return <Link to={'/authors/' + this.props.review.authorId}>{this.props.review.authorFullName}</Link>;
    }

    getScore() {
        return this.props.review.score;
    }

    onBookClick() {
        this.context.router.history.push('/reader/' + this.props.review.bookId);
    }

    render() {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <div className="col-sm-12">
                        <div className="col-sm-3">
                            <div className="col-sm-12">
                                {this.getBookCover()}
                                <br/>
                            </div>
                            <div className="col-sm-12 text-center">
                                {this.getBookName()}
                            </div>
                        </div>
                        <div className="col-sm-9">
                            <table className="table">
                                <tbody>
                                    <tr>
                                        <td>Name</td>
                                        <td>{this.getReviewName()}</td>
                                    </tr>
                                    <tr>
                                        <td>Author</td>
                                        <td>{this.getAuthorName()}</td>
                                    </tr>
                                    <tr>
                                        <td>Score</td>
                                        <td>{this.getScore()}</td>
                                    </tr>
                                </tbody>
                            </table>
                            <div className="col-sm-12">
                                <button className="btn btn-success">Read</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default ReviewListItem;