export const environment = {
  apiBaseUrl: process.env.REACT_APP_API_BASE_URL || 'http://localhost',
  production: process.env.NODE_ENV === 'production',
};
