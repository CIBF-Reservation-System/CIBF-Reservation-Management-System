import axios from "axios";

const api = axios.create({
  baseURL: "", // Uses relative path, so requests go through Vite proxy or same-origin
  withCredentials: true,
});

export default api;
