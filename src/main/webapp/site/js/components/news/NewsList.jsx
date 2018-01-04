import React from 'react';

import NewsListItem from './NewsListItem.jsx';

/*
    props:
    news - array,
    dt - date for cached images
 */
class NewsList extends React.Component {
    render() {
        return (
            <div>
                {this.props.news.map((news, key) => {
                    return <NewsListItem news={news} key={key} dt={this.props.dt}/>
                })}
            </div>
        )
    }
}

export default NewsList;