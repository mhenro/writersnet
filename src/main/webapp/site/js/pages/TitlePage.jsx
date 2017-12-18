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
            infinite: false
        };
        return (
            <div onWheel={(e) => this.wheel(e)}>
                    TITLE
                <Slider {...settings} ref={(s) => this.slider = s}>
                    <div><h3>1</h3></div>
                    <div><h3>2</h3></div>
                    <div><h3>3</h3></div>
                    <div><h3>4</h3></div>
                    <div><h3>5</h3></div>
                    <div><h3>6</h3></div>
                </Slider>
            </div>
        )
    }
}

export default TitlePage;