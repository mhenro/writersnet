import React from 'react';
import { Panel, Accordion } from 'react-bootstrap';
import { GiftCategory } from '../../utils.jsx';
import GiftListItem from './GiftListItem.jsx';

/*
    props:
    - gifts - array
*/
class GiftList extends React.Component {
    getGiftsFromCategory(category) {
        return this.props.gifts.filter(gift => gift.category === category);
    }

    getGiftCategoriesFromGifts() {
        let existed = [];
        return this.props.gifts.filter(gift => {
            if (existed.some(category => category === GiftCategory.valueOf(gift.category))) {
                return false;
            }
            existed.push(GiftCategory.valueOf(gift.category));
            return true;
        }).map(gift => {
            let category = {
                name: GiftCategory.valueOf(gift.category),
                gifts: this.getGiftsFromCategory(gift.category)
            };
            return category;
        });
    }

    renderGifts(gifts) {
        return gifts.map((gift, key) => {
            return <GiftListItem gift={gift} key={key}/>
        });
    }

    renderCategories() {
        return this.getGiftCategoriesFromGifts().map((category, key) => {
            return (
                <Panel header={category.name} eventKey={key} key={key}>
                    {this.renderGifts(category.gifts)}
                </Panel>
            )
        });
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