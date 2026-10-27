/**
 * TravelMate API Client & Utility Library
 * Supports both Live Spring Boot Backend and Vercel Static Demo Fallback
 */

const BACKEND_URL = window.API_BASE_URL || localStorage.getItem('TRAVELMATE_API_URL') || '';
const API_BASE = BACKEND_URL ? `${BACKEND_URL.replace(/\/$/, '')}/api` : '/api';

// Initial Mock Database for static deployments (e.g. Vercel)
const STATIC_DESTINATIONS = [
  {
    id: 1,
    name: "Goa",
    state: "Goa",
    description: "Famous for pristine sandy beaches, vibrant coastal nightlife, Portuguese colonial architecture, and fresh seafood shacks.",
    bestTimeToVisit: "November to February",
    estimatedDailyCost: 3500.0,
    travelType: "Beach",
    imageUrl: "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=800&q=80",
    rating: 4.9,
    popularAttractions: "Baga Beach, Calangute, Aguada Fort, Basilica of Bom Jesus, Dudhsagar Falls"
  },
  {
    id: 2,
    name: "Manali",
    state: "Himachal Pradesh",
    description: "A high-altitude Himalayan resort town known for snow-capped peaks, pine forests, adventure sports, and scenic mountain valleys.",
    bestTimeToVisit: "October to June",
    estimatedDailyCost: 3000.0,
    travelType: "Hill Station",
    imageUrl: "https://images.unsplash.com/photo-1626621341517-bbf3d9990a23?auto=format&fit=crop&w=800&q=80",
    rating: 4.8,
    popularAttractions: "Solang Valley, Rohtang Pass, Hadimba Temple, Old Manali, Jogini Waterfall"
  },
  {
    id: 3,
    name: "Jaipur",
    state: "Rajasthan",
    description: "The majestic Pink City renowned for magnificent royal forts, opulent palaces, vibrant bazaars, and rich Rajasthani heritage.",
    bestTimeToVisit: "October to March",
    estimatedDailyCost: 2800.0,
    travelType: "Heritage",
    imageUrl: "https://images.unsplash.com/photo-1603262110263-fb010d6e59d4?auto=format&fit=crop&w=800&q=80",
    rating: 4.7,
    popularAttractions: "Hawa Mahal, Amber Palace, City Palace, Jantar Mantar, Nahargarh Fort"
  },
  {
    id: 4,
    name: "Kerala (Alleppey)",
    state: "Kerala",
    description: "God's Own Country famous for tranquil backwaters, traditional houseboats, Ayurvedic wellness retreats, and lush palm groves.",
    bestTimeToVisit: "September to March",
    estimatedDailyCost: 3800.0,
    travelType: "Nature",
    imageUrl: "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?auto=format&fit=crop&w=800&q=80",
    rating: 4.9,
    popularAttractions: "Alleppey Backwaters, Marari Beach, Vembanad Lake, Krishnapuram Palace"
  },
  {
    id: 5,
    name: "Hyderabad",
    state: "Telangana",
    description: "The historic City of Pearls blending legendary Nizami architecture, bustling markets, world-famous biryani, and high-tech elegance.",
    bestTimeToVisit: "October to February",
    estimatedDailyCost: 2500.0,
    travelType: "Heritage",
    imageUrl: "https://images.unsplash.com/photo-1572455044327-7348c1be7267?auto=format&fit=crop&w=800&q=80",
    rating: 4.6,
    popularAttractions: "Charminar, Golconda Fort, Ramoji Film City, Hussain Sagar, Chowmahalla Palace"
  },
  {
    id: 6,
    name: "Ooty",
    state: "Tamil Nadu",
    description: "The Queen of Hill Stations in the Nilgiri hills, featuring sprawling tea gardens, cool mist, pine forests, and heritage toy train rides.",
    bestTimeToVisit: "March to June & September to November",
    estimatedDailyCost: 2600.0,
    travelType: "Hill Station",
    imageUrl: "https://images.unsplash.com/photo-1589182373726-e4f658ab50f0?auto=format&fit=crop&w=800&q=80",
    rating: 4.6,
    popularAttractions: "Ooty Lake, Botanical Gardens, Doddabetta Peak, Pykara Falls, Nilgiri Mountain Railway"
  },
  {
    id: 7,
    name: "Varanasi",
    state: "Uttar Pradesh",
    description: "One of the world's oldest continually inhabited cities, sacred Ghats along the holy River Ganga, mesmerizing Aarti, and timeless spirituality.",
    bestTimeToVisit: "October to March",
    estimatedDailyCost: 2200.0,
    travelType: "Spiritual",
    imageUrl: "https://images.unsplash.com/photo-1561361513-2d000a50f0dc?auto=format&fit=crop&w=800&q=80",
    rating: 4.8,
    popularAttractions: "Dashashwamedh Ghat, Kashi Vishwanath Temple, Assi Ghat, Sarnath, Manikarnika Ghat"
  },
  {
    id: 8,
    name: "Rishikesh",
    state: "Uttarakhand",
    description: "The Yoga Capital of the World along the foothills of the Himalayas, famed for white water river rafting, cliff jumping, and serene ashrams.",
    bestTimeToVisit: "September to April",
    estimatedDailyCost: 2400.0,
    travelType: "Adventure",
    imageUrl: "https://images.unsplash.com/photo-1544735716-392fe2489ffa?auto=format&fit=crop&w=800&q=80",
    rating: 4.7,
    popularAttractions: "Laxman Jhula, Triveni Ghat, Shivpuri Rafting, Beatles Ashram, Neer Garh Waterfall"
  },
  {
    id: 9,
    name: "Agra",
    state: "Uttar Pradesh",
    description: "Home to the timeless wonder of the world—the Taj Mahal—along with stunning Mughal architecture and exquisite handicrafts.",
    bestTimeToVisit: "October to March",
    estimatedDailyCost: 2700.0,
    travelType: "Heritage",
    imageUrl: "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=800&q=80",
    rating: 4.9,
    popularAttractions: "Taj Mahal, Agra Fort, Mehtab Bagh, Fatehpur Sikri, Itimad-ud-Daulah"
  },
  {
    id: 10,
    name: "Udaipur",
    state: "Rajasthan",
    description: "The romantic City of Lakes crowned with sparkling marble palaces, sunset boat cruises, and exquisite royal courtyards.",
    bestTimeToVisit: "September to March",
    estimatedDailyCost: 3200.0,
    travelType: "Heritage",
    imageUrl: "https://images.unsplash.com/photo-1615836245337-f5b9b2303f10?auto=format&fit=crop&w=800&q=80",
    rating: 4.8,
    popularAttractions: "City Palace, Lake Pichola, Jag Mandir, Saheliyon-ki-Bari, Fateh Sagar Lake"
  }
];

// Local fallback store initialization
function initLocalStore() {
  if (!localStorage.getItem('TM_INITIALIZED')) {
    const defaultUser = { id: 1, name: "Aarav Sharma", email: "demo@travelmate.com" };
    localStorage.setItem('TM_USER', JSON.stringify(defaultUser));
    
    const sampleTrip = {
      id: 1,
      tripName: "Goa Coastal Getaway 2026",
      source: "Mumbai",
      destination: "Goa",
      departureDate: new Date(Date.now() + 10 * 86400000).toISOString().split('T')[0],
      returnDate: new Date(Date.now() + 14 * 86400000).toISOString().split('T')[0],
      travelers: 2,
      budget: 45000.0,
      travelPreference: "Relaxation",
      status: "Upcoming",
      durationDays: 5,
      totalEstimatedCost: 45000.0,
      totalActualSpent: 5200.0,
      remainingBudget: 39800.0,
      itineraryItems: [
        { id: 101, dayNumber: 1, timeOfDay: "Morning", activityName: "Arrival & Check-in at North Goa Resort", description: "Settle in, unpack, and enjoy refreshing coconut water by the beach.", estimatedCost: 400.0 },
        { id: 102, dayNumber: 1, timeOfDay: "Afternoon", activityName: "Calangute & Baga Beach Stroll", description: "Explore vibrant beach shacks, relax on sunbeds, and try local Goan fish curry.", estimatedCost: 800.0 },
        { id: 103, dayNumber: 1, timeOfDay: "Evening", activityName: "Sunset at Tito's Lane & Beachside Cafe", description: "Experience Goa's famous evening vibes with live acoustic music and sea breeze.", estimatedCost: 1200.0 },
        { id: 104, dayNumber: 2, timeOfDay: "Morning", activityName: "Fort Aguada & Sinquerim Lighthouse", description: "Explore the 17th-century Portuguese fortress with panoramic Arabian Sea views.", estimatedCost: 300.0 },
        { id: 105, dayNumber: 2, timeOfDay: "Afternoon", activityName: "Water Sports at Anjuna Beach", description: "Enjoy thrilling parasailing, jet skiing, or banana boat rides.", estimatedCost: 1800.0 },
        { id: 106, dayNumber: 2, timeOfDay: "Evening", activityName: "Anjuna Flea Market & Curties Cafe", description: "Browse handmade souvenirs, bohemian clothing, and watch the sun dip into the sea.", estimatedCost: 700.0 }
      ],
      expenses: [
        { id: 201, category: "Transportation", description: "Flights/Train & Local taxi cabs", estimatedAmount: 11250.0, actualAmount: 3200.0 },
        { id: 202, category: "Accommodation", description: "Resort booking advance", estimatedAmount: 15750.0, actualAmount: 2000.0 },
        { id: 203, category: "Food", description: "Meals & beach shack dining", estimatedAmount: 9000.0, actualAmount: 0.0 },
        { id: 204, category: "Sightseeing and activities", description: "Water sports & monument fees", estimatedAmount: 5400.0, actualAmount: 0.0 },
        { id: 205, category: "Shopping and miscellaneous", description: "Souvenirs and emergency buffer", estimatedAmount: 3600.0, actualAmount: 0.0 }
      ]
    };

    localStorage.setItem('TM_TRIPS', JSON.stringify([sampleTrip]));
    localStorage.setItem('TM_INITIALIZED', 'true');
  }
}
initLocalStore();

