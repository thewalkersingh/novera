import axios from 'axios';

export const api = axios.create({
    baseURL: '/api/v1', // Proxied to localhost:8080 via vite.config.ts
    headers: {
        'Content-Type': 'application/json',
    },
});