import axios from 'axios';

const BASE_URL = import.meta.env.VITE_BASE_URL;

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true, // send/receive cookies if needed
  headers: {
    'Content-Type': 'application/json',
  },
});

export const stallService = {
  // Get all stalls
  async getAllStalls() {
    try {
      const response = await api.get('/stall/getstalls');
      return response.data; // This should be the array of stalls
    } catch (error: any) {
      console.error('Error fetching stalls:', error.response?.data || error.message);
      throw error;
    }
  },

 
};
