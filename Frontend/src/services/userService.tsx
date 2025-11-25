import axios from "axios";

const BASE_URL = import.meta.env.VITE_BASE_URL;

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true, // send/receive cookies automatically
  headers: {
    "Content-Type": "application/json",
  },
});

export const userService = {
  // Get all users
  async getAllUsers() {
    try {
      const response = await api.get("/user");
      return response.data; // returns an array of users
    } catch (error) {
      console.error("Failed to fetch users", error);
      throw error;
    }
  },

  // Get a user by ID
  async getUserById(userId: string) {
    try {
      const response = await api.get(`/user/${userId}`);
      return response.data.data; // returns the user object
    } catch (error) {
      console.error(`Failed to fetch user ${userId}`, error);
      throw error;
    }
  },
};
