import React from 'react';
import { Panel, Accordion } from 'react-bootstrap';
import { GiftCategory } from '../../utils.jsx';
import GiftListItem from './GiftListItem.jsx';

/*
    props:
    - gifts - array
*/
class GiftList extends React.Component {
    renderGifts(gifts) {
        let result = [],
            tempArr = [],
            count = Math.ceil(gifts.length / 4),
            i;
        for (i = 0; i < gifts.length; i++) {
            if (i % 4 === 0) {
                ++count;
                result.push(
                    <div className="row" key={i}>
                        {tempArr}
                    </div>
                );
                tempArr = [];
            }

            tempArr.push(
                <GiftListItem gift={gifts[i]} key={i}/>
            );
        }
        result.push(
            <div className="row" key={i}>
                {tempArr}
            </div>
        );
        return result;
    }

    renderCategories() {
        let result = [],
            key = 0;
        for (let giftCategory in this.props.gifts) {
            let categoryName = GiftCategory.valueOf(giftCategory);
            result.push(
                <Panel header={categoryName} eventKey={key} key={key}>
                    {this.renderGifts(this.props.gifts[giftCategory])}
                </Panel>
            );
            ++key;
        }
        return result;
    }

    render() {
        return (
            <Accordion>
                {this.renderCategories()}
            </Accordion>
        )
    }
}

export default GiftList;