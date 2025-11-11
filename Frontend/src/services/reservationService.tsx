import axios from 'axios';

const RESERVATION_URL = import.meta.env.VITE_RESERVATION_URL;

const api = axios.create({
  reservationURL : RESERVATION_URL,
  withCredentials: true, // send/receive cookies automatically
  headers: {
    'Content-Type': 'application/json',
  },
});

export const reservationService = {
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
