import React from 'react';

class TopSelector extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activeButton: 'novelties'
        };
    }

    getButtonClass(btnName) {
        let clsDef = 'btn ',
            clsAdd = this.state.activeButton === btnName ? 'btn-success' : 'btn-primary';

        return clsDef + clsAdd;
    }

    onBtnClick(btnName) {
        this.setState({
            activeButton: btnName
        });
    }

    render() {
        return (
            <div>
                Top by:
                <div className="btn-group btn-group-justified">
                    <a className={this.getButtonClass('novelties')} onClick={() => this.onBtnClick('novelties')}>Novelties</a>
                    <a className={this.getButtonClass('ratings')} onClick={() => this.onBtnClick('ratings')}>Ratings</a>
                    <a className={this.getButtonClass('novelsCount')} onClick={() => this.onBtnClick('novelsCount')}>Novels count</a>
                    <a className={this.getButtonClass('novelsVolume')} onClick={() => this.onBtnClick('novelsVolume')}>Novels volume</a>
                    <a className={this.getButtonClass('commentsCount')} onClick={() => this.onBtnClick('commentsCount')}>Comments count</a>
                    <a className={this.getButtonClass('viewsCount')} onClick={() => this.onBtnClick('viewsCount')}>Views count</a>
                </div>
            </div>
        )
    }
}

export default TopSelector;