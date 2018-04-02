import React from 'react';
import { connect } from 'react-redux';
import DatePicker from 'react-bootstrap-datetimepicker';
import Select from 'react-select';
import {
    getAuthorDetails,
    setAuthor,
    saveAuthor,
    saveAvatar,
    changePassword,
    restoreDefaultAvatar
} from '../actions/AuthorActions.jsx';
import {
    createNotify,
    setUserDetails
} from '../actions/GlobalActions.jsx';
import { setToken } from '../actions/AuthActions.jsx';
import { locale, getLocale } from '../locale.jsx';

import FileUploader from '../components/FileUploader.jsx';

/*
    props:
    - author

 */
class OptionsPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            firstName: '',
            lastName: '',
            sectionName: '',
            sectionDescription: '',
            birthday: new Date().toISOString().split('T')[0],
            city: '',
            siteLanguage: {value: 'EN', label: 'English'},
            preferredLanguages: [],
            currentPassword: '',
            newPassword: '',
            confirmNewPassword: ''
        };
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        this.updateForm();
    }

    updateForm() {
        this.props.onGetAuthorDetails(this.props.login, () => this.updateState());
    }

    updateState() {
        let author = this.props.author;
        this.setState({
            firstName: author.firstName || '',
            lastName: author.lastName || '',
            sectionName: author.section.name || '',
            sectionDescription: author.section.description || '',
            birthday: author.birthday || new Date().toISOString().split('T')[0],
            city: author.city || '',
            siteLanguage: {value: author.language, label: locale[author.language || 'EN'].label},
            preferredLanguages: author.preferredLanguages ? author.preferredLanguages.split(';').map(lang => { return {value: lang, label: locale[lang].label}}) : []
        });
    }

    onDateChange(value) {
        this.setState({
            birthday: value
        });
    }

    onFieldChange(proxy) {
        switch (proxy.target.id) {
            case 'first_name': this.setState({firstName: proxy.target.value}); break;
            case 'last_name': this.setState({lastName: proxy.target.value}); break;
            case 'section_name': this.setState({sectionName: proxy.target.value}); break;
            case 'section_description': this.setState({sectionDescription: proxy.target.value}); break;
            case 'birthday': this.setState({birthday: proxy.target.value}); break;
            case 'city': this.setState({city: proxy.target.value}); break;
            case 'siteLanguage': this.setState({siteLanguage: proxy.target.value}); break;
            case 'preferredLanguages': this.setState({preferredLanguages: proxy.target.value}); break;
            case 'current_password': this.setState({currentPassword: proxy.target.value}); break;
            case 'new_password': this.setState({newPassword: proxy.target.value}); break;
            case 'confirm_new_password': this.setState({confirmNewPassword: proxy.target.value}); break;
        }
    }

    onLanguageChange(siteLanguage) {
        this.setState({
           siteLanguage: siteLanguage
        });
    }

    onMultiLanguageChange(preferredLanguages) {
        this.setState({
            preferredLanguages: preferredLanguages
        });
    }

    onAvatarChange(event) {
        if (event.target.files[0].size >= 102400 && !this.props.author.premium) {
            this.props.onCreateNotify('warning', 'Warning', 'For non-premium authors image size should not be larger than 100Kb');
            return;
        }
        let formData = new FormData();
        formData.append('avatar', event.target.files[0]);
        formData.append('userId', this.props.author.username);

        this.props.onSaveAvatar(formData, this.props.token, () => this.updateForm());
    }

    onRestoreDefaultAvatar() {
        this.props.onRestoreDefaultAvatar(this.props.token, () => this.updateForm());
    }

    getDatePickerProps() {
        return {
            placeholder: 'Enter your date of birth',
            readOnly: true
        }
    }

    onSubmit(event) {
        event.preventDefault();
        let author = {
            username: this.props.login,
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            sectionName: this.state.sectionName,
            sectionDescription: this.state.sectionDescription,
            birthday: this.state.birthday,
            city: this.state.city,
            language: this.state.siteLanguage.value,
            preferredLanguages: this.state.preferredLanguages.map(lang => lang.value).reduce((cur, next) => cur + ';' + next)
        };
        this.props.onSaveAuthor(author, this.props.token, () => this.props.onGetAuthorDetails(this.props.login, () => this.updateState()));
    }

    onChangePassword(event) {
        event.preventDefault();
        let changePasswordRequest = {
            currentPassword: this.state.currentPassword,
            newPassword: this.state.newPassword,
            confirmNewPassword: this.state.confirmNewPassword
        };
        this.props.onChangePassword(changePasswordRequest, this.props.token);
    }

    getComboboxItems() {
        let options = [];
        for (var lang in locale) {
            options.push({
                value: lang,
                label: locale[lang].label
            });
        }
        return options;
    }

    isDataLoaded() {
        if (!this.props.author) {
            return false;
        }
        return true;
    }

    render() {
        if (!this.isDataLoaded()) {
            return null;
        }
        return (
            <div>
                <div className="panel panel-default">
                    <div className="panel-heading">
                        {getLocale(this.props.language)['General']}
                    </div>
                    <div className="panel-body">
                        <form className="form-horizontal" onSubmit={event => this.onSubmit(event)}>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="first_name">{getLocale(this.props.language)['First name:']}</label>
                                <div className="col-sm-10">
                                    <input value={this.state.firstName} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="first_name" placeholder={getLocale(this.props.language)['Enter your first name']} name="first_name"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="last_name">{getLocale(this.props.language)['Last name:']}</label>
                                <div className="col-sm-10">
                                    <input value={this.state.lastName} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="last_name" placeholder={getLocale(this.props.language)['Enter your last name']} name="last_name"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="section_name">{getLocale(this.props.language)['Section name:']}</label>
                                <div className="col-sm-10">
                                    <input value={this.state.sectionName} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="section_name" placeholder={getLocale(this.props.language)['Enter the name of your section']} name="section_name"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="section_description">{getLocale(this.props.language)['Section description:']}</label>
                                <div className="col-sm-10">
                                    <textarea value={this.state.sectionDescription} onChange={proxy => this.onFieldChange(proxy)} rows="5" className="form-control" id="section_description" placeholder={getLocale(this.props.language)['Enter the description of your section']} name="section_description"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="birthday">{getLocale(this.props.language)['Birthday:']}</label>
                                <div className="col-sm-10">
                                    <DatePicker dateTime={this.state.birthday} onChange={value => this.onDateChange(value)} inputProps={this.getDatePickerProps()} id="birthday" defaultText="" format="YYYY-MM-DD" inputFormat="DD-MM-YYYY" mode="date"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="city">{getLocale(this.props.language)['City:']}</label>
                                <div className="col-sm-10">
                                    <input value={this.state.city} onChange={proxy => this.onFieldChange(proxy)} type="text" className="form-control" id="city" placeholder={getLocale(this.props.language)['Enter your city']} name="city"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="language">{getLocale(this.props.language)['Interface language:']}</label>
                                <div className="col-sm-10">
                                    <Select value={this.state.siteLanguage} id="siteLanguage" options={this.getComboboxItems()} onChange={lang => this.onLanguageChange(lang)} placeholder={getLocale(this.props.language)['Choose website language']}/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="language">{getLocale(this.props.language)['Preferred languages:']}</label>
                                <div className="col-sm-10">
                                    <Select value={this.state.preferredLanguages} id="preferredLanguages" options={this.getComboboxItems()} multi={true} onChange={lang => this.onMultiLanguageChange(lang)} placeholder={getLocale(this.props.language)['Choose your preferred languages']}/>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-12 text-center">
                                    <button type="submit" className="btn btn-success">{getLocale(this.props.language)['Save']}</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <div className="panel panel-default">
                    <div className="panel-heading">
                        {getLocale(this.props.language)['Avatar']}
                    </div>
                    <div className="panel-body">
                        <div className="col-sm-4">
                            <img src={this.props.author.avatar + '?date=' + new Date()} className="img-rounded" width="200" height="auto"/>
                        </div>
                        <div className="col-sm-8">
                            <div className="btn-group-vertical">
                                <FileUploader
                                    btnName={getLocale(this.props.language)['Choose your avatar']}
                                    name="avatar"
                                    accept=".png,.jpg"
                                    className="btn btn-success"
                                    onChange={event => this.onAvatarChange(event)}
                                />
                                <br/>
                                <button onClick={() => this.onRestoreDefaultAvatar()} type="button" className="btn btn-success">{getLocale(this.props.language)['Restore default photo']}</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="panel panel-default">
                    <div className="panel-heading">
                        {getLocale(this.props.language)['Security']}
                    </div>
                    <div className="panel-body">
                        <form className="form-horizontal" onSubmit={event => this.onChangePassword(event)}>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="current_password">{getLocale(this.props.language)['Current password:']}</label>
                                <div className="col-sm-10">
                                    <input value={this.state.currentPassword} onChange={proxy => this.onFieldChange(proxy)} type="password" className="form-control" id="current_password" placeholder={getLocale(this.props.language)['Enter your current password']} name="current_password"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="new_password">{getLocale(this.props.language)['New password:']}</label>
                                <div className="col-sm-10">
                                    <input value={this.state.newPassword} onChange={proxy => this.onFieldChange(proxy)} type="password" className="form-control" id="new_password" placeholder={getLocale(this.props.language)['Enter your new password']} name="new_password"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="control-label col-sm-2" htmlFor="confirm_new_password">{getLocale(this.props.language)['Confirm new password:']}</label>
                                <div className="col-sm-10">
                                    <input value={this.state.confirmNewPassword} onChange={proxy => this.onFieldChange(proxy)} type="password" className="form-control" id="confirm_new_password" placeholder={getLocale(this.props.language)['Confirm your new password']} name="confirm_new_password"/>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-12 text-center">
                                    <button type="submit" className="btn btn-success">{getLocale(this.props.language)['Change password']}</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        registered: state.GlobalReducer.registered,
        token: state.GlobalReducer.token,
        login: state.GlobalReducer.user.login,
        author: state.AuthorReducer.author,
        language: state.GlobalReducer.language
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onGetAuthorDetails: (userId, callback) => {
            getAuthorDetails(userId).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(setAuthor(json));
                    dispatch(setUserDetails(json));
                    callback();
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSaveAuthor: (author, token, callback) => {
            saveAuthor(author, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Data was saved successfully'));
                    dispatch(setToken(json.token));
                    callback();
                }
                else if (json.message.includes('JWT expired at')) {
                    dispatch(setToken(''));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onSaveAvatar: (avatar, token, callback) => {
            saveAvatar(avatar, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Avatar was saved successfully'));
                    callback();
                    dispatch(setToken(json.token));
                }
                else if (json.message.includes('JWT expired at')) {
                    dispatch(setToken(''));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onChangePassword: (changePasswordRequest, token) => {
            if (!changePasswordRequest.currentPassword || changePasswordRequest.currentPassword.length === 0) {
                dispatch(createNotify('warning', 'Warning', 'Please input a correct password'));
                return;
            }
            if (changePasswordRequest.newPassword !== changePasswordRequest.confirmNewPassword) {
                dispatch(createNotify('warning', 'Warning', 'Your new password doesn\'t equlas to your confirmation password'));
                return;
            }

            changePassword(changePasswordRequest, token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Your password was changed successfully'));
                    dispatch(setToken(json.token));
                }
                else if (json.message.includes('JWT expired at')) {
                    dispatch(setToken(''));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onRestoreDefaultAvatar: (token, callback) => {
            restoreDefaultAvatar(token).then(([response, json]) => {
                if (response.status === 200) {
                    dispatch(createNotify('success', 'Success', 'Avatar was restored successfully'));
                    callback();
                    dispatch(setToken(json.token));
                }
                else if (json.message.includes('JWT expired at')) {
                    dispatch(setToken(''));
                }
                else {
                    dispatch(createNotify('danger', 'Error', json.message));
                }
            }).catch(error => {
                dispatch(createNotify('danger', 'Error', error.message));
            });
        },

        onCreateNotify: (type, header, message) => {
            dispatch(createNotify(type, header, message));
        }
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(OptionsPage);