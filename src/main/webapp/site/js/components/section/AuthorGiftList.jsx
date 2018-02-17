import React from 'react';
import AuthorGiftListItem from './AuthorGiftListItem.jsx';

/*
 props:
 - gifts - array of gifts
 */
class AuthorGiftList extends React.Component {
    renderGifts() {
        let result = [],
            tempArr = [],
            count = Math.ceil(this.props.gifts.length / 4),
            i;
        for (i = 0; i < this.props.gifts.length; i++) {
            if (i % 4 === 0) {
                ++count;
                result.push(
                    <div className="row" key={i}>
                        {tempArr}
                        <div className="col-sm-12">
                            <br/>
                        </div>
                    </div>
                );
                tempArr = [];
            }

            tempArr.push(
                <AuthorGiftListItem gift={this.props.gifts[i]} key={i}/>
            );
        }
        result.push(
            <div className="row" key={i}>
                {tempArr}
                <div className="col-sm-12">
                    <br/>
                </div>
            </div>
        );
        return result;
    }

    render() {
        return (
            <div>
                {this.renderGifts()}
            </div>
        )
    }
}

export default AuthorGiftList;