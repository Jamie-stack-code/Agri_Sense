import axios from 'axios';

const API_URL = 'http://localhost:5000/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const adminService = {
  getSystemStats: () => api.get('/admin/stats'),
  getUsers: () => api.get('/admin/users'),
  getSubscriptions: () => api.get('/admin/subscriptions'),
  updateSettings: (settings) => api.put('/admin/settings', settings),
};

export default api;
