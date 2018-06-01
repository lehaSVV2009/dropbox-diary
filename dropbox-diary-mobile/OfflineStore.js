import { AsyncStorage } from "react-native";

export const readOfflineMessages = () =>
  AsyncStorage.getItem("messages")
    .then(messages => JSON.parse(messages))
    .then(messages =>
      messages.map(message => {
        // Parse ISO8601 date from DB to js Date object
        message.createdAt = new Date(message.createdAt);
        return message;
      })
    );

export const createOfflineMessages = messages => {
  if (!messages || !Array.isArray(messages)) {
    Promise.reject(messages);
  }
  // Save date object as ISO8601 in DB
  messages = messages.map(message => {
    message.createdAt = message.createdAt.toISOString();
    return message;
  });
  return AsyncStorage.getItem("messages")
    .then(dbMessages => {
      dbMessages = JSON.parse(dbMessages);
      dbMessages = dbMessages.concat(messages);
      AsyncStorage.setItem("messages", JSON.stringify(dbMessages))
        .then(() => {
          console.log("Updated in Offline DB");
          console.log(dbMessages);
          return Promise.resolve(dbMessages);
        })
        .catch(error => Promise.reject(error));
    })
    .catch(error => {
      AsyncStorage.setItem("messages", JSON.stringify(messages))
        .then(() => {
          console.log("Created in Offline DB");
          console.log(messages);
          Promise.resolve(messages);
        })
        .catch(error => Promise.reject(error));
    });
};

export const deleteOfflineMessages = () => AsyncStorage.removeItem("messages");
