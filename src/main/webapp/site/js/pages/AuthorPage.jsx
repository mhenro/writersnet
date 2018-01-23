import React from 'react';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';
import AuthorList from '../components/authors/AuthorList.jsx';
import AlphabetPagination from '../components/AlphabetPagination.jsx';
import { getLocale } from '../locale.jsx';

import {
    getAuthors,
    getAuthorsByFirstLetter,
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
            currentName: null,
            searchPattern: '',
            size: 5,
            sortBy: 'firstName'
        };
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        this.props.onGetAuthors(this.state.currentName, this.state.activePage, this.state.size, this.state.sortBy, totalPages => this.setTotalPages(totalPages));
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        this.props.onGetAuthors(this.state.currentName, page, this.state.size, this.state.sortBy, totalPages => this.setTotalPages(totalPages));
    }

    setTotalPages(totalPages) {
        this.setState({
            totalPages: totalPages
        });
    }

    onLetterClick(letter) {
        this.setState({
            currentName: letter,
            activePage: 1
        });
        this.props.onGetAuthorsByFirstLetter(letter, 1, this.state.size, this.state.sortBy, totalPages => this.setTotalPages(totalPages));
    }

    onSearchChange(event) {
        this.setState({
            searchPattern: event.target.value,
            activePage: 1
        });
    }

    onKeyDown(key) {
        if (key.key === 'Enter') {
            this.props.onGetAuthors(this.state.searchPattern, 1, this.state.size, this.state.sortBy, totalPages => this.setTotalPages(totalPages));
        }
    }

    selectSortType(sortType) {
        this.setState({
            sortBy: sortType
        });
        this.props.onGetAuthors(this.state.currentName, 1, this.state.size, sortType, totalPages => this.setTotalPages(totalPages));
    }

    getSortClass(sortType) {
        if (this.state.sortBy === sortType) {
            return 'btn active btn-xs btn-default';
        }
        return 'btn btn-xs btn-default';
    }

    render() {
        return (
            <div>
                <div className="col-sm-12">
                    <AlphabetPagination onClick={letter => this.onLetterClick(letter)}/>
                    <br/>
                </div>
                <div className="col-sm-12">
                    <div className="input-group">
                        <input value={this.state.searchPattern} onChange={event => this.onSearchChange(event)} onKeyDown={key => this.onKeyDown(key)} type="text" className="form-control" placeholder={getLocale(this.props.language)['Input author name']} />
                        <div className="input-group-btn">
                            <button className="btn btn-default" type="submit">
                                <i className="glyphicon glyphicon-search"></i>
                            </button>
                        </div>
                    </div>
                    <br/>
                </div>
                <div className="col-sm-12">
                    {getLocale(this.props.language)['Sort by:']}&nbsp;
                    <div className="btn-group">
                        <button onClick={() => this.selectSortType('firstName')} className={this.getSortClass('firstName')}>{getLocale(this.props.language)['Name']}</button>
                        <button onClick={() => this.selectSortType('totalRating')} className={this.getSortClass('totalRating')}>{getLocale(this.props.language)['Rating']}</button>
                        <button onClick={() => this.selectSortType('online')} className={this.getSortClass('online')}>{getLocale(this.props.language)['Online']}</button>
                    </div>
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
                        onSelect={page => this.pageSelect(page)}/>
                    <br/>
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
                        onSelect={page => this.pageSelect(page)}/>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        authors: state.AuthorReducer.authors,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthors: (name, page, size, sortType, totalPagesCallback) => {
            return getAuthors(name, page - 1, size, sortType).then(([response, json]) => {
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
        },

        onGetAuthorsByFirstLetter: (letter, page, size, sortType, totalPagesCallback) => {
            return getAuthorsByFirstLetter(letter, page - 1, size, sortType).then(([response, json]) => {
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