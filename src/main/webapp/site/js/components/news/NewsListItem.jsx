import React from 'react';
import { Link } from 'react-router-dom';
import { formatDate } from '../../utils.jsx';

/*
    props:
    news - array
    dt - date for cached images
 */
class NewsListItem extends React.Component {
    getAuthorAvatar() {
        return this.props.news.authorAvatar;
    }

    getSubscriptionName() {
        return <Link to={'/authors/' + this.props.news.authorId}>{this.props.news.authorFullName}</Link>;
    }

    getFriendName() {
        return <Link to={'/authors/' + this.props.news.subscriptionId}>{this.props.news.subscriptionFullName}</Link>;
    }

    getBookName() {
        return <Link to={'/reader/' + this.props.news.bookId}>{this.props.news.bookName}</Link>;
    }

    getCreated() {
        let date = new Date(this.props.news.created);
        return formatDate(date);
    }

    getBookUpdatedNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} updated the {this.getBookName()}
            </div>
        )
    }

    getNewCommentNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} left a comment in the {this.getBookName()}
            </div>
        )
    }

    getUpdatePersonalInfoNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} updated his personal info.
            </div>
        )
    }

    getAddFriendNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} added {this.getFriendName()} to the subscriptions.
            </div>
        )
    }

    getNewsText() {
        switch (this.props.news.type) {
            case 1: return this.getBookUpdatedNews(this.props.news); break;
            case 3: return this.getNewCommentNews(this.props.news); break;
            case 4: return this.getUpdatePersonalInfoNews(this.props.news); break;
            case 8: return this.getAddFriendNews(this.props.news); break;
        }
    }

    render() {
        return (
            <div>
                <hr/>
                <div className="row">
                    <div className="col-sm-1 col-xs-3">
                        <img src={this.getAuthorAvatar() /*+ '?date=' + this.props.dt*/} className="img-rounded" width="100%" height="auto"/>
                    </div>
                    <div className="col-sm-11 col-xs-9">
                        <div className="col-sm-12">{this.getSubscriptionName()}</div>
                        <div className="col-sm-12">{this.getCreated()}</div>
                    </div>
                </div>
                <div className="row">
                    <br/>
                    <div className="col-sm-12">
                        {this.getNewsText()}
                    </div>
                </div>
            </div>
        )
    }
}

export default NewsListItem;