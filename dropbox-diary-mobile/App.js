import React from "react";
import { GiftedChat } from "react-native-gifted-chat";

import * as API from "./API";

export default class App extends React.Component {
  state = {
    messages: [],
  }

  componentWillMount() {
    this.setState({
      messages: [
        {
          _id: 1,
          text: "Hello! Any interesting story?",
          createdAt: new Date(),
          user: {
            _id: 2,
            name: "React Native",
            avatar: "https://placeimg.com/140/140/any",
          },
        },
      ],
    })
  }

  onSend(messages = []) {
    if (messages.length === 0) {
      return;
    }
    messages.forEach(message => {
      API
        .createNote(message.text)
        .then(response => {
          this.setState(previousState => ({
            messages: GiftedChat.append(previousState.messages, message),
          }));
        })
    })
  }

  render() {
    return (
      <GiftedChat
        messages={this.state.messages}
        onSend={messages => this.onSend(messages)}
        user={{
          _id: 1,
        }}
      />
    )
  }
}