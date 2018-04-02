import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types'
import ReactStars from 'react-stars';

import { getLocale } from '../../locale.jsx';
import { formatDate, getHost } from '../../utils.jsx';

/*
    props:
    - book
    - language
 */
class BookBriefItem extends React.Component {
    static contextTypes = {
        router: PropTypes.shape({
            history: PropTypes.shape({
                push: PropTypes.func.isRequired,
                replace: PropTypes.func.isRequired
            }).isRequired,
            staticContext: PropTypes.object
        }).isRequired
    };

    getName() {
        let name = this.props.book.name;
        return name;
    }

    getAuthor() {
        let author = this.props.book.author;
        return (
            <Link to={'/authors/' + author.username}>{author.firstName + ' ' + author.lastName}</Link>
        )
    }

    getRating() {
        return (
            <div>
                <ReactStars count={5} size={18} color2={'orange'} edit={false} value={this.props.book.totalRating.averageRating} className="stars"/>
                <b>{this.props.book.totalRating.averageRating.toFixed(2) + ' * ' + this.props.book.totalRating.userCount}</b>
                <div className="stars-end"></div>
            </div>
        )
    }

    getGenre() {
        let genre = getLocale(this.props.language)[this.props.book.genre];
        return genre;
    }

    getDescription() {
        let description = this.props.book.description;
        return description;
    }

    getLanguage() {
        let language = getLocale(this.props.book.language).label;
        return language;
    }

    getCreated() {
        let created = new Date(this.props.book.created);
        return created.getFullYear();   //formatDate(created, 'D-M-Y');
    }

    getLastUpdate() {
        let lastUpdate = new Date(this.props.book.lastUpdate);
        return formatDate(lastUpdate, 'D-M-Y');
    }

    onBookClick() {
        this.context.router.history.push('/reader/' + this.props.book.id);
    }

    onAuthorClick() {
        this.context.router.history.push('/authors/' + this.props.book.author.username);
    }

    renderCrown() {
        if (this.props.book.author.premium) {
            return <img src={getHost() + 'css/images/crown.png'} title="Premium author" width="32" height="auto"/>;
        }
    }

    renderPurse() {
        if (this.props.book.paid) {
            let cost = parseFloat(this.props.book.cost / 100).toFixed(2);
            return (
                <img src={getHost() + '../css/images/purse.png'} className="img-rounded" title={'Paid book (' + cost + '$)'} width="24"
                     height="auto"/>
            )
        }
    }

    render() {
        return (
            <div className={'panel panel-default ' + (this.props.book.author.premium ? 'premium' : '')}>
                <div className="panel-body">
                    <div className="row">
                        <div className="col-sm-3">
                            <div className="col-sm-12">
                                <img src={this.props.book.cover /*+ '?date=' + new Date()*/} onClick={() => this.onBookClick()} className="img-rounded clickable" width="100%" height="auto"/>
                            </div>
                            <div className="col-sm-12 text-center">
                                <br/>
                                {this.renderPurse()}
                            </div>
                        </div>
                        <div className="col-sm-6">
                            <table className="table">
                                <tbody>
                                    <tr>
                                        <td>Name</td>
                                        <td>{this.getName()}</td>
                                    </tr>
                                    <tr>
                                        <td>Author</td>
                                        <td>{this.getAuthor()}</td>
                                    </tr>
                                    <tr>
                                        <td>Rating</td>
                                        <td>{this.getRating()}</td>
                                    </tr>
                                    <tr>
                                        <td>Genre</td>
                                        <td>{this.getGenre()}</td>
                                    </tr>
                                    <tr>
                                        <td>Description</td>
                                        <td>{this.getDescription()}</td>
                                    </tr>
                                    <tr>
                                        <td>Language</td>
                                        <td>{this.getLanguage()}</td>
                                    </tr>
                                    <tr>
                                        <td>Created</td>
                                        <td>{this.getCreated()}</td>
                                    </tr>
                                    <tr>
                                        <td>Last update</td>
                                        <td>{this.getLastUpdate()}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div className="col-sm-3 col-xs-1">
                            <div className="col-sm-12">
                                <img src={this.props.book.author.avatar/* + '?date=' + new Date()*/} onClick={() => this.onAuthorClick()} className="img-rounded clickable" width="100%" height="auto"/>
                            </div>
                            <div className="col-sm-12 text-center">
                                <br/>
                                {this.renderCrown()}
                            </div>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-12">
                        <div className="btn-toolbar" style={{margin: '0 0 10px 10px'}}>
                            <Link to={'/reader/' + this.props.book.id} className="btn btn-success btn-sm">Read</Link>
                            <Link to={'/authors/' + this.props.book.author.username} className="btn btn-success btn-sm">Go to writer</Link>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default BookBriefItem;