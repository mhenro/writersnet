import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';
import {
    getAuthorDetails,
    setAuthor
} from '../actions/AuthorActions.jsx';
import {
    deleteBook
} from '../actions/BookActions.jsx';
import {
    openBookPropsForm,
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
    constructor(props) {
        super(props);
        this.state = {
            showSeriesForm: false
        };

        ['onAddNewBook', 'onEditSeries', 'onEditBook', 'onDeleteBook'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        this.props.onGetAuthorDetails(this.props.match.params.authorName);
    }

    onAddNewBook() {
        this.props.onOpenBookPropsForm();
    }

    onEditSeries() {

    }

    onEditBook(book) {
        this.props.onOpenBookPropsForm(book);
    }

    onDeleteBook(bookId, token) {
        this.props.onDeleteBook(bookId, token, () => this.props.onGetAuthorDetails(this.props.match.params.authorName));
    }

    renderSectionToolbar() {
        if (this.props.registered && this.props.login === this.props.author.username) {
            return (
                <div
                    className="col-sm-12 panel panel-success">
                    <div className="panel-body btn-group">
                        <button className="btn btn-success" onClick={this.onAddNewBook}>Add new book</button>
                        <button className="btn btn-success" onClick={() => this.openSeriesForm()}>Edit series</button>
                    </div>
                </div>
            )
        } else {
            return null;
        }
    }

    openSeriesForm() {
        this.setState({
            showSeriesForm: true
        });
    }

    saveSeriesForm() {
        this.closeSeriesForm();
    }

    closeSeriesForm() {
        this.setState({
            showSeriesForm: false
        });
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
                    <div className="col-sm-12 col-lg-3">
                        <AuthorFile author={this.props.author} registered={this.props.registered} login={this.props.login}/>
                    </div>
                    <div className="col-sm-12 col-lg-9">
                        <AuthorShortInfo author={this.props.author}/>
                    </div>
                </div>
                <div className="col-sm-12 panel panel-success">
                    <div className="panel-body">
                        {this.props.author.section.description}
                    </div>
                </div>
                {this.renderSectionToolbar()}
                <hr/>
                <BookSerieList series={this.props.author.bookSeries}
                               books={this.props.author.books}
                               registered={this.props.registered}
                               login={this.props.login}
                               author={this.props.author}
                               onEditBook={this.onEditBook}
                               onDeleteBook={this.onDeleteBook}
                               token={this.props.token}
                               language={this.props.language}/>

                {/* Edit series form */}
                <Modal show={this.state.showSeriesForm} onHide={() => this.closeSeriesForm()}>
                    <Modal.Header>
                        TODO: Edit series header
                    </Modal.Header>
                    <Modal.Body>
                        TODO: Body
                    </Modal.Body>
                    <Modal.Footer>
                        <div className="btn-group">
                            <Button onClick={() => this.saveSeriesForm()} className="btn btn-success">Save</Button>
                            <Button onClick={() => this.closeSeriesForm()} className="btn btn-default">Close</Button>
                        </div>
                    </Modal.Footer>
                </Modal>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        author: state.AuthorReducer.author,
        registered: state.GlobalReducer.registered,
        token: state.GlobalReducer.token,
        login: state.GlobalReducer.user.login,
        language: state.GlobalReducer.language
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
        },

        onOpenBookPropsForm: (book) => {
            dispatch(openBookPropsForm(book));
        },

        onDeleteBook: (bookId, token, callback) => {
            return deleteBook(bookId, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Book was deleted successfully'));
                    callback();
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