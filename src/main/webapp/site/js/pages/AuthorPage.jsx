import React from 'react';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';
import AuthorList from '../components/authors/AuthorList.jsx';
import AlphabetPagination from '../components/AlphabetPagination.jsx';
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
            totalPages: 1,
            currentName: null
        };

        ['pageSelect', 'setTotalPages', 'onLetterClick'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        this.props.onGetAuthors(this.state.currentName, this.state.activePage, this.setTotalPages);
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        this.props.onGetAuthors(this.state.currentName, page, this.setTotalPages);
    }

    setTotalPages(totalPages) {
        this.setState({
            totalPages: totalPages
        });
    }

    onLetterClick(letter) {
        this.setState({
            currentName: letter,
            currentPage: 1
        });
        this.props.onGetAuthors(letter, 1, this.setTotalPages);
    }

    render() {
        return (
            <div>
                <div className="col-sm-12">
                    <AlphabetPagination onClick={this.onLetterClick}/>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <AuthorList authors={this.props.authors}/>
                </div>
                <div className="col-sm-12 text-center">
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
        onGetAuthors: (name, page, totalPagesCallback) => {
            return getAuthors(name, page - 1).then(([response, json]) => {
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