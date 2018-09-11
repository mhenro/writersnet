import React from 'react';
import { Link } from 'react-router-dom';
import { formatDate } from '../../utils.jsx';
import { getLocale } from '../../locale.jsx';

/*
    props:
    - news - array
    - dt - date for cached images
    - language
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

    getContestName() {
        // return <Link to={'/reader/' + this.props.news.bookId}>{this.props.news.bookName}</Link>;
        return <span>{this.props.news.contestName}</span>
    }

    getCreated() {
        let date = new Date(this.props.news.created);
        return formatDate(date);
    }

    getBookUpdatedNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} {getLocale(this.props.language)['updated the']} {this.getBookName()}
            </div>
        )
    }

    getNewCommentNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} {getLocale(this.props.language)['left a comment in the']} {this.getBookName()}
            </div>
        )
    }

    getUpdatePersonalInfoNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} {getLocale(this.props.language)['updated his personal info']}
            </div>
        )
    }

    getAddFriendNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} {getLocale(this.props.language)['added']} {this.getFriendName()} {getLocale(this.props.language)['to the subscriptions']}
            </div>
        )
    }

    getCreateContestNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} has created a new contest "{this.getContestName()}"
            </div>
        )
    }

    getJudgeJoinToContestNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} took part in the contest "{this.getContestName()}" as judge
            </div>
        )
    }

    getParticipantJoinToContestNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} took part in the contest "{this.getContestName()}" as participant with book {this.getBookName()}
            </div>
        )
    }

    getContestWinnerNews(news) {
        return (
            <div>
                {this.getSubscriptionName()} has won in the contest "{this.getContestName()}" with book {this.getBookName()}
            </div>
        )
    }

    getNewsText() {
        switch (this.props.news.type) {
            case 1: return this.getBookUpdatedNews(this.props.news); break;
            case 3: return this.getNewCommentNews(this.props.news); break;
            case 4: return this.getUpdatePersonalInfoNews(this.props.news); break;
            case 8: return this.getAddFriendNews(this.props.news); break;
            case 10: return this.getCreateContestNews(this.props.news); break;
            case 11: return this.getJudgeJoinToContestNews(this.props.news); break;
            case 12: return this.getParticipantJoinToContestNews(this.props.news); break;
            case 13: return this.getContestWinnerNews(this.props.news); break;
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