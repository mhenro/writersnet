import React from 'react';
import { connect } from 'react-redux';

import TopSelector from '../components/rating/TopSelector.jsx';
import TopTable from '../components/rating/TopTable.jsx';

class RatingPage extends React.Component {
    constructor(props) {
        super(props);
        ['onTopClick'].map(fn => this[fn] = this[fn].bind(this));
    }

    onTopClick(topName) {
        console.log('select top ' + topName);
    }

    render() {
        return (
            <div>
                <h4>Authors tops</h4>
                <div className="col-sm-12">
                    <TopSelector author={true} onTopClick={this.onTopClick}/>
                </div>
                <div className="col-sm-12">
                    <br/>
                </div>
                <div className="col-sm-12">
                    <TopTable author={true}/>
                </div>

                <h4>Books tops</h4>
                <div className="col-sm-12">
                    <TopSelector book={true} onTopClick={this.onTopClick}/>
                </div>
                <div className="col-sm-12">
                    <br/>
                </div>
                <div className="col-sm-12">
                    <TopTable book={true}/>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(RatingPage);