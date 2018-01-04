import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';
import TextEditor from '../texteditor/TextEditor.jsx';

import { closeReviewForm } from '../../actions/BookActions.jsx';

/*
    props:
    - editableBook
 */
class ReviewForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            editorText: null
        };
    }

    onClose() {
        this.props.onClose();
    }

    onSave(text) {

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
                    Add new review to {this.getBookName()}
                </Modal.Header>
                <Modal.Body>
                    <TextEditor onUpdateText={text => this.onUpdateText(text)}/>
                </Modal.Body>
                <Modal.Footer>
                    <div className="btn-group">
                        <Button onClick={() => this.onSave()} className="btn btn-success">Save</Button>
                        <Button onClick={() => this.onClose()} className="btn btn-default">Close</Button>
                    </div>
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showReviewForm: state.BookReducer.showReviewForm,
        editableBook: state.BookReducer.editableBook
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onClose: () => {
            dispatch(closeReviewForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ReviewForm);