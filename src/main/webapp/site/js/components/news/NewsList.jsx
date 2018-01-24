import React from 'react';

import NewsListItem from './NewsListItem.jsx';

/*
    props:
    - news - array,
    - dt - date for cached images
    - language
 */
class NewsList extends React.Component {
    render() {
        return (
            <div>
                {this.props.news.map((news, key) => {
                    return <NewsListItem news={news} key={key} dt={this.props.dt} language={this.props.language}/>
                })}
            </div>
        )
    }
}

export default NewsList;