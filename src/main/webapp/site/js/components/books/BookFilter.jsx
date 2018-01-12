import React from 'react';
import Select from 'react-select';

class BookFilter extends React.Component {
    loadGenres(value) {
    }

    onGenreChange(genre) {

    }

    filterOption(option, filter) {

    }

    onSubmit(event) {
        event.preventDefault();
    }

    render() {
        return (
            <form>
                {/*<fieldset className="form-group">
                    <legend>Filters</legend>
                    <div className="form-check">
                        <label className="form-check-label">
                            <input type="checkbox" className="form-check-input" />
                            Check me out
                        </label>
                    </div>
                </fieldset>*/}
                <fieldset className="scheduler-border">
                    <legend className="scheduler-border">Filters</legend>
                    <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="genre">Genre:</label>
                            <div className="col-sm-10">
                                <Select.Async value=""
                                              id="genre"
                                              loadOptions={value => this.loadGenres(value)}
                                              onChange={genre => this.onGenreChange(genre)}
                                              filterOption={(option, filter) => this.filterOption(option, filter)}
                                              noResultsText="Nothing found"
                                              loadingPlaceholder="Searching..."
                                              placeholder="Select a genre"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="control-label col-sm-2" htmlFor="language">Language:</label>
                            <div className="col-sm-10">
                                <Select.Async value=""
                                              id="language"
                                              loadOptions={value => this.loadGenres(value)}
                                              onChange={genre => this.onGenreChange(genre)}
                                              filterOption={(option, filter) => this.filterOption(option, filter)}
                                              noResultsText="Nothing found"
                                              loadingPlaceholder="Searching..."
                                              placeholder="Select a language"/>
                            </div>
                        </div>
                    </form>
                </fieldset>
            </form>
        )
    }
}

export default BookFilter;