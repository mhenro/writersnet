import React from 'react';
import { Link } from 'react-router-dom';
import { formatDate } from '../../utils.jsx';

/*
 props:
 news - array
 */
class NewsListItem extends React.Component {
    getAuthorAvatar() {
        return this.props.news.authorAvatar;
    }

    getSubscriptionName() {
        return <Link to={'/authors/' + this.props.news.authorId}>{this.props.news.authorFullName}</Link>;
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

    getNewsText() {
        switch (this.props.news.type) {
            case 1: return this.getBookUpdatedNews(this.props.news); break;
        }
    }

    render() {
        return (
            <div>
                <hr/>
                <div className="row">
                    <div className="col-sm-1">
                        <img src={this.getAuthorAvatar() + '?date=' + new Date()} className="img-rounded" width="100%" height="auto"/>
                    </div>
                    <div className="col-sm-11">
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