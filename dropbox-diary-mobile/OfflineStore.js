import store from "react-native-simple-store"

export const readOfflineMessages = () => store.get("messages")

export const createOfflineMessages = messages => store.push("messages", messages)

export const deleteOfflineMessages = () => store.delete("messages")