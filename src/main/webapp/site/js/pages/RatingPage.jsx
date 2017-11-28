import React from 'react';
import { connect } from 'react-redux';

import TopSelector from '../components/rating/TopSelector.jsx';
import TopTable from '../components/rating/TopTable.jsx';

class RatingPage extends React.Component {
    render() {
        return (
            <div>
                <div className="col-sm-12">
                    <TopSelector/>
                </div>
                <div className="col-sm-12">
                    <br/>
                </div>
                <div className="col-sm-12">
                    <TopTable author={true}/>
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