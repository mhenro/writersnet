import React from 'react';

import NewsListItem from './NewsListItem.jsx';

/*
    props:
    news - array
 */
class NewsList extends React.Component {
    render() {
        return (
            <div>
                {this.props.news.map((news, key) => {
                    return <NewsListItem news={news} key={key}/>
                })}
            </div>
        )
    }
}

export default NewsList;