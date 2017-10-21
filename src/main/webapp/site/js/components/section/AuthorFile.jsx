import React from 'react';

/*
    props:
    - author
 */
class AuthorFile extends React.Component {
    render() {
        return (
            <div>
                <div className="row">
                    <div className="col-sm-12" style={{textAlign: 'center'}}>
                        <img src={this.props.author.avatar} className="img-rounded" width="200px" height="200px"/>
                    </div>
                </div>
                <br/>
                <div className="row">
                    <div className="col-sm-12" style={{textAlign: 'center'}}>
                        <div className="btn-group-vertical">
                            <button className="btn btn-success">Send message</button>
                            <button className="btn btn-success">Add to friends</button>
                            <button className="btn btn-success">Options</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AuthorFile;