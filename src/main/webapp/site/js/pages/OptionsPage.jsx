import React from 'react';
import { connect } from 'react-redux';
import DatePicker from 'react-bootstrap-datetimepicker';
import Select from 'react-select';
import {
    getAuthorDetails,
    setAuthor
} from '../actions/AuthorActions.jsx';
import {
    createNotify
} from '../actions/GlobalActions.jsx';

/*
    props:
    - author

 */
class OptionsPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            firstName: '',
            birthday: new Date().toISOString(),
            siteLanguage: {value: 'EN', label: 'English'},
            preferredLanguages: []
        };

        ['onSubmit', 'onDateChange', 'onLanguageChange', 'onMultiLanguageChange', 'updateForm'].map(fn => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        if (!this.props.author) {
            this.props.onGetAuthorDetails(this.props.login, this.updateForm);
        } else {
            this.updateForm(this.props.author);
        }
    }

    updateForm(author) {
        this.setState({
            firstName: author.firstName,
            lastName: author.lastName,
            sectionName: author.section.name,
            //TODO: birthday: ???
            city: author.city,
            siteLanguage: {value: author.language, label: author.language}
            //TODO: preferred languages
        });
    }

    onDateChange(value) {
        this.setState({
            birthday: value
        });
    }

    onLanguageChange(siteLanguage) {
        this.setState({
           siteLanguage: siteLanguage
        });
        console.log('you selected ' + siteLanguage.label);
    }

    onMultiLanguageChange(preferredLanguages) {
        this.setState({
            preferredLanguages: preferredLanguages
        });
        for (var key in preferredLanguages) {
            console.log('you selected ' + preferredLanguages[key].label);
        }
    }

    getDatePickerProps() {
        return {
            placeholder: 'Enter your date of birth',
            readOnly: true
        }
    }

    onSubmit(event) {
        event.preventDefault();
    }

    render() {
        const options = [
            { value: 'RU', label: 'Русский' },
            { value: 'EN', label: 'English' }
        ];

        return (
            <div>
                <div className="panel panel-default">
                    <div className="panel-heading">
                        General
                    </div>
                    <div className="panel-body">
                        <form className="form-horizontal" onSubmit={this.onSubmit}>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="first_name">First name:</label>
                                <div className="col-sm-10">
                                    <input value={this.state.firstName} type="text" className="form-control" id="first_name" placeholder="Enter your first name" name="first_name"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="last_name">Last name:</label>
                                <div className="col-sm-10">
                                    <input value={this.state.lastName} type="text" className="form-control" id="last_name" placeholder="Enter your last name" name="last_name"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="section_name">Section name:</label>
                                <div className="col-sm-10">
                                    <input value={this.state.sectionName} type="text" className="form-control" id="section_name" placeholder="Enter the name of your section" name="section_name"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="birthday">Birthday:</label>
                                <div className="col-sm-10">
                                    <DatePicker inputProps={this.getDatePickerProps()} defaultText="" inputFormat="DD-MM-YYYY" mode="date"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="city">City:</label>
                                <div className="col-sm-10">
                                    <input value={this.state.city} type="text" className="form-control" id="city" placeholder="Enter your city" name="city"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="language">Language:</label>
                                <div className="col-sm-10">
                                    <Select value={this.state.siteLanguage} options={options} onChange={this.onLanguageChange} placeholder="Choose website language"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="language">Preferred languages:</label>
                                <div className="col-sm-10">
                                    <Select value={this.state.preferredLanguages} options={options} multi={true} onChange={this.onMultiLanguageChange} placeholder="Choose your preferred languages"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-12" style={{textAlign: 'center'}}>
                                    <button type="submit" className="btn btn-success">Save</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <div className="panel panel-default">
                    <div className="panel-heading">
                        Avatar
                    </div>
                    <div className="panel-body">
                        <div className="col-sm-4">
                            <img src="" className="img-rounded" width="200" height="300"/>
                        </div>
                        <div className="col-sm-8">
                            <div className="btn-group-vertical">
                                <button type="button" className="btn btn-success">Load new photo</button>
                                <br/>
                                <button type="button" className="btn btn-success">Restore default photo</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="panel panel-default">
                    <div className="panel-heading">
                        Security
                    </div>
                    <div className="panel-body">
                        Login
                        <br/>
                        Password
                    </div>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        registered: state.GlobalReducer.registered,
        login: state.GlobalReducer.user.login,
        author: state.AuthorReducer.author
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthorDetails: (userId, callback) => {
            return getAuthorDetails(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setAuthor(json));
                    callback(json);
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(OptionsPage);