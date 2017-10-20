import React from 'react';
import AuthorListItem from './AuthorListItem.jsx';

/*
    props:
    - authors - array of authors
 */
class AuthorList extends React.Component {
    render() {
        return (
            <div>
                {
                    this.props.authors.map((author, key) => {
                        return (
                            <AuthorListItem key={key} author={author}/>
                        )
                    })
                }
            </div>
        )
    }
}

export default AuthorList;