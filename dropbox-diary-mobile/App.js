import React from "react";
import {
  Button,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View
} from "react-native";

export default class App extends React.Component {
  state = {
    text: ""
  }

  handleChangeText = text => this.setState({ text });
  handleCancel = () => {
    // TODO close the app
  }
  handleOk = () => {
    // TODO save to dropbox
  }

  render() {
    return (
      <ScrollView>
        <View style={styles.container}>
          <Text>What did you do yesterday?</Text>
          <View style={styles.textArea}>
            <TextInput
              multiline
              placeholder="No time..."
              value={this.state.text}
              onChangeText={this.handleChangeText}
            />
          </View>
          <View style={styles.buttonsBar}>
            <Button
              color="grey"
              title="Cancel"
              onPress={this.handleCancel}
            />
            <Button
              color="blue"
              title="Ok"
              onPress={this.handleOk}
            />
          </View>
        </View>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    marginTop: 50,
    marginBottom: 275
  },
  textArea: {
    borderBottomColor: "#000000",
    borderBottomWidth: 1,
    width: "90%",
    padding: 5
  },
  buttonsBar: {
    flexDirection: "row"
  }
});
