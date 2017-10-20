import React from 'react';
import { getQueryParams } from '../utils.jsx';
import { connect } from 'react-redux';
import { sendActivationToken } from '../actions/AuthActions.jsx';
import { createNotify } from '../actions/GlobalActions.jsx';

class GlobalDataContainer extends React.Component {
    componentDidMount() {
        let query = getQueryParams(document.location.search);
        let activationToken = query.activationToken;
        if (activationToken) {
            this.props.onSendActivationToken(activationToken);
        }
    }

    render() {
        return (
            <div></div>
        )
    }
}

const mapStateToProps = (state) => {
    return {

    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onSendActivationToken: (activationToken) => {
            return sendActivationToken(activationToken).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('info', 'Info', 'User activation was completed! Please log-in.'));
                }
                else if (response.status === 403) {
                    dispatch(createNotify('danger', 'Error', 'Activation user error'));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(GlobalDataContainer);