import React from 'react';
import { connect } from 'react-redux';
import {
    getAuthorDetails,
    setAuthor
} from '../actions/AuthorActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

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
            <div className="container">
                <div className="row">
                    <div className="col-sm-12">{this.props.author.firstName + ' ' + this.props.author.lastName}</div>
                </div>
                <div className="row">
                    <div className="col-sm-12">{this.props.author.section.name}</div>
                </div>
                {this.props.match.params.authorName}
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