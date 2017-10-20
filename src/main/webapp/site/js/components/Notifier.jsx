import React from 'react';
import { connect } from 'react-redux';
import {AlertList} from 'react-bs-notifier';

import { removeNotify } from '../actions/GlobalActions.jsx';

class Notifier extends React.Component {
    constructor(props) {
        super(props);

        ['onAlertDismissed'].map(fn => this[fn] = this[fn].bind(this));
    }

    onAlertDismissed(alert) {
        this.props.removeNotify(alert);
    }

    render() {
        return (
            <div>
                <AlertList
                    position="top-right"
                    alerts={this.props.alerts}
                    timeout={3000}
                    dismissTitle="Close popup"
                    onDismiss={this.onAlertDismissed}
                />
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        alerts: state.GlobalReducer.alerts
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        removeNotify: (alert) => {
            dispatch(removeNotify(alert));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Notifier);