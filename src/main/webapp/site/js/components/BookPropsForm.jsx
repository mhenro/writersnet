import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';
import Select from 'react-select';

import {
    closeBookPropsForm,
    createNotify
} from '../actions/GlobalActions.jsx';
import {
    setBook,
    getBookDetails
} from '../actions/BookActions.jsx';

import { locale } from '../locale.jsx';

/*
    props:
    - showBookPropsForm
    - editableBook
    - author
 */
class BookPropsForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            name: '',
            description: '',
            serie: null,
            genre: null,
            language: null
        };

        ['save', 'close', 'onShow', 'onFieldChange', 'onSerieChange', 'onGenreChange', 'onLanguageChange', 'updateForm', 'updateState', 'onSubmit'].map(fn => this[fn] = this[fn].bind(this));
    }

    onShow() {
        if (this.isDataLoaded() && this.props.editableBook) {
            this.updateForm();
        } else {
            this.setState({
                name: '',
                description: '',
                serie: null,
                genre: null,
                language: null
            });
        }
    }

    updateForm() {
        this.props.onGetBookDetails(this.props.editableBook.id, this.updateState);
    }

    updateState() {
        let book = this.props.book;
        this.setState({
            name: book.name,
            description: book.description,
            serie: book.bookSerie ? {value: book.bookSerie.id, label: book.bookSerie.name} : {},
            genre: {value: book.genre, label: book.genre},
            language: {value: book.language, label: locale[book.language || 'EN'].label}
        });
    }

    save() {
        //TODO
    }

    close() {
        this.props.closeBookPropsForm();
    }

    onFieldChange(proxy) {
        switch (proxy.target.id) {
            case 'name': this.setState({name: proxy.target.value}); break;
            case 'description': this.setState({description: proxy.target.value}); break;
        }
    }

    onSerieChange(serie) {
        this.setState({
            serie: serie
        });
    }

    getSerieItems() {
        let options = [];
        this.props.author.bookSeries.forEach(serie => {
            options.push({
                value: serie.id,
                label: serie.name
            });
        });
        return options;
    }

    onGenreChange(genre) {
        this.setState({
            genre: genre
        });
    }

    getGenreItems() {
        let options = [];
        return options;
    }

    onLanguageChange(language) {
        this.setState({
            language: language
        });
    }

    getLanguageItems() {
        let options = [];
        for (var lang in locale) {
            options.push({
                value: lang,
                label: locale[lang].label
            });
        }
        return options;
    }

    onSubmit(event) {
        event.preventDefault();
       //TODO
    }

    isDataLoaded() {
        if (!this.props.author || !this.props.showBookPropsForm) {
            return false;
        }
        return true;
    }

    render() {
        if (!this.isDataLoaded()) {
            return null;
        }
        return (
            <Modal show={this.props.showBookPropsForm} onHide={this.close} onShow={this.onShow}>
                <Modal.Header>
                    {this.props.editableBook ? 'Editing "' + this.props.editableBook.name + '"' : 'Adding a new book'}
                </Modal.Header>
                <Modal.Body>
                    <form className="form-horizontal" onSubmit={this.onSubmit}>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="name">Name:</label>
                            <div className="col-sm-10">
                                <input value={this.state.name} onChange={this.onFieldChange} type="text" className="form-control" id="name" placeholder="Enter the book name" name="name"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="description">Description:</label>
                            <div className="col-sm-10">
                                <input value={this.state.description} onChange={this.onFieldChange} type="text" className="form-control" id="description" placeholder="Enter the book description" name="description"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="serie">Serie's name:</label>
                            <div className="col-sm-10">
                                <Select value={this.state.serie} id="serie" options={this.getSerieItems()} onChange={this.onSerieChange} placeholder="Choose the book serie"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="genre">Genre:</label>
                            <div className="col-sm-10">
                                <Select value={this.state.genre} id="genre" options={this.getGenreItems()} onChange={this.onGenreChange} placeholder="Choose the book genre"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="language">Language:</label>
                            <div className="col-sm-10">
                                <Select value={this.state.language} id="language" options={this.getLanguageItems()} onChange={this.onLanguageChange} placeholder="Choose the book language"/>
                            </div>
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <div className="btn-group">
                        <Button onClick={this.save} className="btn btn-success">Save</Button>
                        <Button onClick={this.close} className="btn btn-default">Close</Button>
                    </div>
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showBookPropsForm: state.GlobalReducer.showBookPropsForm,
        editableBook: state.GlobalReducer.editableBook,
        book: state.BookReducer.book,
        author: state.AuthorReducer.author,
        registered: state.GlobalReducer.registered,
        login: state.GlobalReducer.user.login
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        closeBookPropsForm: () => {
            dispatch(setBook(null));
            dispatch(closeBookPropsForm());
        },

        onGetBookDetails: (bookId, callback) => {
            return getBookDetails(bookId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setBook(json));
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

export default connect(mapStateToProps, mapDispatchToProps)(BookPropsForm);