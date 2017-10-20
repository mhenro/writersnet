import React from 'react';
import { Link } from 'react-router-dom';

/*
    props:
    - author
 */
class AuthorListItem extends React.Component {
    render() {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <div className="row">
                        <div className="col-sm-4">
                            <img src={this.props.author.avatar} className="img-rounded" alt="avatar" width="150" height="200"/>
                        </div>
                        <div className="col-sm-8">
                            <div>
                                {this.props.author.firstName + ' ' + this.props.author.lastName}
                            </div>
                            <div>
                                <span className="glyphicon glyphicon-heart"></span>&nbsp;
                                5.00 * 1542
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <Link to={'authors/' + this.props.author.username} className="btn btn-success btn-sm">Author page</Link>
                                    {/*<button className="btn btn-success">Author page</button>*/}
                                </div>
                                <div className="col-sm-6">
                                    <span>offline</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AuthorListItem;