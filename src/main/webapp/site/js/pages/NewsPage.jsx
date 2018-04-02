import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';

import { getNews } from '../actions/NewsActions.jsx';
import { createNotify } from '../actions/GlobalActions.jsx';
import { setToken } from '../actions/AuthActions.jsx';

import NewsList from '../components/news/NewsList.jsx';

class NewsPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            news: [],
            totalPages: 1,
            activePage: 1
        };
        ['updateNews', 'pageSelect'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        this.timer = setInterval(() => {
            if (this.props.token !== '') {
                this.props.onGetNews(this.props.token, this.state.activePage, this.updateNews)
                clearInterval(this.timer);
            }
        }, 1000);
    }

    updateNews(news) {
        this.setState({
            news: news.content,
            totalPages: news.totalPages,
            activePage: news.number + 1
        });
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });
        this.props.onGetNews(this.props.token, page, this.updateNews)
    }

    render() {
        return (
            <div>
                <div className="text-center">
                    <Pagination
                        className={'shown'}
                        prev
                        next
                        first
                        last
                        ellipsis
                        boundaryLinks
                        items={this.state.totalPages}
                        maxButtons={3}
                        activePage={this.state.activePage}
                        onSelect={this.pageSelect}/>
                </div>
                <NewsList news={this.state.news} dt={this.props.mutableDate} language={this.props.language}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        token: state.GlobalReducer.token,
        mutableDate: state.GlobalReducer.mutableDate,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetNews: (token, page, callback) => {
            getNews(token, page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(NewsPage);