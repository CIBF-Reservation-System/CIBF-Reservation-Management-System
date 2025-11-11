import axios from 'axios';

const BASE_URL = import.meta.env.VITE_BASE_URL;

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true, // send/receive cookies automatically
  headers: {
    'Content-Type': 'application/json',
  },
});

export const authService = {
  async login(email: string, password: string) {
    const response = await api.post('/auth/login', { email, password });

    console.log(response);

    // Store role locally (optional)
    const role = response.data.user?.role;
    if (role) localStorage.setItem('role', role);

    return { user: response.data.user };
  },

  async logout() {
    await api.post('/auth/logout');
    localStorage.removeItem('role');
    return { success: true };
  },

  async getProfile() {
    const response = await api.get('/auth/profile');
    return response.data;
  },
};
