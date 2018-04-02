import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';
import Select from 'react-select';
import TextEditor from '../texteditor/TextEditor.jsx';
import { getLocale } from '../../locale.jsx';

import { closeReviewForm } from '../../actions/BookActions.jsx';
import { saveReview } from '../../actions/ReviewActions.jsx';
import { createNotify } from '../../actions/GlobalActions.jsx';

/*
    props:
    - editableBook
    - onCloseUpdate - callback
 */
class ReviewForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            editorText: null,
            name: '',
            score: 3
        };
    }

    onClose() {
        this.props.onClose();
        this.props.onCloseUpdate();
    }

    onSave() {
        let review = {
            bookId: this.props.editableBook.id,
            text: this.state.editorText,
            authorId: this.props.login,
            name: this.state.name,
            score: this.state.score.value
        };
        this.props.onSaveReview(review, this.props.token, () => this.onClose());
    }

    onUpdateText(text) {
        this.setState({
            editorText: text
        });
    }

    onShow() {

    }

    getBookName() {
        return '"' + this.props.editableBook.name + '"';
    }

    getScoreItems() {
        let getTextScore = (score) => {
            switch (score) {
                case 1: return 'Very bad';
                case 2: return 'Bad';
                case 3: return 'Normal';
                case 4: return 'Good';
                case 5: return 'Great';
            }
        };
        let options = [1, 2, 3, 4, 5];
        return options.map(score => {
            return {
                value: score,
                label: getTextScore(score)
            };
        });
    }

    onScoreChange(score) {
        this.setState({
            score: score
        });
    }

    onFieldChange(proxy) {
        switch (proxy.target.id) {
            case 'name': this.setState({name: proxy.target.value}); break;
            case 'score': this.setState({score: proxy.target.value}); break;
        }
    }

    onSubmit(event) {
        event.preventDefault();
    }

    isDateLoaded() {
        if (!this.props.editableBook) {
            return false;
        }
        return true;
    }

    render() {
        if (!this.isDateLoaded()) {
            return null;
        }

        return (
            <Modal show={this.props.showReviewForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    {getLocale(this.props.language)['Add new review to'] + ' ' + this.getBookName()}
                </Modal.Header>
                <Modal.Body>
                    <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="name">{getLocale(this.props.language)['Name:']}</label>
                            <div className="col-sm-10">
                                <input value={this.state.name} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="name" placeholder={getLocale(this.props.language)['Enter the review name']} name="name"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="score">{getLocale(this.props.language)['Score:']}</label>
                            <div className="col-sm-10">
                                <Select value={this.state.score}
                                        id="score"
                                        options={this.getScoreItems()}
                                        onChange={score => this.onScoreChange(score)}
                                        placeholder={getLocale(this.props.language)['Choose your score for this book']}
                                        menuContainerStyle={{zIndex: '9999'}}/>
                            </div>
                        </div>
                    </form>
                    <TextEditor onUpdateText={text => this.onUpdateText(text)}/>
                </Modal.Body>
                <Modal.Footer>
                    <div className="btn-group">
                        <Button onClick={() => this.onSave()} className="btn btn-success">{getLocale(this.props.language)['Save']}</Button>
                        <Button onClick={() => this.onClose()} className="btn btn-default">{getLocale(this.props.language)['Close']}</Button>
                    </div>
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showReviewForm: state.BookReducer.showReviewForm,
        editableBook: state.BookReducer.editableBook,
        login: state.GlobalReducer.user.login,
        token: state.GlobalReducer.token,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onClose: () => {
            dispatch(closeReviewForm());
        },

        onSaveReview: (review, token, exitCallback) => {
            saveReview(review, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Review was saved successfully'));
                    exitCallback();
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

export default connect(mapStateToProps, mapDispatchToProps)(ReviewForm);