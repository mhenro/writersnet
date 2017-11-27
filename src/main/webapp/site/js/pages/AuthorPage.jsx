import React from 'react';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';
import AuthorList from '../components/authors/AuthorList.jsx';
import {
    getAuthors,
    setAuthors
} from '../actions/AuthorActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

/*
    props:
    - authors - list of authors
 */
class AuthorPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activePage: 1,
            totalPages: 1
        };

        ['handleSelect', 'setTotalPages'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        this.props.onGetAuthors(this.state.activePage, this.setTotalPages);
    }

    handleSelect(eventKey) {
        this.setState({
            activePage: eventKey
        });

        this.props.onGetAuthors(eventKey, this.setTotalPages);
    }

    setTotalPages(totalPages) {
        this.setState({
            totalPages: totalPages
        });
    }

    render() {
        return (
            <div>
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
                    onSelect={this.handleSelect}/>
                <AuthorList authors={this.props.authors}/>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        authors: state.AuthorReducer.authors
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthors: (page, totalPagesCallback) => {
            return getAuthors(page - 1).then(([response, json]) => {
                if (response.status === 200) {
                    let authors = json.content;
                    dispatch(setAuthors(authors));
                    totalPagesCallback(json.totalPages);
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

export default connect(mapStateToProps, mapDispatchToProps)(AuthorPage);