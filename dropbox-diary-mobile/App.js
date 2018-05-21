import React from "react"
import { GiftedChat } from "react-native-gifted-chat"

import * as API from "./API"
import * as OfflineStore from "./OfflineStore"

export default class App extends React.Component {
  state = {
    messages: [],
  }

  componentDidMount() {
    this.addHelloMessage()
    OfflineStore
      .readOfflineMessages()
      .then(messages => {
        API
          .createEvents(messages)
          .then(response => {
            if (!API.isEventCreated(response)) {
              throw new Error(`API request failed... ${response.message}`)
            }
            // Show saved messages on UI
            const savedMessages = messages.map(message => ({ sent: true, received: true, ...message }))
            this.addMessages(savedMessages)
    
            // Delete all already saved by API message from offline store
            OfflineStore.deleteOfflineMessages()
          })
          .catch(error => {
            console.log(`Messages ${messages} are still in Offline store`)
            this.addMessages(messages)
          })
      })
      .catch(error => {
        console.log("No offline messages found")
      })
  }

  handleSend = (messages = []) => {
    if (messages.length === 0) {
      return
    }
    // Add messages to UI as non-saved
    this.addMessages(messages)

    // Try to create new diary events by API
    API
      .createEvents(messages)
      .then(response => {
        if (!API.isEventCreated(response)) {
          throw new Error(`API request failed... ${response.message}`)
        }
        // Add 2 ticks to messages as a mark of saved by API message.
        const savedMessages = messages.map(message => ({ sent: true, received: true, ...message }))
        this.updateMessages(savedMessages)
      })
      .catch(error => {
        // Save failed messages in offline database
        const offlineMessages = messages.map(message => ({ sent: true, received: false, ...message }))
        OfflineStore
          .createOfflineMessages(offlineMessages)
          .then(() => {
            console.log("Created offline messages " + offlineMessages)
            this.updateMessages(offlineMessages)
          })
      })
  }

  // Add 'Hello' message to UI
  addHelloMessage = () =>
    this.addMessages([{
      _id: 1,
      text: "Hello! Any interesting story?",
      createdAt: new Date(),
      user: {
        _id: 2,
        name: "React Native",
        avatar: "https://placeimg.com/140/140/any",
      },
    }])

  // Add messages to UI
  addMessages = messages =>
    this.setState(
      previousState => ({
        messages: GiftedChat.append(previousState.messages, messages)
      }))

  // Update messages in UI (e.g. messages ticks)
  updateMessages = messages =>
    messages.forEach(message => {
      this.setState(previousState => ({
        messages: previousState.messages.map(
          oldMessage => oldMessage._id === message._id ? message : oldMessage)
      }))  
    })

  render() {
    return (
      <GiftedChat
        messages={this.state.messages}
        onSend={this.handleSend}
        user={{
          _id: 1,
        }}
      />
    )
  }
}