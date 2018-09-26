import React from 'react';

/*
 props:
 - complaints - array of complaints
 - activePage - current page number
 - pageSize - number of elements on the page
 */
class AuthorComplaintList extends React.Component {
    renderComplaints() {
        let keyOffset = (this.props.activePage - 1) * this.props.pageSize
        return this.props.complaints.map((complaint, key) => {
            return (
                <tr key={key}>
                    <td>{key + keyOffset + 1}</td>
                    <td>{complaint.authorFullName}</td>
                    <td>{complaint.complaintText}</td>
                </tr>
            )
        })
    }

    render() {
        return (
            <div>
                <table className="table table-hover">
                    <thead>
                    <tr>
                        <td>#</td>
                        <td>From</td>
                        <td>Message</td>
                    </tr>
                    </thead>
                    <tbody>
                        {this.renderComplaints()}
                    </tbody>
                </table>
            </div>
        )
    }
}

export default AuthorComplaintList;