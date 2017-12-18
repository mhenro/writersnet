import React from 'react';
import Slider from 'react-slick';

class TitlePage extends React.Component {
    componentDidMount() {

    }

    wheel(e) {
        let delta = e.deltaY;
        if (delta > 0) {
            this.slider.slickNext();
        } else {
            this.slider.slickPrev();
        }
    }

    render() {
        let settings = {
            dots: true,
            vertical: true,
            adaptiveHeight: true,
            infinite: false
        };
        return (
            <div onWheel={(e) => this.wheel(e)}>
                    TITLE
                <Slider {...settings} ref={(s) => this.slider = s}>
                    <div><h3>WritersNets.com is a simple</h3>You can easily create your account and start to add your novels! We show you how it simple in the next screens.</div>
                    <div><h3>WritersNets.com is a simple</h3>You can easily create your account and start to add your novels! We show you how it simple in the next screens.</div>
                    <div><h3>WritersNets.com is a simple</h3>You can easily create your account and start to add your novels! We show you how it simple in the next screens.</div>
                    <div><h3>WritersNets.com is a simple</h3>You can easily create your account and start to add your novels! We show you how it simple in the next screens.</div>
                    <div><h3>WritersNets.com is a simple</h3>You can easily create your account and start to add your novels! We show you how it simple in the next screens.</div>
                </Slider>
            </div>
        )
    }
}

export default TitlePage;