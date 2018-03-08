import React from 'react';
import { connect } from 'react-redux';
import { Modal, Button, Tooltip, OverlayTrigger } from 'react-bootstrap';
import { Pagination } from 'react-bootstrap';

import { closeSearchAuthorForm } from '../../actions/ContestActions.jsx';
import { createNotify } from '../../actions/GlobalActions.jsx';

class SearchAuthorForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activePage: 1,
            totalPages: 1
        };
    }

    onSelect() {

    }

    onShow() {

    }

    onClose() {
        this.props.onClose();
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        //this.props.onGetAllContests(page, data => this.updateContests(data));
    }

    renderTable() {
        return (
            <table>

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

    render() {
        return (
            <Modal show={this.props.showSearchAuthorForm} onHide={() => this.onClose()} onShow={() => this.onShow()}>
                <Modal.Header>
                    Select authors
                </Modal.Header>
                <Modal.Body>
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
                    {this.renderTable()}
                </Modal.Body>
                <Modal.Footer>
                    {this.renderFooterButtons()}
                </Modal.Footer>
            </Modal>
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
        onClose: () => {
            dispatch(closeSearchAuthorForm());
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchAuthorForm);