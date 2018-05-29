import React, { Component } from "react";

export default class Layout extends Component {
  render() {
    return (
      <div className="viewport">
        <div className="center">{this.props.center}</div>
        <div className="bottom">{this.props.bottom}</div>
      </div>
    );
  }
}
