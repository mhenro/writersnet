import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { Modal, Button, Tooltip, OverlayTrigger } from 'react-bootstrap';
import PropTypes from 'prop-types';
import { createNotify, setGiftedUser } from '../../actions/GlobalActions.jsx';
import { closeAuthorGiftsForm, getAuthorGifts } from '../../actions/AuthorActions.jsx';

import AuthorGiftList from './AuthorGiftList.jsx';

/*
    props:
    - author
 */
class AuthorGiftForm extends React.Component {
    static contextTypes = {
        router: PropTypes.shape({
            history: PropTypes.shape({
                push: PropTypes.func.isRequired,
                replace: PropTypes.func.isRequired
            }).isRequired,
            staticContext: PropTypes.object
        }).isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            activePage: 1,
            totalPages: 1,
            gifts: []
        };
    }

    onShow() {
        this.props.onGetAuthorGifts(this.props.author.username, this.state.activePage, page => this.updatePage(page));
    }

    onClose() {
        this.props.onCloseAuthorGiftsForm();
    }

    onBuy() {
        this.props.onSetGiftedUser(this.props.author.username);
        this.onClose();
        this.context.router.history.push('/gifts');
    }

    updatePage(page) {
        this.setState({
            totalPages: page.totalPages,
            activePage: page.number + 1,
            gifts: page.content
        });
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        this.props.onGetAuthorGifts(this.props.author.username, page, page => this.updatePage(page));
    }

    onSelectGift() {
        console.log('gift is selected');
    }

    renderBody() {
        return (
            <Modal.Body>
                <div className="row">
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
                    <div className="col-sm-12">
                        <AuthorGiftList gifts={this.state.gifts}/>
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
            </Modal.Body>
        )
    }

    renderFooterButtons() {
        return (
            <div className="btn-group">
                <Button onClick={() => this.onBuy()} className="btn btn-success">Give a gift</Button>
                <Button onClick={() => this.onClose()} className="btn btn-default">Close</Button>
            </div>
        )
    }

    render() {
        return (
            <Modal show={this.props.showAuthorGiftsForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    Author's gifts
                </Modal.Header>
                {this.renderBody()}
                <Modal.Footer>
                    {this.renderFooterButtons()}
                </Modal.Footer>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        showAuthorGiftsForm: state.AuthorReducer.showAuthorGiftsForm
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthorGifts: (userId, page, callback) => {
            return getAuthorGifts(userId, page - 1).then(([response, json]) => {
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

        onSetGiftedUser: (userId) => {
            dispatch(setGiftedUser(userId));
        },

        onCloseAuthorGiftsForm: () => {
            dispatch(closeAuthorGiftsForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(AuthorGiftForm);