import React from 'react';
import { connect } from 'react-redux';
import { Pagination } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { Modal, Button, Tooltip, OverlayTrigger } from 'react-bootstrap';
import { createNotify } from '../../actions/GlobalActions.jsx';
import { closeComplaintForm, makeComplaint, getAllComplaints } from '../../actions/AuthorActions.jsx';
import AuthorComplaintList from '../../components/section/AuthorComplaintList.jsx';

/*
    props:
    - author
    - me - boolean
    - onGetAuthorDetails - callback
 */
class ComplaintForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activePage: 1,
            pageSize: 10,
            totalPages: 1,
            complaints: [],
            complaintText: ''
        };
    }

    onShow() {
        this.props.onGetAllComplaints(this.props.author.username, 0, this.state.pageSize, (page) => this.updatePage(page))
    }

    onClose() {
        this.props.onCloseComplaintForm();
        this.setState({
            complaintText: ''
        })
    }

    onMakeComplaint() {
        let complaintRequest = {
            userId: this.props.author.username,
            text: this.state.complaintText
        };
        this.props.onMakeComplaint(complaintRequest, this.props.token, this.props.onGetAuthorDetails, () => this.onClose());
    }

    updatePage(page) {
        this.setState({
            totalPages: page.totalPages,
            activePage: page.number + 1,
            complaints: page.content
        });
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        this.props.onGetAllComplaints(this.props.author.username, page, this.state.pageSize, (page) => this.updatePage(page))
    }

    onFieldChange(proxy) {
        switch (proxy.target.id) {
            case 'complaint_text': this.setState({complaintText: proxy.target.value}); break;
        }
    }

    onSubmit(event) {
        event.preventDefault();
        // let author = {
        //     username: this.props.login,
        //     firstName: this.state.firstName,
        //     lastName: this.state.lastName,
        //     sectionName: this.state.sectionName,
        //     sectionDescription: this.state.sectionDescription,
        //     birthday: this.state.birthday,
        //     city: this.state.city,
        //     language: this.state.siteLanguage.value,
        //     preferredLanguages: this.state.preferredLanguages.map(lang => lang.value).reduce((cur, next) => cur + ';' + next)
        // };
        // this.props.onSaveAuthor(author, this.props.token, () => this.props.onGetAuthorDetails(this.props.login, () => this.updateState()));
    }

    renderBody() {
        return (
            <Modal.Body>
                <div className="row">
                    <div className="col-sm-12">
                        <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                            <div className="form-group">
                                <label className="control-label col-sm-4" htmlFor="complaint_text">Complaint message</label>
                                <div className="col-sm-8">
                                    <input value={this.state.complaintText} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="complaint_text" placeholder="Enter your complaint" name="complaint_text"/>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
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
                        <AuthorComplaintList complaints={this.state.complaints}
                                             activePage={this.state.activePage}
                                             pageSize={this.state.pageSize}
                        />
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
                {this.props.me ? null : <Button onClick={() => this.onMakeComplaint()} className="btn btn-danger">Make a complaint</Button>}
                <Button onClick={() => this.onClose()} className="btn btn-default">Close</Button>
            </div>
        )
    }

    render() {
        return (
            <Modal show={this.props.showComplaintForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    Make a complaint to this author
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
        showComplaintForm: state.AuthorReducer.showComplaintForm,
        token: state.GlobalReducer.token
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onMakeComplaint: (complaintRequest, token, callback, closeCallback) => {
            if (complaintRequest.text.length === 0) {
                dispatch(createNotify('warning', 'Warning', 'Complaint message should not be empty'));
                return
            }
            makeComplaint(complaintRequest, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'The complaint was made successfully'));
                    callback();
                    closeCallback();
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onGetAllComplaints: (userId, page, size, callback) => {
            getAllComplaints(userId, page - 1, size).then(([response, json]) => {
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

        onCloseComplaintForm: () => {
            dispatch(closeComplaintForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ComplaintForm);