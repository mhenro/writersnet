import React from 'react';
import { Pagination } from 'react-bootstrap';

import ContestEditForm from '../components/contests/ContestEditForm.jsx';
import ContestManagementForm from '../components/contests/ContestManagementForm.jsx';
import ContestList from '../components/contests/ContestList.jsx';

class ContestPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activePage: 1,
            totalPages: 1
        };
    }

    pageSelect(page) {
        this.setState({
            activePage: page
        });

        //this.props.onGetAuthors(this.state.currentName, page, this.state.size, this.state.sortBy, totalPages => this.setTotalPages(totalPages));
    }

    render() {
        return (
            <div>
                <div className="col-sm-12 text-center">
                    <button className="btn btn-success">Create new contest</button>
                    <br/>
                    <br/>
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
                <div className="col-sm-12 text-center">
                    <ContestList/>
                    <br/>
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

                {/* Contest popup form */}
                <ContestEditForm/>

                {/* Contest management popup form */}
                <ContestManagementForm/>
            </div>
        )
    }
}

export default ContestPage;