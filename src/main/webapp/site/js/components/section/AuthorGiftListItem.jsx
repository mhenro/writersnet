import React from 'react';
import { Tooltip, OverlayTrigger } from 'react-bootstrap';
/*
 props:
 - gift
 */
class AuthorGiftListItem extends React.Component {
    getName() {
        return this.props.gift.gift.name;
    }

    getDescription() {
        return (
            <Tooltip id="tooltip">
                <table className="table">
                    <tbody>
                        <tr>
                            <td>description:</td>
                            <td>{this.props.gift.gift.description}</td>
                        </tr>
                        <tr>
                            <td>from:</td>
                            <td>{this.props.gift.senderFullName}</td>
                        </tr>
                        <tr>
                            <td>message:</td>
                            <td>{this.props.gift.sendMessage}</td>
                        </tr>
                    </tbody>
                </table>
            </Tooltip>
        )
    }

    getCost() {
        return parseFloat(this.props.gift.gift.cost / 100).toFixed(2) + ' cr.';
    }

    getImage() {
        return this.props.gift.gift.image;
    }

    render() {
        return (
            <div className="col-sm-3">
                <div className="panel panel-default clickable">
                    <div className="panel-body">
                        <div className="col-sm-12 text-center gift-price">
                            {this.getCost()}
                        </div>
                        <div className="col-sm-12">
                            <OverlayTrigger placement="top" overlay={this.getDescription()}>
                                <img src={this.getImage()} width="64px" height="64px"/>
                            </OverlayTrigger>
                        </div>
                    </div>
                    <div className="panel-footer text-center">
                        {this.getName()}
                    </div>
                </div>
            </div>
        )
    }
}

export default AuthorGiftListItem;