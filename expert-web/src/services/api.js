import axios from 'axios';

const API_URL = 'http://localhost:5000/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const authService = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
};

export const advisoryService = {
  getAll: () => api.get('/advisories'),
  create: (data) => api.post('/advisories', data),
};

export const questionService = {
  getAll: () => api.get('/chat/questions'),
  reply: (questionId, data) => api.post(`/chat/reply/${questionId}`, data),
};

export const userService = {
  getAll: () => api.get('/users'),
  getStats: () => api.get('/users/stats'),
};

export default api;
