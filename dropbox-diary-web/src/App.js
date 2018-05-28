import React, { Component } from "react";
import { Button, Input, MessageList } from "react-chat-elements";

import "react-chat-elements/dist/main.css";

export default class App extends Component {
  render() {
    return (
      <div>
        <MessageList
          lockable={true}
          toBottomHeight={"100%"}
          dataSource={[
            {
              position: "right",
              type: "text",
              text: "Lorem ipsum dolor sit amet, consectetur adipisicing elit",
              date: new Date()
            },
            {
              position: "right",
              type: "text",
              text: "Blablabla",
              date: new Date()
            }
          ]}
        />
        <Input
          multiline
          placeholder="Type here..."
          rightButtons={
            <Button color="white" backgroundColor="black" text="Send" />
          }
        />
      </div>
    );
  }
}
