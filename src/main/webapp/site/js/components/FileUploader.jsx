import React from 'react';

/*
    props:
    - name
    - className
    - onChange - callback function
    - disabled
    - accept
    - btnName
 */
class FileUploader extends React.Component{
    constructor(props) {
        super(props);

        this.state = {
            value: '',
            styles: {
                parent: {
                    position: 'relative'
                },
                file: {
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    opacity: 0,
                    width: '100%',
                    zIndex: 1
                },
                button: {
                    position: 'relative',
                    zIndex: 0
                }
            }
        };
        ['handleChange'].map(fn => this[fn] = this[fn].bind(this));
    }

    handleChange(e) {
        this.setState({
            value: e.target.value.split(/(\\|\/)/g).pop()
        });
        if (this.props.onChange) this.props.onChange(e);
    }

    render() {
        return (
            <div style={this.state.styles.parent}>
                {/* Actual file input */}
                <input type="file"
                       name={this.props.name}
                       className={this.props.className}
                       onChange={this.handleChange}
                       disabled={this.props.disabled}
                       accept={this.props.accept}
                       style={this.state.styles.file}
                />
                {/* Emulated file input */}
                <input type="button"
                       tabIndex="-1"
                       name={this.props.name + '_filename'}
                       value={this.props.btnName}
                       className={this.props.className}
                       onChange={() => {}}
                       placeholder={this.props.placeholder}
                       disabled={this.props.disabled}
                       style={this.state.styles.button}
                />
            </div>
        )
    }
}

export default FileUploader;