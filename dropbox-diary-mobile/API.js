import axios from "axios"

import { API_KEY, API_URL } from "./env"

const client = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  validateStatus: status => status >= 200 && status < 300,
})

client.interceptors.request.use(config => {
  config.headers["x-api-key"] = API_KEY
  return config
})

export const isEventCreated = response => response && response.data && response.data.statusCode === 201

export const createEvents = messages => client.post(
  "/v1",
  messages.map(message => ({
    text: message.text,
    date: message.createdAt.toISOString(),
  }))
)