const API = {
  // Generic request wrapper with automatic offline demo fallback
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
      credentials: 'same-origin'
    };

    try {
      const response = await fetch(`${API_BASE}${endpoint}`, config);
      if (response.ok) {
        return await response.json();
      }
      throw new Error(`Server returned ${response.status}`);
    } catch (err) {
      // Fallback to client-side localStorage simulation when server is unavailable (e.g. static Vercel host)
      return this.handleFallback(endpoint, options);
    }
  },

  handleFallback(endpoint, options) {
    const method = options.method || 'GET';
    const body = options.body ? JSON.parse(options.body) : {};

    // Auth Me
    if (endpoint === '/auth/me') {
      const user = JSON.parse(localStorage.getItem('TM_USER') || 'null');
      if (user) return { success: true, message: "Demo authenticated", data: user };
      throw new Error("Not logged in");
    }

    // Login
    if (endpoint === '/auth/login') {
      const user = { id: 1, name: "Aarav Sharma", email: body.email || "demo@travelmate.com" };
      localStorage.setItem('TM_USER', JSON.stringify(user));
      return { success: true, message: "Logged in successfully.", data: user };
    }

    // Register
    if (endpoint === '/auth/register') {
      const user = { id: Date.now(), name: body.name || "Explorer", email: body.email };
      localStorage.setItem('TM_USER', JSON.stringify(user));
      return { success: true, message: "Registered successfully.", data: user };
    }

    // Logout
    if (endpoint === '/auth/logout') {
      localStorage.removeItem('TM_USER');
      return { success: true, message: "Logged out." };
    }

    // Destinations
    if (endpoint.startsWith('/destinations')) {
      return { success: true, message: "Destinations retrieved", data: STATIC_DESTINATIONS };
    }

    // Trips list
    if (endpoint.startsWith('/trips') && method === 'GET') {
      const trips = JSON.parse(localStorage.getItem('TM_TRIPS') || '[]');
      
      // If single trip GET /trips/{id}
      const match = endpoint.match(/\/trips\/(\d+)/);
      if (match) {
        const id = parseInt(match[1], 10);
        const trip = trips.find(t => t.id === id) || trips[0];
        return { success: true, message: "Trip details", data: trip };
      }

      return { success: true, message: "Trips retrieved", data: trips };
    }

    // Create Trip
    if (endpoint === '/trips' && method === 'POST') {
      const trips = JSON.parse(localStorage.getItem('TM_TRIPS') || '[]');
      const newId = Date.now();
      const budget = body.budget || 20000;
      
      const newTrip = {
        id: newId,
        tripName: body.tripName,
        source: body.source,
        destination: body.destination,
        departureDate: body.departureDate,
        returnDate: body.returnDate,
        travelers: body.travelers,
        budget: budget,
        travelPreference: body.travelPreference || 'Relaxation',
        status: 'Upcoming',
        durationDays: 5,
        totalEstimatedCost: budget,
        totalActualSpent: 0,
        remainingBudget: budget,
        itineraryItems: [
          { id: newId + 1, dayNumber: 1, timeOfDay: "Morning", activityName: `Arrival & Check-in in ${body.destination}`, description: "Settle in, unpack, and orient with local surroundings.", estimatedCost: Math.round(budget * 0.05) },
          { id: newId + 2, dayNumber: 1, timeOfDay: "Afternoon", activityName: "Local Market & Landmark Exploration", description: "Discover popular local streets and authentic cuisine.", estimatedCost: Math.round(budget * 0.08) },
          { id: newId + 3, dayNumber: 1, timeOfDay: "Evening", activityName: "Sunset Point & Leisure Dinner", description: "Enjoy twilight views followed by traditional dining.", estimatedCost: Math.round(budget * 0.10) }
        ],
        expenses: [
          { id: newId + 10, category: "Transportation", description: "Travel bookings & cabs", estimatedAmount: Math.round(budget * 0.25), actualAmount: 0 },
          { id: newId + 11, category: "Accommodation", description: "Stay bookings", estimatedAmount: Math.round(budget * 0.35), actualAmount: 0 },
          { id: newId + 12, category: "Food", description: "Meals & dining", estimatedAmount: Math.round(budget * 0.20), actualAmount: 0 },
          { id: newId + 13, category: "Sightseeing and activities", description: "Entry tickets & tours", estimatedAmount: Math.round(budget * 0.12), actualAmount: 0 },
          { id: newId + 14, category: "Shopping and miscellaneous", description: "Souvenirs & buffer", estimatedAmount: Math.round(budget * 0.08), actualAmount: 0 }
        ]
      };

      trips.unshift(newTrip);
      localStorage.setItem('TM_TRIPS', JSON.stringify(trips));
      return { success: true, message: "Trip created", data: newTrip };
    }

    // Update Trip
    if (endpoint.match(/\/trips\/(\d+)$/) && method === 'PUT') {
      const id = parseInt(endpoint.split('/')[2], 10);
      const trips = JSON.parse(localStorage.getItem('TM_TRIPS') || '[]');
      const trip = trips.find(t => t.id === id);
      if (trip) {
        Object.assign(trip, body);
        localStorage.setItem('TM_TRIPS', JSON.stringify(trips));
        return { success: true, message: "Trip updated", data: trip };
      }
    }

    // Delete Trip
    if (endpoint.match(/\/trips\/(\d+)$/) && method === 'DELETE') {
      const id = parseInt(endpoint.split('/')[2], 10);
      let trips = JSON.parse(localStorage.getItem('TM_TRIPS') || '[]');
      trips = trips.filter(t => t.id !== id);
      localStorage.setItem('TM_TRIPS', JSON.stringify(trips));
      return { success: true, message: "Trip deleted" };
    }

    // Add Itinerary Activity
    if (endpoint.match(/\/trips\/(\d+)\/itinerary$/) && method === 'POST') {
      const tripId = parseInt(endpoint.split('/')[2], 10);
      const trips = JSON.parse(localStorage.getItem('TM_TRIPS') || '[]');
      const trip = trips.find(t => t.id === tripId);
      if (trip) {
        const item = { ...body, id: Date.now() };
        trip.itineraryItems.push(item);
        localStorage.setItem('TM_TRIPS', JSON.stringify(trips));
        return { success: true, message: "Activity added", data: item };
      }
    }

    // Delete Itinerary Activity
    if (endpoint.match(/\/trips\/(\d+)\/itinerary\/(\d+)$/) && method === 'DELETE') {
      const parts = endpoint.split('/');
      const tripId = parseInt(parts[2], 10);
      const itemId = parseInt(parts[4], 10);
      const trips = JSON.parse(localStorage.getItem('TM_TRIPS') || '[]');
      const trip = trips.find(t => t.id === tripId);
      if (trip) {
        trip.itineraryItems = trip.itineraryItems.filter(i => i.id !== itemId);
        localStorage.setItem('TM_TRIPS', JSON.stringify(trips));
        return { success: true, message: "Activity deleted" };
      }
    }

    // Add Expense
    if (endpoint.match(/\/trips\/(\d+)\/expenses$/) && method === 'POST') {
      const tripId = parseInt(endpoint.split('/')[2], 10);
      const trips = JSON.parse(localStorage.getItem('TM_TRIPS') || '[]');
      const trip = trips.find(t => t.id === tripId);
      if (trip) {
        const exp = { ...body, id: Date.now() };
        trip.expenses.push(exp);
        trip.totalActualSpent = trip.expenses.reduce((a, b) => a + (b.actualAmount || 0), 0);
        trip.remainingBudget = trip.budget - trip.totalActualSpent;
        localStorage.setItem('TM_TRIPS', JSON.stringify(trips));
        return { success: true, message: "Expense added", data: exp };
      }
    }

    // Update Expense
    if (endpoint.match(/\/trips\/(\d+)\/expenses\/(\d+)$/) && method === 'PUT') {
      const parts = endpoint.split('/');
      const tripId = parseInt(parts[2], 10);
      const expId = parseInt(parts[4], 10);
      const trips = JSON.parse(localStorage.getItem('TM_TRIPS') || '[]');
      const trip = trips.find(t => t.id === tripId);
      if (trip) {
        const exp = trip.expenses.find(e => e.id === expId);
        if (exp) Object.assign(exp, body);
        trip.totalActualSpent = trip.expenses.reduce((a, b) => a + (b.actualAmount || 0), 0);
        trip.remainingBudget = trip.budget - trip.totalActualSpent;
        localStorage.setItem('TM_TRIPS', JSON.stringify(trips));
        return { success: true, message: "Expense updated", data: exp };
      }
    }

    // Delete Expense
    if (endpoint.match(/\/trips\/(\d+)\/expenses\/(\d+)$/) && method === 'DELETE') {
      const parts = endpoint.split('/');
      const tripId = parseInt(parts[2], 10);
      const expId = parseInt(parts[4], 10);
      const trips = JSON.parse(localStorage.getItem('TM_TRIPS') || '[]');
      const trip = trips.find(t => t.id === tripId);
      if (trip) {
        trip.expenses = trip.expenses.filter(e => e.id !== expId);
        trip.totalActualSpent = trip.expenses.reduce((a, b) => a + (b.actualAmount || 0), 0);
        trip.remainingBudget = trip.budget - trip.totalActualSpent;
        localStorage.setItem('TM_TRIPS', JSON.stringify(trips));
        return { success: true, message: "Expense deleted" };
      }
    }

    // Contact
    if (endpoint === '/contact') {
      return { success: true, message: "Thank you for reaching out! Our travel team will respond promptly." };
    }

    return { success: true, message: "OK", data: null };
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
