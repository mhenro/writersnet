import React from 'react';
import { getHost } from '../../utils.jsx';

/*
    props:
    - gift
    - onBuyGift - callback (giftId, msg)
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

    onBuyGift() {
        this.props.onBuyGift(this.props.gift.id, 'test message');
    }

    render() {
        return (
            <div className="col-sm-3">
                <div className="panel panel-default clickable" onClick={() => this.onBuyGift()}>
                    <div className="panel-body">
                        <div className="col-sm-12 text-right gift-price">
                            {this.getCost()}
                        </div>
                        <div className="col-sm-12">
                            <img src={this.getImage()} title={this.getDescription()} width="128px" height="128px"/>
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