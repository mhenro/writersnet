import React from 'react';
import { connect } from 'react-redux'

class RatingPage extends React.Component {
    render() {
        return (
            <div>
                Rating page
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