import React from 'react';

/*
    props:
    - contentHeight
 */
class Footer extends React.Component {
    isFixed() {
        return this.props.contentHeight < screen.availHeight;
    }

    render() {
        return (
            <div>
                <br/><br/>
                <footer className={'container-fluid ' + (this.isFixed() ? 'navbar-fixed-bottom' : '')}>
                    <div className="col-sm-12 text-center">
                        © 2018 "WritersNets.com"
                    </div>
                </footer>
            </div>
        )
    }
}

export default Footer;