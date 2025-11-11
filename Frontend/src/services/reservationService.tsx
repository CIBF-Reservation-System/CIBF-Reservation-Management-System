<<<<<<< HEAD

import axios from 'axios';

const BASE_URL = import.meta.env.VITE_BASE_URL;

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true, // send/receive cookies if needed
=======
import axios from 'axios';

const RESERVATION_URL = import.meta.env.VITE_RESERVATION_URL;

const api = axios.create({
  reservationURL : RESERVATION_URL,
  withCredentials: true, // send/receive cookies automatically
>>>>>>> origin/main
  headers: {
    'Content-Type': 'application/json',
  },
});

export const reservationService = {
<<<<<<< HEAD
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
=======
    async createReservation(
        userId : string, 
        stallId : string,
        businessName : string,
        email : string,
        phoneNumber : string) {
            const response = await api.post('/reservations', 
            { userId, stallId, businessName, email, phoneNumber });

            console.log(response);

            return { reservation: response.data.reservation };
    },

    async getReservationsByUser(userId : string) {
        const response = await api.get(`/reservations/user/${userId}`);
>>>>>>> origin/main
