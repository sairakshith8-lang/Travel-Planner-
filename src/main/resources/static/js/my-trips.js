/**
 * My Trips Dashboard Controller
 */

let currentFilter = 'all';
let tripToDeleteId = null;

document.addEventListener('DOMContentLoaded', async () => {
  const isAuth = await checkAuth();
  if (isAuth) {
    loadTrips();
    setupTabs();
    setupDeleteModal();
  }
});

async function checkAuth() {
  try {
    const res = await API.getCurrentUser();
    if (!res || !res.success || !res.data) {
      window.location.href = 'login.html?redirect=my-trips.html';
      return false;
    }
    const titleEl = document.getElementById('dashboard-title');
    if (titleEl && res.data.name) {
      titleEl.textContent = `${res.data.name.split(' ')[0]}'s Travel Plans`;
    }
    return true;
  } catch (err) {
    window.location.href = 'login.html?redirect=my-trips.html';
    return false;
  }
}

async function loadTrips() {
  const container = document.getElementById('trips-container');
  if (!container) return;

  container.innerHTML = '<div style="grid-column: 1 / -1; text-align: center; padding: 40px;"><p class="text-muted">Loading trips...</p></div>';

  try {
    const res = await API.getMyTrips(currentFilter);
    if (res && res.success && res.data) {
      renderTrips(res.data);
    } else {
      renderEmptyState();
    }
  } catch (err) {
    container.innerHTML = `<p class="text-muted" style="grid-column: 1 / -1; text-align: center;">Error loading trips: ${err.message}</p>`;
  }
}

function renderTrips(trips) {
  const container = document.getElementById('trips-container');
  if (!container) return;

  if (trips.length === 0) {
    renderEmptyState();
    return;
  }

  container.innerHTML = trips.map(trip => createTripCardHtml(trip)).join('');

  // Attach delete button listeners
  container.querySelectorAll('.btn-delete-trip').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      e.stopPropagation();
      const id = btn.getAttribute('data-id');
      const title = btn.getAttribute('data-title');
      openDeleteModal(id, title);
    });
  });
}

function createTripCardHtml(trip) {
  const safeTitle = escapeHtml(trip.tripName);
  const safeSource = escapeHtml(trip.source);
  const safeDest = escapeHtml(trip.destination);
  const safePref = escapeHtml(trip.travelPreference || 'General');
  const departureFormatted = formatDate(trip.departureDate);
  const returnFormatted = formatDate(trip.returnDate);
  const status = trip.status || 'Upcoming';

  let statusClass = 'status-upcoming';
  if (status.toLowerCase() === 'active') statusClass = 'status-active';
  if (status.toLowerCase() === 'completed') statusClass = 'status-completed';

  const budgetFmt = formatINR(trip.budget);
  const actualFmt = formatINR(trip.totalActualSpent);

  return `
    <div class="trip-card">
      <div class="trip-card-header">
        <div class="trip-route">
          <span>${safeSource}</span>
          <span>→</span>
          <span>${safeDest}</span>
        </div>
        <span class="trip-status-pill ${statusClass}">${status}</span>
      </div>

      <h3 class="trip-title">${safeTitle}</h3>

      <div class="trip-details-list">
        <div class="trip-detail-item">
          <label>Travel Dates</label>
          <span>${departureFormatted} - ${returnFormatted}</span>
        </div>
        <div class="trip-detail-item">
          <label>Duration &amp; Group</label>
          <span>${trip.durationDays} Days • ${trip.travelers} Traveler${trip.travelers > 1 ? 's' : ''}</span>
        </div>
        <div class="trip-detail-item">
          <label>Total Budget</label>
          <span style="color: var(--primary);">${budgetFmt}</span>
        </div>
        <div class="trip-detail-item">
          <label>Actual Spent</label>
          <span>${actualFmt}</span>
        </div>
      </div>

      <div class="trip-card-footer">
        <a href="trip-details.html?id=${trip.id}" class="btn btn-primary btn-sm" style="flex: 1;">View &amp; Manage Trip</a>
        <button class="btn btn-danger-outline btn-sm btn-delete-trip" data-id="${trip.id}" data-title="${safeTitle}" title="Delete Trip">🗑</button>
      </div>
    </div>
  `;
}

function renderEmptyState() {
  const container = document.getElementById('trips-container');
  if (!container) return;

  let msg = 'No travel plans found in this category.';
  if (currentFilter === 'all') {
    msg = 'You haven’t created any travel plans yet. Start by planning your first journey!';
  } else if (currentFilter === 'upcoming') {
    msg = 'You have no upcoming trips scheduled. Plan a new getaway now!';
  } else if (currentFilter === 'completed') {
    msg = 'No completed journeys found.';
  }

  container.innerHTML = `
    <div class="empty-state" style="grid-column: 1 / -1;">
      <div class="empty-state-icon">✈️</div>
      <h3>No Trips Found</h3>
      <p>${msg}</p>
      <a href="create-trip.html" class="btn btn-primary btn-lg">+ Plan Your First Trip</a>
    </div>
  `;
}

function setupTabs() {
  const tabs = document.querySelectorAll('.tab-btn');
  tabs.forEach(tab => {
    tab.addEventListener('click', () => {
      tabs.forEach(t => t.classList.remove('active'));
      tab.classList.add('active');
      currentFilter = tab.getAttribute('data-filter') || 'all';
      loadTrips();
    });
  });
}

function setupDeleteModal() {
  const modal = document.getElementById('delete-modal');
  const cancelBtn = document.getElementById('cancel-delete-btn');
  const cancelX = document.getElementById('cancel-delete-x');
  const confirmBtn = document.getElementById('confirm-delete-btn');

  const closeModal = () => {
    if (modal) modal.classList.remove('active');
    tripToDeleteId = null;
  };

  if (cancelBtn) cancelBtn.addEventListener('click', closeModal);
  if (cancelX) cancelX.addEventListener('click', closeModal);

  if (confirmBtn) {
    confirmBtn.addEventListener('click', async () => {
      if (!tripToDeleteId) return;
      confirmBtn.disabled = true;
      confirmBtn.textContent = 'Deleting...';

      try {
        await API.deleteTrip(tripToDeleteId);
        showToast('Trip deleted successfully', 'info');
        closeModal();
        loadTrips();
      } catch (err) {
        showToast(err.message || 'Failed to delete trip', 'error');
      } finally {
        confirmBtn.disabled = false;
        confirmBtn.textContent = 'Delete Trip';
      }
    });
  }
}

function openDeleteModal(id, title) {
  tripToDeleteId = id;
  const titleEl = document.getElementById('delete-trip-title');
  if (titleEl) titleEl.textContent = `"${title}"`;
  const modal = document.getElementById('delete-modal');
  if (modal) modal.classList.add('active');
}
