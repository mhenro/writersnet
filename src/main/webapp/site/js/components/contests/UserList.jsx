import React from 'react';
import { Pagination } from 'react-bootstrap';

/*
    props:
    - listName - string
 */
class UserList extends React.Component {
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

        //this.props.onGetAllContests(page, data => this.updateContests(data));
    }

    render() {
        return (
            <div className="col-sm-12">
                <fieldset className="scheduler-border">
                    <legend className="scheduler-border">{this.props.listName}</legend>
                    <div>
                        <div className="col-sm-12 text-center">
                            <button className="btn btn-primary">Add new member</button>
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
                            <br/>
                            <br/>
                        </div>
                        <div className="col-sm-12 text-center">
                            <table className="table table-hover">
                                <tbody>
                                    <tr>
                                        <td>Body1</td>
                                        <td>
                                            <button className="btn btn-default btn-xs glyphicon glyphicon-remove" title="Remove this member from the list"></button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Body2</td>
                                        <td>
                                            <button className="btn btn-default btn-xs glyphicon glyphicon-remove" title="Remove this member from the list"></button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </fieldset>
            </div>
        )
    }
}

export default UserList;