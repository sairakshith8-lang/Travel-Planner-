/**
 * TravelMate API Client & Utility Library
 */

const API_BASE = '/api';

const API = {
  // Generic request wrapper with session credentials
  async request(endpoint, options = {}) {
    const defaultHeaders = {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    };

    const config = {
      ...options,
      headers: {
        ...defaultHeaders,
        ...(options.headers || {})
      },
      credentials: 'same-origin' // Ensures Spring Boot HttpSession cookie is transmitted
    };

    try {
      const response = await fetch(`${API_BASE}${endpoint}`, config);
      const data = await response.json().catch(() => null);

      if (!response.ok) {
        const errorMsg = (data && data.message) ? data.message : `Request failed with status ${response.status}`;
        const error = new Error(errorMsg);
        error.status = response.status;
        error.data = data;
        throw error;
      }

      return data;
    } catch (err) {
      console.error(`API Error [${endpoint}]:`, err);
      throw err;
    }
  },

  // Auth Endpoints
  async register(name, email, password) {
    return this.request('/auth/register', {
      method: 'POST',
      body: JSON.stringify({ name, email, password })
    });
  },

  async login(email, password) {
    return this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password })
    });
  },

  async logout() {
    return this.request('/auth/logout', { method: 'POST' });
  },

  async getCurrentUser() {
    return this.request('/auth/me');
  },

  // Destinations Endpoints
  async getDestinations(query = '', travelType = '', maxCost = '') {
    const params = new URLSearchParams();
    if (query) params.append('query', query);
    if (travelType) params.append('travelType', travelType);
    if (maxCost) params.append('maxCost', maxCost);
    return this.request(`/destinations?${params.toString()}`);
  },

  async getDestinationById(id) {
    return this.request(`/destinations/${id}`);
  },

  // Trips Endpoints
  async getMyTrips(filter = 'all') {
    return this.request(`/trips?filter=${filter}`);
  },

  async getTrip(id) {
    return this.request(`/trips/${id}`);
  },

  async createTrip(tripData) {
    return this.request('/trips', {
      method: 'POST',
      body: JSON.stringify(tripData)
    });
  },

  async updateTrip(id, tripData) {
    return this.request(`/trips/${id}`, {
      method: 'PUT',
      body: JSON.stringify(tripData)
    });
  },

  async deleteTrip(id) {
    return this.request(`/trips/${id}`, { method: 'DELETE' });
  },

  // Itinerary Endpoints
  async addItineraryActivity(tripId, itemData) {
    return this.request(`/trips/${tripId}/itinerary`, {
      method: 'POST',
      body: JSON.stringify(itemData)
    });
  },

  async updateItineraryActivity(tripId, itemId, itemData) {
    return this.request(`/trips/${tripId}/itinerary/${itemId}`, {
      method: 'PUT',
      body: JSON.stringify(itemData)
    });
  },

  async deleteItineraryActivity(tripId, itemId) {
    return this.request(`/trips/${tripId}/itinerary/${itemId}`, {
      method: 'DELETE'
    });
  },

  // Expenses Endpoints
  async addExpense(tripId, expenseData) {
    return this.request(`/trips/${tripId}/expenses`, {
      method: 'POST',
      body: JSON.stringify(expenseData)
    });
  },

  async updateExpense(tripId, expenseId, expenseData) {
    return this.request(`/trips/${tripId}/expenses/${expenseId}`, {
      method: 'PUT',
      body: JSON.stringify(expenseData)
    });
  },

  async deleteExpense(tripId, expenseId) {
    return this.request(`/trips/${tripId}/expenses/${expenseId}`, {
      method: 'DELETE'
    });
  },

  // Contact Message
  async submitContact(formData) {
    return this.request('/contact', {
      method: 'POST',
      body: JSON.stringify(formData)
    });
  }
};

// Formatting Utilities
function formatINR(amount) {
  if (amount === null || amount === undefined || isNaN(amount)) return '₹0';
  return '₹' + Number(amount).toLocaleString('en-IN', { maximumFractionDigits: 0 });
}

function formatDate(dateStr) {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return date.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' });
}

// Toast Notifications
function showToast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  
  const icon = type === 'success' ? '✓' : (type === 'error' ? '✕' : 'ℹ');
  toast.innerHTML = `<span><strong>${icon}</strong> ${message}</span>`;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
    toast.style.opacity = '0';
    toast.style.transform = 'translateY(10px)';
    setTimeout(() => toast.remove(), 400);
  }, 4000);
}
