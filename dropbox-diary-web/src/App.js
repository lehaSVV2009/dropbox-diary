import React, { Component } from "react";
import { Button, Input, MessageList } from "react-chat-elements";

import Layout from "./Layout";

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

    // TODO scroll to last element
  };

  createMessage = (text, position = "right") => ({
    position,
    type: "text",
    text,
    date: new Date()
  });

  render() {
    return (
      <Layout
        center={<MessageList dataSource={this.state.messages} />}
        bottom={
          <Input
            className="input"
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
        }
      />
    );
  }
}
