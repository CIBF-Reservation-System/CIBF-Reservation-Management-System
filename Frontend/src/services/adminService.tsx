// import api from "@/lib/utilsApi";

// export const adminService = {
//   async getDashboardStats() {
//     const response = await api.get("/api/v1/admin/dashboard/stats");
//     return response.data.data;
//   },
//   async getAllStalls(area?: string, status?: string) {
//     const params: Record<string, string> = {};
//     if (area && area !== "all") params.area = area;
//     if (status && status !== "all") params.status = status;
//     const response = await api.get("/api/v1/admin/stalls", { params });
//     return response.data.data;
//   },
//   async getAllReservations(status?: string, page = 0, size = 100) {
//     const params: Record<string, string | number> = { page, size };
//     if (status && status !== "all") params.status = status;
//     const response = await api.get("/api/v1/admin/reservations", { params });
//     return response.data.data;
//   },
//   async approveStall(stallId: string) {
//     const response = await api.post(`/api/v1/admin/stalls/${stallId}/approve`);
//     return response.data.data;
//   },
//   async rejectStall(stallId: string, reason?: string) {
//     const response = await api.post(`/api/v1/admin/stalls/${stallId}/reject`, {
//       reason,
//     });
//     return response.data.data;
//   },
//   async updateStallAvailability(stallId: string, isAvailable: boolean) {
//     const response = await api.patch(
//       `/api/v1/admin/stalls/${stallId}/availability`,
//       null,
//       { params: { isAvailable } }
//     );
//     return response.data.data;
//   },
//   async updateReservationStatus(reservationId: string, status: string) {
//     const response = await api.patch(
//       `/api/v1/admin/reservations/${reservationId}/status`,
//       null,
//       { params: { status } }
//     );
//     return response.data.data;
//   },
//   async sendNotification(notificationData: Record<string, any>) {
//     const response = await api.post(
//       "/api/v1/admin/notifications/send",
//       notificationData
//     );
//     return response.data.data;
//   },
// };

import axios from "axios";

const BASE_URL = import.meta.env.VITE_BASE_URL;

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

export const adminService = {
  // Get all reservations
  async getAllReservations() {
    const response = await api.get("/reservations");
    return response.data || [];
  },

  // Get user by ID
  async getUserById(userId: string) {
    const response = await api.get(`/users/${userId}`);
    return response.data.data || {};
  },

  // Get stall by ID
  async getStallById(stallId: string) {
    const response = await api.get(`/stall/stall/${stallId}`);
    return response.data.stall || {};
  },
};
