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
                <Slider {...settings} ref={(s) => this.slider = s}>
                    <div className="align-middle">
                        <br/><br/><br/><br/><br/><br/><br/><br/><br/>
                        <h3 className="text-center">Welcome to WritersNets.com!</h3>
                        <br/><br/>
                        <div className="row">
                            <div className="col-sm-4">
                                <img width="200" height="auto" src="https://localhost/css/images/author.png"/><br/>
                                We are already <span className="big-counter">150</span>!
                            </div>
                            <div className="col-sm-4">
                                <img width="200" height="auto" src="https://localhost/css/images/books.png"/><br/>
                                We have <span className="big-counter">100</span> books!
                            </div>
                            <div className="col-sm-4">
                                <img width="200" height="auto" src="https://localhost/css/images/online.jpg"/><br/>
                                <span className="big-counter">25</span> authors online!
                            </div>
                        </div>
                        <br/><br/><br/><br/><br/><br/><br/><br/><br/>
                    </div>
                    <div><h3>WritersNets.com is a simple</h3>You can easily create your account and start to add your novels! We show you how it simple in the next screens.
                        <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
                    </div>
                    <div><h3>WritersNets.com is a simple</h3>You can easily create your account and start to add your novels! We show you how it simple in the next screens.
                        <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
                    </div>
                    <div><h3>WritersNets.com is a simple</h3>You can easily create your account and start to add your novels! We show you how it simple in the next screens.
                        <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
                    </div>
                    <div><h3>WritersNets.com is a simple</h3>You can easily create your account and start to add your novels! We show you how it simple in the next screens.
                        <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
                    </div>
                </Slider>
            </div>
        )
    }
}

export default TitlePage;