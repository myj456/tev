import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8080', // Spring Boot 기본 포트
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터: 모든 요청에 Access Token을 추가합니다.
instance.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
});

export default instance;
