import React, { Component } from "react";
import { Button, Input, MessageList } from "react-chat-elements";

export default class App extends Component {
  constructor() {
    super();
    this.state = {
      messages: [this.createMessage("Hello! Any interesting story?", "left")],
      text: ""
    };
  }

  handleInputChange = ({ target: { value } }) => this.setState({ text: value });

  handleInputKeyUp = event => {
    // Send message on Enter. Shift+Enter will not send message but add next line
    if (event.keyCode === 13 && !event.shiftKey) {
      this.handleSendClick();
    }
  };

  handleSendClick = () => {
    const { text, messages } = this.state;
    if (!text || !text.trim()) {
      return;
    }

    // Add new message to UI
    messages.push(this.createMessage(text));
    this.setState(
      {
        text: "",
        messages
      },
      () => {
        // Clear text input
        this.refs.input.clear();

        // Scroll to last message
        this.refs.center.scrollTop = this.refs.center.scrollHeight;
      }
    );
  };

  createMessage = (text, position = "right") => ({
    position,
    type: "text",
    text,
    date: new Date()
  });

  render() {
    return (
      <div className="viewport">
        <div className="center" ref="center">
          <MessageList dataSource={this.state.messages} />
        </div>
        <div className="bottom">
          <Input
            className="input"
            ref="input"
            multiline
            placeholder="Type a message..."
            onChange={this.handleInputChange}
            onKeyUp={this.handleInputKeyUp}
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
      </div>
    );
  }
}
