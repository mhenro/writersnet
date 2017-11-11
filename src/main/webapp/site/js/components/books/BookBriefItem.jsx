import React from 'react';

/*
    props:
 */
class BookBriefItem extends React.Component {
    render() {
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <div className="row">
                        <div className="col-sm-2">
                            <img src="/css/images/avatar.png" className="img-rounded" />
                        </div>
                        <div className="col-sm-8">
                            <table className="table">
                                <tbody>
                                    <tr>
                                        <td>Название</td>
                                        <td>Название</td>
                                    </tr>
                                    <tr>
                                        <td>Автор</td>
                                        <td>Название</td>
                                    </tr>
                                    <tr>
                                        <td>Жанр</td>
                                        <td>Название</td>
                                    </tr>
                                    <tr>
                                        <td>Описание</td>
                                        <td>Название</td>
                                    </tr>
                                    <tr>
                                        <td>Год</td>
                                        <td>Название</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div className="col-sm-2">
                            <img src="/css/images/avatar.png" className="img-circle" />
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-12">
                        <div className="btn-toolbar" style={{margin: '0 0 10px 10px'}}>
                            <a href="#" className="btn btn-success btn-sm">Read</a>
                            <a href="#" className="btn btn-success btn-sm">Go to writer</a>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default BookBriefItem;