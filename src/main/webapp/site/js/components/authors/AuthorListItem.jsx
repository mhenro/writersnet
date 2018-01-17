import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import ReactStars from 'react-stars';

import { getHost } from '../../utils.jsx';
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

    renderCrown() {
        if (this.props.author.premium) {
            return <img src={getHost() + 'css/images/crown.png'} title="Premium author" width="32" height="auto"/>;
        }
    }

    render() {
        return (
            <div className={'panel panel-default ' + (this.props.author.premium ? 'premium' : '')}>
                <div className="panel-body">
                    <div className="row">
                        <div className="col-sm-4">
                            <div className="author-short-avatar">
                                <img src={this.props.author.avatar} onClick={() => this.goToAuthor()} className="img-rounded clickable" width="150" height="auto"/>
                            </div>
                        </div>
                        <div className="col-sm-7">
                            <div>
                                {this.props.author.fullName}
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
                        <div className="col-sm-1 text-right">
                            {this.renderCrown()}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AuthorListItem;