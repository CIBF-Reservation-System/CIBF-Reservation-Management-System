
import axios from 'axios';

const BASE_URL = import.meta.env.VITE_BASE_URL;

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true, // send/receive cookies if needed
  headers: {
    'Content-Type': 'application/json',
  },
});

export const reservationService = {
  // Get all stalls
  async makeReservation() {
    try {
      const response = await api.post('/reservations');
      return response.data; 
    } catch (error: any) {
      console.error('Error fetching stalls:', error.response?.data || error.message);
      throw error;
    }
  },


};