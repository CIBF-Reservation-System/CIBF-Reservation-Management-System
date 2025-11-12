
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
  async makeReservation(reservations: any[]) {
    try {
      const response = await api.post('/reservations', reservations);
      return response.data;
    } catch (error: any) {
      console.error('Error making reservation:', error.response?.data || error.message);
      throw error;
    }
  },

   // Fetch reservations by userId
  async getUserReservations(userId: string) {
    try {
      const response = await api.get(`/reservations/user/${userId}`);
      return response.data; // returns an array of reservations
    } catch (error: any) {
      console.error('Error fetching reservations:', error.response?.data || error.message);
      throw error;
    }
  },


};


