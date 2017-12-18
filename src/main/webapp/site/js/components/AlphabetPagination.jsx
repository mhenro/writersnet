import React from 'react';

/*
    props:
    - language
    - onClick - callback
 */
class AlphabetPagination extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeButton: null
        };
    }

    onClick(letter) {
        this.setState({
            activeButton: letter
        });
        this.props.onClick(letter);
    }

    getClass(btnName) {
        if (btnName === this.state.activeButton) {
            return 'btn btn-default alphabet-rectangle active';
        }
        return 'btn btn-default alphabet-rectangle';
    }

    render() {
        return (
            <div>
                <div>
                    <button onClick={() => this.onClick('A')} className={this.getClass('A')}>A</button>
                    <button onClick={() => this.onClick('B')} className={this.getClass('B')}>B</button>
                    <button onClick={() => this.onClick('C')} className={this.getClass('C')}>C</button>
                    <button onClick={() => this.onClick('D')} className={this.getClass('D')}>D</button>
                    <button onClick={() => this.onClick('E')} className={this.getClass('E')}>E</button>
                    <button onClick={() => this.onClick('F')} className={this.getClass('F')}>F</button>
                    <button onClick={() => this.onClick('G')} className={this.getClass('G')}>G</button>
                    <button onClick={() => this.onClick('H')} className={this.getClass('H')}>H</button>
                    <button onClick={() => this.onClick('I')} className={this.getClass('I')}>I</button>
                    <button onClick={() => this.onClick('J')} className={this.getClass('J')}>J</button>
                    <button onClick={() => this.onClick('K')} className={this.getClass('K')}>K</button>
                    <button onClick={() => this.onClick('L')} className={this.getClass('L')}>L</button>
                    <button onClick={() => this.onClick('M')} className={this.getClass('M')}>M</button>
                    <button onClick={() => this.onClick('N')} className={this.getClass('N')}>N</button>
                    <button onClick={() => this.onClick('O')} className={this.getClass('O')}>O</button>
                    <button onClick={() => this.onClick('P')} className={this.getClass('P')}>P</button>
                    <button onClick={() => this.onClick('Q')} className={this.getClass('Q')}>Q</button>
                    <button onClick={() => this.onClick('R')} className={this.getClass('R')}>R</button>
                    <button onClick={() => this.onClick('S')} className={this.getClass('S')}>S</button>
                    <button onClick={() => this.onClick('T')} className={this.getClass('T')}>T</button>
                    <button onClick={() => this.onClick('V')} className={this.getClass('V')}>V</button>
                    <button onClick={() => this.onClick('U')} className={this.getClass('U')}>U</button>
                    <button onClick={() => this.onClick('W')} className={this.getClass('W')}>W</button>
                    <button onClick={() => this.onClick('X')} className={this.getClass('X')}>X</button>
                    <button onClick={() => this.onClick('Y')} className={this.getClass('Y')}>Y</button>
                    <button onClick={() => this.onClick('Z')} className={this.getClass('Z')}>Z</button>
                </div>
            </div>
        )
    }
}

export default AlphabetPagination;