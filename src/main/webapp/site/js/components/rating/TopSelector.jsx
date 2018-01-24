import React from 'react';
import { getLocale } from '../../locale.jsx';

/*
    props:
    - onTopClick - callback function
    - book
    - author
    - language
 */
class TopSelector extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            activeAuthorButton: 'authorsTopByRating',
            activeBookButton: 'booksTopByRating'
        };
    }

    getButtonAuthorClass(btnName) {
        let clsDef = 'btn ',
            clsAdd = this.state.activeAuthorButton === btnName ? 'btn-success' : 'btn-primary';

        return clsDef + clsAdd;
    }

    getButtonBookClass(btnName) {
        let clsDef = 'btn ',
            clsAdd = this.state.activeBookButton === btnName ? 'btn-success' : 'btn-primary';

        return clsDef + clsAdd;
    }

    onBtnAuthorClick(btnName) {
        this.setState({
            activeAuthorButton: btnName
        });
        this.props.onTopClick(btnName);
    }

    onBtnBookClick(btnName) {
        this.setState({
            activeBookButton: btnName
        });
        this.props.onTopClick(btnName);
    }

    renderForBigScreen() {
        if (this.props.author) {
            return (
                <div className="btn-group btn-group-justified hidden-sm hidden-xs">
                    <a className={this.getButtonAuthorClass('authorsTopByRating')}
                       onClick={() => this.onBtnAuthorClick('authorsTopByRating')}>{getLocale(this.props.language)['Ratings']}</a>
                    <a className={this.getButtonAuthorClass('authorsTopByNovelsCount')}
                       onClick={() => this.onBtnAuthorClick('authorsTopByNovelsCount')}>{getLocale(this.props.language)['Novels count']}</a>
                    <a className={this.getButtonAuthorClass('authorsTopByCommentsCount')}
                       onClick={() => this.onBtnAuthorClick('authorsTopByCommentsCount')}>{getLocale(this.props.language)['Comments count']}</a>
                    <a className={this.getButtonAuthorClass('authorsTopByViewsCount')}
                       onClick={() => this.onBtnAuthorClick('authorsTopByViewsCount')}>{getLocale(this.props.language)['Views count']}</a>
                </div>
            )
        } else if (this.props.book) {
            return (
                <div className="btn-group btn-group-justified hidden-sm hidden-xs">
                    <a className={this.getButtonBookClass('booksTopByNovelty')}
                       onClick={() => this.onBtnBookClick('booksTopByNovelty')}>{getLocale(this.props.language)['Novelties']}</a>
                    <a className={this.getButtonBookClass('booksTopByRating')}
                       onClick={() => this.onBtnBookClick('booksTopByRating')}>{getLocale(this.props.language)['Ratings']}</a>
                    <a className={this.getButtonBookClass('booksTopByNovelVolume')}
                       onClick={() => this.onBtnBookClick('booksTopByNovelVolume')}>{getLocale(this.props.language)['Novel volume']}</a>
                    <a className={this.getButtonBookClass('booksTopByCommentsCount')}
                       onClick={() => this.onBtnBookClick('booksTopByCommentsCount')}>{getLocale(this.props.language)['Comments count']}</a>
                    <a className={this.getButtonBookClass('booksTopByViewsCount')}
                       onClick={() => this.onBtnBookClick('booksTopByViewsCount')}>{getLocale(this.props.language)['Views count']}</a>
                </div>
            )
        }
    }

    renderForSmallScreen() {
        if (this.props.author) {
            return (
                <div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonAuthorClass('authorsTopByRating')}
                           onClick={() => this.onBtnAuthorClick('authorsTopByRating')}>{getLocale(this.props.language)['Ratings']}</a>
                        <a className={this.getButtonAuthorClass('authorsTopByNovelsCount')}
                           onClick={() => this.onBtnAuthorClick('authorsTopByNovelsCount')}>{getLocale(this.props.language)['Novels count']}</a>
                    </div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonAuthorClass('authorsTopByCommentsCount')}
                           onClick={() => this.onBtnAuthorClick('authorsTopByCommentsCount')}>{getLocale(this.props.language)['Comments count']}</a>
                        <a className={this.getButtonAuthorClass('authorsTopByViewsCount')}
                           onClick={() => this.onBtnAuthorClick('authorsTopByViewsCount')}>{getLocale(this.props.language)['Views count']}</a>
                    </div>
                </div>
            )
        } else if (this.props.book) {
            return (
                <div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonBookClass('booksTopByNovelty')}
                           onClick={() => this.onBtnBookClick('booksTopByNovelty')}>{getLocale(this.props.language)['Novelties']}</a>
                        <a className={this.getButtonBookClass('booksTopByRating')}
                           onClick={() => this.onBtnBookClick('booksTopByRating')}>{getLocale(this.props.language)['Ratings']}</a>
                    </div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonBookClass('booksTopByNovelVolume')}
                           onClick={() => this.onBtnBookClick('booksTopByNovelVolume')}>{getLocale(this.props.language)['Novel volume']}</a>
                    </div>
                    <div className="btn-group btn-group-justified visible-sm visible-xs">
                        <a className={this.getButtonBookClass('booksTopByCommentsCount')}
                           onClick={() => this.onBtnBookClick('booksTopByCommentsCount')}>{getLocale(this.props.language)['Comments count']}</a>
                        <a className={this.getButtonBookClass('booksTopByViewsCount')}
                           onClick={() => this.onBtnBookClick('booksTopByViewsCount')}>{getLocale(this.props.language)['Views count']}</a>
                    </div>
                </div>
            )
        }
    }

    render() {
        return (
            <div>
                {getLocale(this.props.language)['Top by:']}
                {this.renderForBigScreen()}
                {this.renderForSmallScreen()}
            </div>
        )
    }
}

export default TopSelector;