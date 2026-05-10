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

export const expertService = {
  getAdvisories: () => api.get('/advisories'),
  getAdvisoryHistory: (expertId) => api.get(`/advisories/history/${expertId}`),
  createAdvisory: (data) => api.post('/advisories', data),
  getQuestions: () => api.get('/chat/questions'),
  replyToQuestion: (id, data) => api.post(`/chat/reply/${id}`, data),
  getDiseaseReports: () => api.get('/diagnostics'),
  submitRecommendation: (data) => api.post('/diagnostics/recommend', data),
};

export default api;
