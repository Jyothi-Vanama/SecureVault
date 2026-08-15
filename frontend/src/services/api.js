import axios from "axios";

// Central Axios instance. Point this at the Spring Boot backend base URL.
// Every other service file should import `api` from here instead of
// creating its own axios instance, so auth headers and error handling
// stay in one place.
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// Attach the JWT (once auth is wired to the real backend) to every request.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("securevault_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Centralized response handling. Expand this once the backend's real
// error-response shape is known.
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired / invalid — clear local session.
      localStorage.removeItem("securevault_token");
      localStorage.removeItem("securevault_user");
    }
    return Promise.reject(error);
  }
);

export default api;
