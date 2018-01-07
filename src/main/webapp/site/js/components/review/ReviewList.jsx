import React from 'react';
import ReviewListItem from './ReviewListItem.jsx';

/*
    props:
    - reviews - array
    - onOpenReview - callback
*/
class ReviewList extends React.Component {
    renderItems() {
        return this.props.reviews.map((review, key) => {
            return <ReviewListItem review={review} key={key}
                                   onOpenReview={this.props.onOpenReview}/>
        });
    }

    render() {
        return (
           <div>
               {this.renderItems()}
           </div>
        )
    }
}

export default ReviewList;