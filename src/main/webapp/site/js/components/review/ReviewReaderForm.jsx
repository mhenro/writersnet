import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';

import {
    closeReviewReaderForm,
    getReviewDetails,
    likeReview,
    dislikeReview
} from '../../actions/ReviewActions.jsx';

import { createNotify } from '../../actions/GlobalActions.jsx';

class ReviewReaderForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            text: '',
            likes: 0,
            dislikes: 0
        };
    }

    onGetReviewDetails() {
        this.props.onGetReviewDetails(this.props.review.id, review => this.updateReview(review));
    }

    updateReview(review) {
        this.setState({
            name: review.name,
            text: review.text,
            likes: review.likes,
            dislikes: review.dislikes
        });
    }

    updateLikes(likes) {
        this.setState({
            likes: likes
        });
    }

    updateDislikes(dislikes) {
        this.setState({
            dislikes: dislikes
        });
    }

    onShow() {
        this.onGetReviewDetails();
    }

    onClose() {
        this.props.onClose();
    }

    getHeaderName() {
        return this.state.name;
    }

    getReviewText() {
        return (
            <div dangerouslySetInnerHTML={{ __html: this.state.text }} />
        )
    }

    getLikeCaption() {
        return (
            <div>
                <span className="glyphicon glyphicon-thumbs-up"></span> Like <span style={{color: '#cccccc'}}>{this.state.likes}</span>
            </div>
        )
    }

    getDislikeCaption() {
        return (
            <div>
                <span className="glyphicon glyphicon-thumbs-down"></span> Dislike <span style={{color: '#cccccc'}}>{this.state.dislikes}</span>
            </div>
        )
    }

    onLikeReview() {
        this.props.onLikeReview(this.props.review.id, likes => this.updateLikes(likes));
    }

    onDislikeReview() {
        this.props.onDislikeReview(this.props.review.id, dislikes => this.updateDislikes(dislikes));
    }

    isDataLoaded() {
        if (!this.props.review) {
            return false;
        }
        return true;
    }

    render() {
        if (!this.isDataLoaded()) {
            return null;
        }

        return (
            <Modal show={this.props.showReviewReaderForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    {this.getHeaderName()}
                </Modal.Header>
                <Modal.Body>
                    {this.getReviewText()}
                </Modal.Body>
                <Modal.Footer>
                    <div className="col-sm-6 text-left">
                        <div className="btn-group">
                            <Button onClick={() => this.onLikeReview()} className="btn btn-success">{this.getLikeCaption()}</Button>
                            <Button onClick={() => this.onDislikeReview()} className="btn btn-danger">{this.getDislikeCaption()}</Button>
                        </div>
                    </div>
                    <div className="col-sm-6 text-right">
                        <div className="btn-group">
                            <Button onClick={() => this.onClose()} className="btn btn-default">Close</Button>
                        </div>
                    </div>
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showReviewReaderForm: state.ReviewReducer.showReviewReaderForm,
        review: state.ReviewReducer.review
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onClose: () => {
            dispatch(closeReviewReaderForm());
        },

        onGetReviewDetails: (reviewId, callback) => {
            return getReviewDetails(reviewId).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.message);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onLikeReview: (reviewId, callback) => {
            return likeReview(reviewId).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.message);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onDislikeReview: (reviewId, callback) => {
            return dislikeReview(reviewId).then(([response, json]) => {
                if (response.status === 200) {
                    callback(json.message);
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

export default connect(mapStateToProps, mapDispatchToProps)(ReviewReaderForm);