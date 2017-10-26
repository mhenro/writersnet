import React from 'react';
import { Link } from 'react-router-dom';

/*
    props:
    - author
    - registered
    - login - user id
 */
class AuthorFile extends React.Component {
    render() {
        return (
            <div className="panel panel-default" style={{padding: '10px'}}>
                <div className="row">
                    <div className="col-sm-12" style={{textAlign: 'center'}}>
                        <img src={'data:image/png;base64,' + this.props.author.avatar} className="img-rounded" width="100%" height="auto"/>
                    </div>
                </div>
                <br/>
                <div className="row">
                    <div className="col-sm-12" style={{textAlign: 'center'}}>
                        <div className="btn-group-vertical">
                            <button className={'btn btn-success ' + (this.props.registered && this.props.login !== this.props.author.username ? '' : 'hidden')}>Send message</button>
                            <br/>
                            <button className={'btn btn-success ' + (this.props.registered && this.props.login !== this.props.author.username? '' : 'hidden')}>Add to friends</button>
                            <br/>
                            <Link to="/options" className={'btn btn-success ' + (this.props.registered && this.props.login === this.props.author.username ? '' : 'hidden')}>Options</Link>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AuthorFile;