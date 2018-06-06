import axios from "axios";

const client = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    "Content-Type": "application/json"
  },
  validateStatus: status => status >= 200 && status < 300
});

client.interceptors.request.use(config => {
  config.headers["x-api-key"] = process.env.REACT_APP_API_KEY;
  return config;
});

export const isEventCreated = response =>
  response && response.data && response.data.statusCode === 201;

export const createEvents = messages =>
  client
    .post(
      "/v1",
      messages.map(message => ({
        text: message.text,
        date: message.createdAt.toISOString()
      }))
    )
    .then(response => {
      if (!isEventCreated(response)) {
        throw new Error(`API request failed... ${response.message}`);
      }

      return response;
    });
