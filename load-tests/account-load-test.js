import http from 'k6/http';

export const options = {
    vus: 100,
    iterations: 100,
};

export default function () {
    http.get('http://localhost:8081/api/v1/accounts/1');
}