import React from 'react';
import { connect } from 'react-redux';
import {
    getAuthorDetails,
    setAuthor
} from '../actions/AuthorActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

import AuthorFile from '../components/section/AuthorFile.jsx';
import AuthorShortInfo from '../components/section/AuthorShortInfo.jsx';
import BookSerieList from '../components/section/BookSerieList.jsx';

/*
    props:
    - this.props.match.params.authorName - user id
* */
class SectionPage extends React.Component {
    componentDidMount() {
        this.props.onGetAuthorDetails(this.props.match.params.authorName);
    }

    render() {
        if (!this.props.author) {
            return null;
        }
        return (
            <div>
                <div className="row">
                    <div className="col-sm-12 section-name">
                        {this.props.author.firstName + ' ' + this.props.author.lastName}
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-12 section-author-name">
                        {this.props.author.section.name}
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-3">
                        <AuthorFile author={this.props.author}/>
                    </div>
                    <div className="col-sm-9">
                        <AuthorShortInfo author={this.props.author}/>
                    </div>
                </div>
                <hr/>
                <BookSerieList series={this.props.author.bookSeries}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        author: state.AuthorReducer.author
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthorDetails: (userId) => {
            return getAuthorDetails(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setAuthor(json));
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

export default connect(mapStateToProps, mapDispatchToProps)(SectionPage);