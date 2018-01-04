import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { formatBytes } from '../../utils.jsx';
import ReactStars from 'react-stars';
/*
    props:
    - author
 */
class AuthorListItem extends React.Component {
    static contextTypes = {
        router: PropTypes.shape({
            history: PropTypes.shape({
                push: PropTypes.func.isRequired,
                replace: PropTypes.func.isRequired
            }).isRequired,
            staticContext: PropTypes.object
        }).isRequired
    };

    getAverageRating() {
        return parseFloat(this.props.author.rating.averageRating.toFixed(2));
    }

    goToAuthor() {
        this.context.router.history.push('/authors/' + this.props.author.username);
    }

    //TODO: Add logic to determine status of the user
    renderOnlineStatus() {
        if (this.props.author.online) {
            return (
                <div className="online-user">
                    online
                </div>
            )
        } else {
            return (
                <div className="offline-user">
                    offline
                </div>
            )
        }
    }

    render() {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <div className="row">
                        <div className="col-sm-4">
                            <img src={this.props.author.avatar /*+ '?date=' + new Date()*/} onClick={() => this.goToAuthor()} className="img-rounded clickable" width="150" height="auto"/>
                        </div>
                        <div className="col-sm-8">
                            <div>
                                {this.props.author.firstName + ' ' + this.props.author.lastName}
                            </div>
                            <div>
                                <ReactStars count={5} size={18} color2={'orange'} edit={false} value={this.getAverageRating()} className="stars"/>
                                <b>{this.props.author.rating.averageRating.toFixed(2) + ' * ' + this.props.author.rating.userCount}</b>
                                <div className="stars-end"></div>
                            </div>
                            <br/>
                            <div className="row">
                                <div className="col-sm-6">
                                    <Link to={'/authors/' + this.props.author.username} className="btn btn-success btn-sm">Author page</Link>
                                </div>
                                <div className="col-sm-6">
                                    {this.renderOnlineStatus()}
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