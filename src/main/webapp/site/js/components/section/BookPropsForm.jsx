import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';
import Select from 'react-select';
import { formatDate } from '../../utils.jsx';

import {
    createNotify
} from '../../actions/GlobalActions.jsx';
import {
    getAuthorDetails,
    setAuthor
} from '../../actions/AuthorActions.jsx';
import {
    closeBookPropsForm,
    setBook,
    getBookDetails,
    saveBook,
    saveBookText,
    getSeries,
    setSeries,
    getGenres,
    setGenres,
    saveCover
} from '../../actions/BookActions.jsx';

import FileUploader from '../FileUploader.jsx';

import { locale, getLocale } from '../../locale.jsx';

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
            serie: {value: null, label: 'Without serie'},
            genre: null,
            language: null,
            cover: '',
            lastUpdated: ''
        };

        ['save', 'close', 'onShow', 'onFieldChange', 'onCoverChange', 'onSaveText', 'onSerieChange',
            'onGenreChange', 'onLanguageChange', 'updateForm', 'onUpdateLastUpdatedField', 'updateState', 'onSubmit'].map(fn => this[fn] = this[fn].bind(this));
    }

    onShow() {
        this.props.onGetSeries(this.props.author.username);
        this.props.onGetGenres();
        if (this.isDataLoaded() && this.props.editableBook) {
            this.updateForm(this.props.editableBook.id);
        } else {
            this.setState({
                name: '',
                description: '',
                serie: {value: null, label: 'Without serie'},
                genre: null,
                language: null,
                cover: '',
                lastUpdated: ''
            });
        }
    }

    updateForm(id) {
        this.props.onGetBookDetails(id, this.updateState);
    }

    updateState() {
        let book = this.props.book;
        this.setState({
            name: book.name,
            description: book.description,
            serie: book.bookSerie ? {value: book.bookSerie.id, label: book.bookSerie.name} : {value: null, label: 'Without serie'},
            genre: {value: book.genre, label: getLocale(this.props.language)[book.genre]},
            language: {value: book.language, label: locale[book.language || 'EN'].label},
            cover: book.cover,
            lastUpdated: book.lastUpdate
        });
    }

    save() {
        let book = {
            id: this.props.editableBook ? this.props.editableBook.id : null,
            authorName: this.props.author.username,
            name: this.state.name,
            description: this.state.description,
            serieId: this.state.serie ? this.state.serie.value : null,
            genre: this.state.genre ? this.state.genre.value : null,
            language: this.state.language.value,
            cover: this.state.cover
        };
        this.props.onSaveBook(book, this.props.token, this.close);
    }

    close() {
        this.props.closeBookPropsForm();
        this.props.onGetAuthorDetails(this.props.author.username);
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
        let options = [{value: null, label: 'Without serie'}];
        this.props.series.forEach(serie => {
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
        this.props.genres.forEach(genre => {
            options.push({
                value: genre,
                label: getLocale(this.props.language)[genre]
            });
        });
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

    getLastUpdated() {
        let date = new Date(this.state.lastUpdated);
        return formatDate(date, 'D-M-Y');
    }

    onCoverChange(event) {
        if (event.target.files[0].size >= 102400) {
            this.props.onCreateNotify('warning', 'Warning', 'Image size should not be larger than 100Kb');
            return;
        }
        let formData = new FormData();
        formData.append('id', this.props.book.id);
        formData.append('userId', this.props.author.username);
        formData.append('cover', event.target.files[0]);

        this.props.onSaveCover(formData, this.props.token, this.updateForm, this.props.editableBook.id);
    }

    onSaveText(event) {
        if (event.target.files[0].size >= 10485760) {
            this.props.onCreateNotify('warning', 'Warning', 'Book text size should not be larger than 10Mb');
            return;
        }
        let bookTextRequest = new FormData;
        bookTextRequest.append('bookId', this.props.editableBook.id);
        bookTextRequest.append('userId', this.props.author.username,);
        bookTextRequest.append('text', event.target.files[0]);

        this.props.onSaveBookText(bookTextRequest, this.props.token, this.onUpdateLastUpdatedField);
    }

    onUpdateLastUpdatedField(date) {
        this.setState({
            lastUpdated: date
        });
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

                        <div className={'panel panel-default ' + (this.props.editableBook ? '' : 'hidden')}>
                            <div className="panel-heading">
                                Cover
                            </div>
                            <div className="panel-body">
                                <div className="col-sm-4">
                                    <img src={this.state.cover + '?date=' + new Date()} className="img-rounded" width="200" height="auto"/>
                                </div>
                                <div className="col-sm-8" style={{textAlign: 'center'}}>
                                    <div className="btn-group-vertical">
                                        <FileUploader
                                            btnName="Choose the book cover"
                                            name="cover"
                                            accept=".png,.jpg"
                                            className="btn btn-success"
                                            onChange={this.onCoverChange}
                                        />
                                        <br/>
                                        <button type="button" className="btn btn-success">Restore default cover</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className={'panel panel-default ' + (this.props.editableBook ? '' : 'hidden')}>
                            <div className="panel-heading">
                                Text {'(last updated at ' + this.getLastUpdated() + ')'}
                            </div>
                            <div className="panel-body">
                                <div className="col-sm-12" style={{textAlign: 'center'}}>
                                    <div className="btn-group-vertical">
                                        <FileUploader
                                            btnName="Load file from your computer"
                                            name="text"
                                            accept=".txt,.docx,.pdf"
                                            className="btn btn-success"
                                            onChange={this.onSaveText}
                                        />
                                        <br/>
                                    </div>
                                </div>
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
        showBookPropsForm: state.BookReducer.showBookPropsForm,
        editableBook: state.BookReducer.editableBook,
        book: state.BookReducer.book,
        author: state.AuthorReducer.author,
        registered: state.GlobalReducer.registered,
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token,
        series: state.BookReducer.series,
        genres: state.BookReducer.genres,
        language: state.GlobalReducer.language
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
        },

        onGetSeries: (userId) => {
            return getSeries(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setSeries(json.content));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetGenres: () => {
            return getGenres().then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setGenres(json));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSaveCover: (cover, token, callback, bookId) => {
            return saveCover(cover, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Cover was saved successfully'));
                    callback(bookId);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSaveBook: (book, token, callback) => {
            return saveBook(book, token).then(([response, json]) => {
                if (response.status === 200) {
                    callback();
                    dispatch(createNotify('success', 'Success', 'Book was added successfully'));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSaveBookText: (bookTextRequest, token, callback) => {
            return saveBookText(bookTextRequest, token).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.message);
                    dispatch(createNotify('success', 'Success', 'Book text was saved successfully'));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

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

        onCreateNotify: (type, header, message) => {
            dispatch(createNotify(type, header, message));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(BookPropsForm);