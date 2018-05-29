import React, { Component } from "react";
import { Button, Input, MessageList } from "react-chat-elements";

import "react-chat-elements/dist/main.css";

export default class App extends Component {
  constructor() {
    super();
    this.state = {
      messages: [this.createMessage("Hello! Any interesting story?", "left")],
      text: ""
    };
  }

  handleInputChange = ({ target: { value } }) => this.setState({ text: value });

  handleSendClick = () => {
    const { text, messages } = this.state;
    if (!text) {
      return;
    }

    // Add new message to UI
    messages.push(this.createMessage(text));
    this.setState({
      text: "",
      messages
    });

    // Clear text input
    this.refs.input.clear();
  };

  createMessage = (text, position = "right") => ({
    position,
    type: "text",
    text,
    date: new Date()
  });

  render() {
    return (
      <div>
        <MessageList dataSource={this.state.messages} />
        <Input
          ref="input"
          multiline
          placeholder="Type a message..."
          onChange={this.handleInputChange}
          rightButtons={
            <Button
              color="white"
              backgroundColor="black"
              text="Send"
              onClick={this.handleSendClick}
            />
          }
        />
      </div>
    );
  }
}
