export const readOfflineMessages = () => {
  const messagesString = localStorage.getItem("messages");
  if (!messagesString) {
    return [];
  }
  try {
    const messages = JSON.parse(messagesString);
    if (!Array.isArray(messages)) {
      return [];
    }

    // Parse ISO8601 dates to date objects
    return messages.map(message => ({
      ...message,
      date: new Date(message.date)
    }));
  } catch (e) {
    return [];
  }
};

/**
 * @param {*} messages [{ title: 'blabla', date: new Date() }]
 */
export const createOfflineMessages = messages => {
  if (!messages || !Array.isArray(messages)) {
    throw new Error("No messages found to save in localstorage");
  }

  // Save date object as ISO8601 in localstorage
  messages = messages.map(message => ({
    ...message,
    date: message.date.toISOString()
  }));

  const oldMessages = readOfflineMessages();
  const updatedMessages = oldMessages.concat(messages);
  localStorage.setItem("messages", JSON.stringify(updatedMessages));

  return updatedMessages;
};

export const deleteOfflineMessages = () => localStorage.removeItem("messages");
