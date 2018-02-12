import React from 'react';
import { getHost } from '../../utils.jsx';

/*
    props:
    - gift
 */
class GiftListItem extends React.Component {
    getName() {
        return this.props.gift.name;
    }

    getDescription() {
        return this.props.gift.description;
    }

    getCost() {
        return parseFloat(this.props.gift.cost / 100).toFixed(2) + ' cr.';
    }

    getImage() {
        return this.props.gift.image;
    }

    render() {
        return (
            <div className="col-sm-3">
                <div className="panel panel-default">
                    <div className="panel-body">
                        <div className="col-sm-12 text-right">
                            {this.getCost()}
                        </div>
                        <div className="col-sm-12">
                            <img src={this.getImage()} title={this.getDescription()} width="100%" height="auto"/>
                        </div>
                    </div>
                    <div className="panel-footer text-center">
                        {this.getName()}
                    </div>
                </div>
            </div>
        )
    }
}

export default GiftListItem;