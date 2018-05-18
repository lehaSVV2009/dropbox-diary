import axios from "axios";

import { API_KEY, API_URL } from "./env";

const client = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  validateStatus: status => status >= 200 && status < 300,
});

client.interceptors.request.use(config => {
  config.headers["x-api-key"] = API_KEY;
  return config;
});

export const createNote = note => client.post("/v1", { note });