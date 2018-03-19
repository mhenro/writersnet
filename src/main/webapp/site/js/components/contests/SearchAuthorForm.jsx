import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button, Tooltip, OverlayTrigger, Checkbox } from 'react-bootstrap';
import { Pagination } from 'react-bootstrap';

import { getAuthors } from '../../actions/AuthorActions.jsx';
import { closeSearchAuthorForm } from '../../actions/ContestActions.jsx';
import { createNotify } from '../../actions/GlobalActions.jsx';
import { clone } from '../../utils.jsx';
import { getLocale } from '../../locale.jsx';

/*
    props:
    - contestId
    - onAddUsers - callback
    - onGetSelectedAuthors - callback
 */
class SearchAuthorForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            sortType: 'firstName',
            size: 5,
            activePage: 1,
            totalPages: 1,
            currentName: null,
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
        this.setState({
            activePage: 1,
            totalPages: 1,
            currentName: null,
            authors: [],
            selectedAuthors: [],
            searchPattern: ''
        });
        this.props.onGetAuthors(null, 1, this.state.size, this.state.sortType, data => this.updateAuthors(data));

        if (!this.props.contestId) {
            return;
        }

        this.props.onGetSelectedAuthors(this.props.contestId, data => this.updateSelectedAuthors(data));
    }

    onClose() {
        this.props.onClose();
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        this.props.onGetAuthors(this.state.currentName, page, this.state.size, this.state.sortType, data => this.updateAuthors(data));
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
            this.props.onGetAuthors(this.state.searchPattern, 1, this.state.size, this.state.sortType, data => this.updateAuthors(data));
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
                    <td><b>Author name</b></td>
                    <td><b>Selected</b></td>
                </tr>
                </thead>
                <tbody>
                {this.state.authors.map((author, key) => {
                    return (
                        <tr key={key}>
                            <td>{author.fullName}</td>
                            <td><Checkbox onChange={e => this.onChecked(e, author.username)} checked={this.isChecked(author.username)}></Checkbox></td>
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
            return getAuthors(name, page - 1, size, sortType).then(([response, json]) => {
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