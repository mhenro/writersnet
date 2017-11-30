import React from 'react';

/*
    props:
    - onTopClick - callback function
    - book
    - author
 */
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
        this.props.onTopClick(btnName);
    }

    renderForBigScreen() {
        if (this.props.author) {
            return (
                <div className="btn-group btn-group-justified hidden-sm hidden-xs">
                    <a className={this.getButtonClass('ratings')} onClick={() => this.onBtnClick('ratings')}>Ratings</a>
                    <a className={this.getButtonClass('novelsCount')} onClick={() => this.onBtnClick('novelsCount')}>Novels
                        count</a>
                    <a className={this.getButtonClass('novelsVolume')} onClick={() => this.onBtnClick('novelsVolume')}>Novels
                        volume</a>
                    <a className={this.getButtonClass('commentsCount')}
                       onClick={() => this.onBtnClick('commentsCount')}>Comments count</a>
                    <a className={this.getButtonClass('viewsCount')} onClick={() => this.onBtnClick('viewsCount')}>Views
                        count</a>
                </div>
            )
        } else if (this.props.book) {
            return (
                <div className="btn-group btn-group-justified hidden-sm hidden-xs">
                    <a className={this.getButtonClass('novelties')} onClick={() => this.onBtnClick('novelties')}>Novelties</a>
                    <a className={this.getButtonClass('ratings')} onClick={() => this.onBtnClick('ratings')}>Ratings</a>
                    <a className={this.getButtonClass('novelsVolume')} onClick={() => this.onBtnClick('novelsVolume')}>Novel
                        volume</a>
                    <a className={this.getButtonClass('commentsCount')}
                       onClick={() => this.onBtnClick('commentsCount')}>Comments count</a>
                    <a className={this.getButtonClass('viewsCount')} onClick={() => this.onBtnClick('viewsCount')}>Views
                        count</a>
                </div>
            )
        }
    }

    renderForSmallScreen() {
        if (this.props.author) {
            return (
                <div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonClass('ratings')}
                           onClick={() => this.onBtnClick('ratings')}>Ratings</a>
                    </div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonClass('novelsCount')}
                           onClick={() => this.onBtnClick('novelsCount')}>Novels count</a>
                        <a className={this.getButtonClass('novelsVolume')}
                           onClick={() => this.onBtnClick('novelsVolume')}>Novels volume</a>
                    </div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonClass('commentsCount')}
                           onClick={() => this.onBtnClick('commentsCount')}>Comments count</a>
                        <a className={this.getButtonClass('viewsCount')} onClick={() => this.onBtnClick('viewsCount')}>Views
                            count</a>
                    </div>
                </div>
            )
        } else if (this.props.book) {
            return (
                <div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonClass('novelties')} onClick={() => this.onBtnClick('novelties')}>Novelties</a>
                        <a className={this.getButtonClass('ratings')}
                           onClick={() => this.onBtnClick('ratings')}>Ratings</a>
                    </div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonClass('novelsVolume')}
                           onClick={() => this.onBtnClick('novelsVolume')}>Novel volume</a>
                    </div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonClass('commentsCount')}
                           onClick={() => this.onBtnClick('commentsCount')}>Comments count</a>
                        <a className={this.getButtonClass('viewsCount')} onClick={() => this.onBtnClick('viewsCount')}>Views
                            count</a>
                    </div>
                </div>
            )
        }
    }

    render() {
        return (
            <div>
                Top by:
                {this.renderForBigScreen()}
                {this.renderForSmallScreen()}
            </div>
        )
    }
}

export default TopSelector;