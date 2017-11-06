import React from 'react';
import { Link } from 'react-router-dom';
import { formatBytes } from '../utils.jsx';
import ReactStars from 'react-stars';
/*
    props:
    - author
 */
class AuthorListItem extends React.Component {
    getAverageRating() {
        return parseFloat(this.props.author.rating.averageRating.toFixed(2));
    }

    render() {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <div className="row">
                        <div className="col-sm-4">
                            <img src={this.props.author.avatar + '?date=' + new Date()} className="img-rounded" width="150" height="auto"/>
                        </div>
                        <div className="col-sm-8">
                            <div>
                                {this.props.author.firstName + ' ' + this.props.author.lastName}
                            </div>
                            <div>
                                <ReactStars count={5} size={18} color2={'orange'} edit={false} value={this.getAverageRating()} className="stars"/>
                                <span className="stars-end"><b>{this.props.author.rating.averageRating.toFixed(2) + ' * ' + this.props.author.rating.userCount}</b></span>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <Link to={'/authors/' + this.props.author.username} className="btn btn-success btn-sm">Author page</Link>
                                </div>
                                <div className="col-sm-6">
                                    <span>offline</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AuthorListItem;