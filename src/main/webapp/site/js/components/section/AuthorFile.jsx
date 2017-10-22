import React from 'react';

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
                        <img src={this.props.author.avatar} className="img-rounded" width="100%"/>
                    </div>
                </div>
                <br/>
                <div className="row">
                    <div className="col-sm-12" style={{textAlign: 'center'}}>
                        <div className="btn-group-vertical">
                            <button className={'btn btn-success ' + (this.props.registered && this.props.login !== this.props.author.username ? '' : 'hidden')}>Send message</button>
                            <button className={'btn btn-success ' + (this.props.registered && this.props.login !== this.props.author.username? '' : 'hidden')}>Add to friends</button>
                            <button className={'btn btn-success ' + (this.props.registered && this.props.login === this.props.author.username ? '' : 'hidden')}>Options</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AuthorFile;