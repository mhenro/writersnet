import React from 'react';

/*
    props:
    - scrollStepInPx
    - delayInMs
 */
class ScrollToTopButton extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isVisible: false,
            intervalId: 0
        };
        window.onscroll = () => {this.onScroll()};

        ['scrollStep', 'scrollToTop'].map(fn => this[fn] = this[fn].bind(this));
    }

    onScroll() {
        this.setState({
            isVisible: window.pageYOffset !== 0
        });
    }

    scrollStep() {
        if (window.pageYOffset === 0) {
            clearInterval(this.state.intervalId);
        }
        window.scroll(0, window.pageYOffset - this.props.scrollStepInPx);
    }

    scrollToTop() {
        let intervalId = setInterval(this.scrollStep, this.props.delayInMs);
        this.setState({
            intervalId: intervalId
        });
    }

    render() {
        if (!this.state.isVisible) {
            return null;
        }
        return(
            <div>
                <button title="Back to top" className="scroll"
                        onClick={this.scrollToTop}>
                    <span className='arrow-up glyphicon glyphicon-chevron-up'></span>
                </button>
            </div>
        )
    }
}

export default ScrollToTopButton;