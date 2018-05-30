import React, { Component } from "react";
import { Button, Input, MessageList } from "react-chat-elements";

import * as API from "./API";

export default class App extends Component {
  constructor() {
    super();
    this.state = {
      messages: [
        this.buildMessage({
          text: "Hello! Any interesting story?",
          position: "left"
        })
      ],
      text: ""
    };
  }

  // Message id counter
  idCounter = 0;

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

    // Create UI message model
    const message = this.buildMessage({ text, status: "waiting" });

    // Add new message to UI
    messages.push(message);
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

    // Create event by API
    API.createEvents([
      {
        text: message.title,
        createdAt: message.date
      }
    ])
      .then(response => {
        if (!API.isEventCreated(response)) {
          throw new Error(`API request failed... ${response.message}`);
        }

        // Mark message as saved by API
        this.updateMessage({ ...message, status: "received" });
      })
      .catch(error => {
        // Mark message as saved in localstorage
        this.updateMessage({ ...message, status: "sent" });
      });
  };

  buildMessage = ({ text, status, position }) => ({
    id: this.idCounter++,
    position: position || "right",
    status,
    type: "text",
    title: text, // title, but not text cause text padding fails with status
    titleColor: "black",
    date: new Date()
  });

  updateMessage = message => {
    this.setState(previousState => ({
      messages: previousState.messages.map(
        oldMessage => (oldMessage.id === message.id ? message : oldMessage)
      )
    }));
  };

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
