import React from 'react';
import Select from 'react-select';
import { locale, getLocale } from '../../locale.jsx';

/*
    props:
    - language - current language
    - genres - array
    - onGenreChange - callback
    - onLanguageChange - callback
*/
class BookFilter extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            language: {value: null, label: 'ALL'},
            genre: {value: null, label: 'ALL'}
        };
    }

    getGenresItems() {
        let options = [{value: null, label: 'ALL'}];
        this.props.genres.forEach(genre => {
            options.push({
                value: genre,
                label: getLocale(this.props.language)[genre]
            });
        });
        return options;
    }

    onGenreChange(genre) {
        this.setState({
            genre: genre
        });
        this.props.onGenreChange(genre);
    }

    getLanguageItems() {
        let options = [{value: null, label: 'ALL'}];
        for (var lang in locale) {
            options.push({
                value: lang,
                label: locale[lang].label
            });
        }
        return options;
    }

    onLanguageChange(language) {
        this.setState({
            language: language
        });
        this.props.onLanguageChange(language);
    }

    clearFilters() {
        this.onGenreChange({value: null, label: 'ALL'});
        this.onLanguageChange({value: null, label: 'ALL'});
    }

    onSubmit(event) {
        event.preventDefault();
    }

    render() {
        return (
            <div>
                <fieldset className="scheduler-border">
                    <legend className="scheduler-border">Filters</legend>
                    <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="genre">Genre:</label>
                            <div className="col-sm-10">
                                <Select value={this.state.genre}
                                              id="genre"
                                              options={this.getGenresItems()}
                                              onChange={genre => this.onGenreChange(genre)}
                                              placeholder="Select a genre"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="language">Language:</label>
                            <div className="col-sm-10">
                                <Select value={this.state.language}
                                              id="language"
                                              options={this.getLanguageItems()}
                                              onChange={genre => this.onLanguageChange(genre)}
                                              placeholder="Select a language"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-12 text-right">
                                <button onClick={() => this.clearFilters()} type="button" className="btn btn-default">Clear filters</button>
                            </div>
                        </div>
                    </form>
                </fieldset>
            </div>
        )
    }
}

export default BookFilter;