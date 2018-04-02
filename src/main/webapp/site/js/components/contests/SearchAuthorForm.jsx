import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button, Tooltip, OverlayTrigger, Checkbox } from 'react-bootstrap';
import { Pagination } from 'react-bootstrap';

import { getAuthors } from '../../actions/AuthorActions.jsx';
import { getBooks } from '../../actions/BookActions.jsx';
import { closeSearchAuthorForm } from '../../actions/ContestActions.jsx';
import { createNotify } from '../../actions/GlobalActions.jsx';
import { clone } from '../../utils.jsx';
import { getLocale } from '../../locale.jsx';

/*
    props:
    - contestId
    - onAddUsers - callback
    - onGetSelectedAuthors - callback
    - withBooks - boolean
 */
class SearchAuthorForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            sortType: 'firstName',
            size: 5,
            activePage: 1,
            totalPages: 1,
            authors: [],
            selectedAuthors: [],
            searchPattern: ''
        };
    }

    onAddAuthorToList(authorId) {
        let authorArr = clone(this.state.selectedAuthors);
        authorArr.push(authorId);
        this.setState({
            selectedAuthors: authorArr
        });
    }

    onRemoveAuthorFromList(authorId) {
        let authorArr = clone(this.state.selectedAuthors),
            index = authorArr.indexOf(authorId);
        authorArr.splice(index, 1);
        this.setState({
            selectedAuthors: authorArr
        });
    }

    isChecked(authorId) {
        return this.state.selectedAuthors.includes(authorId);
    }

    onChecked(e, authorId) {
        if (e.target.checked) {
            this.onAddAuthorToList(authorId);
        } else {
            this.onRemoveAuthorFromList(authorId);
        }
    }

    onSelect() {
        if (!this.props.contestId) {
            return;
        }

        let addJudgeRequest = {
            judges: this.state.selectedAuthors.join(','),
            contestId: this.props.contestId
        };
        this.props.onAddUsers(addJudgeRequest, this.props.token, () => this.onClose());
    }

    onShow() {
        let sortType = this.props.withBooks ? 'name' : 'firstName';
        this.setState({
            sortType: sortType,
            activePage: 1,
            totalPages: 1,
            authors: [],
            selectedAuthors: [],
            searchPattern: ''
        });
        if (this.props.withBooks) {
            this.props.onGetBooks(null, 1, this.state.size, sortType, data => this.updateAuthors(data));
        } else {
            this.props.onGetAuthors(null, 1, this.state.size, sortType, data => this.updateAuthors(data));
        }

        if (!this.props.contestId) {
            return;
        }

        this.props.onGetSelectedAuthors(this.props.contestId, data => this.updateSelectedAuthors(data));
    }

    onClose() {
        this.setState({
            sortType: 'firstName',
            size: 5,
            activePage: 1,
            totalPages: 1,
            authors: [],
            selectedAuthors: [],
            searchPattern: ''
        });
        this.props.onClose();
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        if (this.props.withBooks) {
            this.props.onGetBooks(null, page, this.state.size, this.state.sortType, data => this.updateAuthors(data));
        } else {
            this.props.onGetAuthors(null, page, this.state.size, this.state.sortType, data => this.updateAuthors(data));
        }
    }

    updateAuthors(data) {
        this.setState({
            totalPages: data.totalPages,
            authors: data.content
        });
    }

    updateSelectedAuthors(data) {
        this.setState({
            selectedAuthors: data
        });
    }

    onSearchChange(event) {
        this.setState({
            searchPattern: event.target.value,
            activePage: 1
        });
    }

    onKeyDown(key) {
        if (key.key === 'Enter') {
            if (this.props.withBooks) {
                this.props.onGetBooks(this.state.searchPattern, 1, this.state.size, this.state.sortType, data => this.updateAuthors(data));
            } else {
                this.props.onGetAuthors(this.state.searchPattern, 1, this.state.size, this.state.sortType, data => this.updateAuthors(data));
            }
        }
    }

    render() {
        return (
            <Modal show={this.props.showSearchAuthorForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    Select authors
                </Modal.Header>
                <Modal.Body>
                    <div className="row">
                        <div className="col-sm-12 text-center">
                            {this.renderSearchButton()}
                            <br/>
                        </div>
                        <div className="col-sm-12 text-center">
                            {this.renderPagination()}
                        </div>
                        <div className="col-sm-12 text-center">
                            {this.renderTable()}
                        </div>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    {this.renderFooterButtons()}
                </Modal.Footer>
            </Modal>
        )
    }

    renderSearchButton() {
        return (
            <div className="input-group">
                <input value={this.state.searchPattern}
                       onChange={event => this.onSearchChange(event)}
                       onKeyDown={key => this.onKeyDown(key)}
                       type="text"
                       className="form-control"
                       placeholder={getLocale(this.props.language)['Input author name']} />
                <div className="input-group-btn">
                    <button className="btn btn-default" type="submit">
                        <i className="glyphicon glyphicon-search"></i>
                    </button>
                </div>
            </div>
        )
    }

    renderPagination() {
        return (
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
        )
    }

    renderTable() {
        return (
            <table className="table table-hover">
                <thead>
                <tr>
                    <td><b>{this.props.withBooks ? 'Book name (Author name)' : 'Author name'}</b></td>
                    <td><b>Selected</b></td>
                </tr>
                </thead>
                <tbody>
                {this.state.authors.map((author, key) => {
                    let name = this.props.withBooks ? (author.name + ' (' + author.author.firstName + ' ' + author.author.lastName + ')') : author.fullName;
                    let onChecked = this.props.withBooks ? e => this.onChecked(e, author.id) : e => this.onChecked(e, author.username);
                    let isChecked = this.props.withBooks ? this.isChecked(author.id) : this.isChecked(author.username);
                    return (
                        <tr key={key}>
                            <td>{name}</td>
                            <td><Checkbox onChange={onChecked} checked={isChecked}></Checkbox></td>
                        </tr>
                    )
                })}
                </tbody>
            </table>
        )
    }

    renderFooterButtons() {
        return (
            <div className="btn-group">
                <Button onClick={() => this.onSelect()}
                        className="btn btn-success">Select all</Button>
                <Button onClick={() => this.onClose()}
                        className="btn btn-default">Cancel</Button>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showSearchAuthorForm: state.ContestReducer.showSearchAuthorForm,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthors: (name, page, size, sortType, callback) => {
            getAuthors(name, page - 1, size, sortType).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetBooks: (name, page, size, sort, callback) => {
            getBooks(name, null, null, page - 1, size, sort).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onClose: () => {
            dispatch(closeSearchAuthorForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchAuthorForm);