import React from 'react';

/*
    props:
    - series
 */
class BookSerieList extends React.Component {
    render() {
        return (
            <div>
                {this.props.series.map((serie, key) => {
                    return (
                        <div className="row" key={key}>
                            <div className="col-sm-12">
                                <div className="panel panel-default">
                                    <div className="panel-heading">
                                        {serie.name}
                                    </div>
                                    <div className="panel-body">
                                        <div>book1</div>
                                        <div>book2</div>
                                        <div>book3</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )
                })}
            </div>
        )
    }
}

export default BookSerieList;