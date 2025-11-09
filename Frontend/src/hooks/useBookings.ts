import { useState, useEffect } from 'react';

export interface Booking {
  id: string;
  ref: string;
  stalls: string[];
  date: string;
  status: "Confirmed" | "Cancelled" | "Pending";
  businessName: string;
  email: string;
  phone: string;
  totalPrice: number;
  bookingDate: string;
  paymentStatus: "Paid" | "Pending" | "Failed";
}

const STORAGE_KEY = 'colombo-bookfair-bookings';

export const useBookings = () => {
  const [bookings, setBookings] = useState<Booking[]>([]);

  // Load bookings from localStorage on mount
  useEffect(() => {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      try {
        setBookings(JSON.parse(stored));
      } catch (error) {
        console.error('Failed to parse bookings:', error);
      }
    }
  }, []);

  // Save bookings to localStorage whenever they change
  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(bookings));
  }, [bookings]);

  const addBooking = (booking: Booking) => {
    setBookings((prev) => [booking, ...prev]);
  };

  const updateBooking = (id: string, updates: Partial<Booking>) => {
    setBookings((prev) =>
      prev.map((b) => (b.id === id ? { ...b, ...updates } : b))
    );
  };

  const cancelBooking = (id: string) => {
    updateBooking(id, { status: 'Cancelled' });
  };

  const getBookingByRef = (ref: string) => {
    return bookings.find((b) => b.ref === ref);
  };

  return {
    bookings,
    addBooking,
    updateBooking,
    cancelBooking,
    getBookingByRef,
  };
};