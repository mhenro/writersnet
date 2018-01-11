import React from 'react';

class BookFilter extends React.Component {
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
                    <div className="control-group">
                        <label className="control-label input-label" htmlFor="startTime">Start :</label>
                        <div className="controls bootstrap-timepicker">
                            <input type="text" className="datetime" id="startTime" name="startTime" placeholder="Start Time" />
                            <i className="icon-time"></i>
                        </div>
                    </div>
                </fieldset>
            </form>
        )
    }
}

export default BookFilter;