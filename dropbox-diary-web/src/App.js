import React, { Component } from "react";
import { Button, Input, MessageList } from "react-chat-elements";

import * as API from "./API";
import * as OfflineStore from "./OfflineStore";

export default class App extends Component {
  state = {
    messages: [],
    text: ""
  };

  componentDidMount() {
    this.addHelloMessage();

    const offlineMessages = OfflineStore.readOfflineMessages();
    if (offlineMessages.length === 0) {
      console.log("No offline messages found");
      return;
    }

    // Add localstorage messages to UI
    this.addMessages(offlineMessages, () => {
      this.scrollToLastMessage();
    });

    // Try to save localstorage messages by API
    API.createEvents(
      offlineMessages.map(message => ({
        text: message.title,
        createdAt: message.date
      }))
    )
      .then(response => {
        if (!API.isEventCreated(response)) {
          throw new Error(`API request failed... ${response.message}`);
        }

        // Mark message as saved by API
        this.updateMessages(
          offlineMessages.map(message => ({
            ...message,
            status: "received"
          }))
        );

        // Delete saved by API messages from localstorage
        OfflineStore.deleteOfflineMessages();
      })
      .catch(error => {
        // Failed to save offline messages by API (probably offline mode)
        console.log(
          `${
            offlineMessages.length
          } Messages are still in Offline store: ${error}`
        );
      });
  }

  handleInputChange = ({ target: { value } }) => this.setState({ text: value });

  handleInputKeyUp = event => {
    // Send message on Enter. Shift+Enter will not send message but add next line
    if (event.keyCode === 13 && !event.shiftKey) {
      this.handleSendClick();
    }
  };

  handleSendClick = () => {
    const { text } = this.state;
    if (!text || !text.trim()) {
      return;
    }

    // Create UI message model
    const message = this.buildMessage({ text, status: "waiting" });

    // Add new message to UI
    this.addMessages([message], () => {
      // Clear text input
      this.refs.input.clear();
      this.scrollToLastMessage();
    });

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
        this.updateMessages([{ ...message, status: "received" }]);
      })
      .catch(error => {
        const offlineMessages = [{ ...message, status: "sent" }];
        // Save failed messaged in localstorage
        OfflineStore.createOfflineMessages(offlineMessages);
        // Mark message as saved in localstorage on UI
        this.updateMessages(offlineMessages);
      });
  };

  scrollToLastMessage = () => {
    this.refs.center.scrollTop = this.refs.center.scrollHeight;
  };

  // Build message model
  buildMessage = ({ text, status, position }) => ({
    position: position || "right",
    status,
    type: "text",
    title: text, // title, but not text cause text padding fails with status
    titleColor: "black",
    date: new Date()
  });

  // Add 'Hello' message to UI
  addHelloMessage = () =>
    this.addMessages([
      this.buildMessage({
        text: "Hello! Any interesting story?",
        position: "left"
      })
    ]);

  // Add messages to UI
  addMessages = (messages, callback) =>
    this.setState(
      previousState => ({
        messages: previousState.messages.concat(messages)
      }),
      callback
    );

  // Update messages in UI (e.g. messages ticks)
  updateMessages = messages =>
    messages.forEach(message => {
      this.setState(previousState => ({
        messages: previousState.messages.map(
          oldMessage =>
            oldMessage.date === message.date ? message : oldMessage
        )
      }));
    });

  render() {
    return (
      <div className="viewport">
        <div className="center" ref="center">
          <MessageList dataSource={this.state.messages} />
        </div>
        <div className="bottom">
          <Input
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
