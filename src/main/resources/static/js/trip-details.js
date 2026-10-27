/**
 * Trip Details, Itinerary & Budget Manager Controller
 */

let currentTrip = null;
let currentTripId = null;

document.addEventListener('DOMContentLoaded', async () => {
  const urlParams = new URLSearchParams(window.location.search);
  currentTripId = urlParams.get('id');

  if (!currentTripId) {
    showToast('No trip ID specified.', 'error');
    setTimeout(() => window.location.href = 'my-trips.html', 1000);
    return;
  }

  await loadTripDetails();
  setupTabSwitcher();
  setupActivityModal();
  setupExpenseModal();
  setupEditTripModal();
});

async function loadTripDetails() {
  try {
    const res = await API.getTrip(currentTripId);
    if (res && res.success && res.data) {
      currentTrip = res.data;
      renderTripBanner(currentTrip);
      renderItinerary(currentTrip.itineraryItems || []);
      renderBudget(currentTrip);
    } else {
      showToast('Could not load trip details.', 'error');
    }
  } catch (err) {
    showToast(err.message || 'Error loading trip details.', 'error');
    setTimeout(() => window.location.href = 'my-trips.html', 1500);
  }
}

/* ==========================================================================
   Trip Banner Rendering
   ========================================================================== */
function renderTripBanner(trip) {
  const container = document.getElementById('trip-banner-container');
  if (!container) return;

  const safeTitle = escapeHtml(trip.tripName);
  const safeSource = escapeHtml(trip.source);
  const safeDest = escapeHtml(trip.destination);
  const safePref = escapeHtml(trip.travelPreference || 'Relaxation');
  const departureFmt = formatDate(trip.departureDate);
  const returnFmt = formatDate(trip.returnDate);
  const status = trip.status || 'Upcoming';

  const budgetFmt = formatINR(trip.budget);
  const estimatedFmt = formatINR(trip.totalEstimatedCost);
  const actualFmt = formatINR(trip.totalActualSpent);
  const remaining = trip.budget - trip.totalActualSpent;
  const remainingFmt = formatINR(remaining);
  const remainingColor = remaining >= 0 ? '#34d399' : '#f87171';

  container.innerHTML = `
    <div class="trip-banner-top">
      <div>
        <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 8px;">
          <span style="font-weight: 700; background: rgba(255,255,255,0.25); padding: 4px 12px; border-radius: var(--radius-pill); font-size: 0.8rem;">
            ${safeSource} → ${safeDest}
          </span>
          <span style="font-weight: 700; background: rgba(255,255,255,0.25); padding: 4px 12px; border-radius: var(--radius-pill); font-size: 0.8rem;">
            ${status}
          </span>
        </div>
        <h1>${safeTitle}</h1>
        <div class="banner-subtitle">
          📅 ${departureFmt} - ${returnFmt} (${trip.durationDays} Days) • 👥 ${trip.travelers} Traveler${trip.travelers > 1 ? 's' : ''} • 🎯 ${safePref}
        </div>
      </div>

      <div>
        <button class="btn btn-secondary btn-sm" id="btn-open-edit-trip" style="background: white; color: var(--primary-dark);">
          ✏ Edit Trip Details
        </button>
      </div>
    </div>

    <!-- 4 Metrics Cards -->
    <div class="trip-metrics-grid">
      <div class="metric-card">
        <label>Total Budget</label>
        <div class="metric-val">${budgetFmt}</div>
      </div>
      <div class="metric-card">
        <label>Estimated Total Cost</label>
        <div class="metric-val">${estimatedFmt}</div>
      </div>
      <div class="metric-card">
        <label>Actual Spending</label>
        <div class="metric-val">${actualFmt}</div>
      </div>
      <div class="metric-card">
        <label>Remaining Budget</label>
        <div class="metric-val" style="color: ${remainingColor};">${remainingFmt}</div>
      </div>
    </div>
  `;

  // Attach Edit Trip event
  const editTripBtn = document.getElementById('btn-open-edit-trip');
  if (editTripBtn) {
    editTripBtn.addEventListener('click', openEditTripModal);
  }
}

/* ==========================================================================
   Itinerary View Rendering
   ========================================================================== */
function renderItinerary(items) {
  const container = document.getElementById('itinerary-days-container');
  if (!container) return;

  if (items.length === 0) {
    container.innerHTML = `
      <div class="empty-state">
        <div class="empty-state-icon">📋</div>
        <h3>No Activities Scheduled</h3>
        <p>Your itinerary is currently empty. Add your first morning, afternoon, or evening activity!</p>
        <button class="btn btn-primary" onclick="openAddActivityModal()">+ Add First Activity</button>
      </div>
    `;
    return;
  }

  // Group items by dayNumber
  const daysMap = {};
  items.forEach(item => {
    if (!daysMap[item.dayNumber]) daysMap[item.dayNumber] = [];
    daysMap[item.dayNumber].push(item);
  });

  // Sort days
  const sortedDays = Object.keys(daysMap).map(Number).sort((a, b) => a - b);

  let html = '';
  sortedDays.forEach(dayNum => {
    const dayItems = daysMap[dayNum];

    // Order items: Morning -> Afternoon -> Evening
    const timeOrder = { 'morning': 1, 'afternoon': 2, 'evening': 3 };
    dayItems.sort((a, b) => {
      const orderA = timeOrder[a.timeOfDay.toLowerCase()] || 4;
      const orderB = timeOrder[b.timeOfDay.toLowerCase()] || 4;
      return orderA - orderB;
    });

    const dayTotalCost = dayItems.reduce((acc, curr) => acc + (curr.estimatedCost || 0), 0);

    html += `
      <div class="itinerary-day-box">
        <div class="day-header">
          <h3>
            <span>🗓 Day ${dayNum}</span>
          </h3>
          <div style="display: flex; align-items: center; gap: 14px;">
            <span style="font-size: 0.85rem; font-weight: 600; color: var(--text-muted);">
              Day Est: <strong style="color: var(--primary);">${formatINR(dayTotalCost)}</strong>
            </span>
            <button class="btn btn-outline btn-sm" onclick="openAddActivityModal(${dayNum})">+ Add Activity</button>
          </div>
        </div>

        <div class="activities-list">
          ${dayItems.map(item => createActivityItemHtml(item)).join('')}
        </div>
      </div>
    `;
  });

  container.innerHTML = html;
}

function createActivityItemHtml(item) {
  const safeName = escapeHtml(item.activityName);
  const safeDesc = escapeHtml(item.description || '');
  const time = item.timeOfDay || 'Morning';
  const timeClass = `time-${time.toLowerCase()}`;
  const costFmt = formatINR(item.estimatedCost);

  return `
    <div class="activity-item">
      <div>
        <span class="time-slot-badge ${timeClass}">${time}</span>
      </div>

      <div class="activity-info">
        <h4>${safeName}</h4>
        ${safeDesc ? `<p>${safeDesc}</p>` : ''}
      </div>

      <div class="activity-actions">
        <span class="activity-cost">${costFmt}</span>
        <button class="btn btn-secondary btn-sm" onclick="openEditActivityModal(${item.id})" title="Edit activity">✏</button>
        <button class="btn btn-danger-outline btn-sm" onclick="deleteActivity(${item.id})" title="Delete activity">🗑</button>
      </div>
    </div>
  `;
}

/* ==========================================================================
   Budget & Expense View Rendering
   ========================================================================== */
function renderBudget(trip) {
  const expenses = trip.expenses || [];
  const tbody = document.getElementById('expense-table-body');
  const progressBar = document.getElementById('budget-progress-bar');
  const progressPercentText = document.getElementById('budget-progress-percent');
  const progressStatusText = document.getElementById('budget-progress-status');

  const totalBudget = trip.budget || 0;
  const totalActual = expenses.reduce((acc, curr) => acc + (curr.actualAmount || 0), 0);
  const pct = totalBudget > 0 ? Math.round((totalActual / totalBudget) * 100) : 0;

  if (progressBar) {
    progressBar.style.width = `${Math.min(pct, 100)}%`;
    if (pct > 100) {
      progressBar.classList.add('exceeded');
    } else {
      progressBar.classList.remove('exceeded');
    }
  }

  if (progressPercentText) {
    progressPercentText.textContent = `${pct}% of Total Budget Spent`;
  }

  if (progressStatusText) {
    if (pct > 100) {
      progressStatusText.innerHTML = `<span style="color: var(--danger); font-weight: 700;">⚠️ Budget Exceeded by ${formatINR(totalActual - totalBudget)}</span>`;
    } else {
      progressStatusText.innerHTML = `<span>Spent ${formatINR(totalActual)} of ${formatINR(totalBudget)}</span>`;
    }
  }

  if (!tbody) return;

  if (expenses.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="6" style="text-align: center; padding: 30px; color: var(--text-muted);">
          No expenses recorded yet. Click "+ Add Expense Item" to start tracking!
        </td>
      </tr>
    `;
    return;
  }

  tbody.innerHTML = expenses.map(exp => {
    const safeCat = escapeHtml(exp.category);
    const safeDesc = escapeHtml(exp.description || '—');
    const estFmt = formatINR(exp.estimatedAmount);
    const actFmt = formatINR(exp.actualAmount);
    const diff = (exp.estimatedAmount || 0) - (exp.actualAmount || 0);

    let varianceHtml = '';
    if (exp.actualAmount === 0) {
      varianceHtml = '<span style="color: var(--text-muted); font-size: 0.85rem;">Not spent yet</span>';
    } else if (diff >= 0) {
      varianceHtml = `<span style="color: var(--success); font-weight: 600;">Within budget (${formatINR(diff)} left)</span>`;
    } else {
      varianceHtml = `<span style="color: var(--danger); font-weight: 600;">Over budget by ${formatINR(Math.abs(diff))}</span>`;
    }

    return `
      <tr>
        <td><strong>${safeCat}</strong></td>
        <td><span style="color: var(--text-muted);">${safeDesc}</span></td>
        <td><span style="font-weight: 600;">${estFmt}</span></td>
        <td><span style="font-weight: 700; color: var(--primary);">${actFmt}</span></td>
        <td>${varianceHtml}</td>
        <td style="text-align: right;">
          <button class="btn btn-secondary btn-sm" onclick="openEditExpenseModal(${exp.id})" title="Edit expense">✏</button>
          <button class="btn btn-danger-outline btn-sm" onclick="deleteExpense(${exp.id})" title="Delete expense">🗑</button>
        </td>
      </tr>
    `;
  }).join('');
}

/* ==========================================================================
   Tab Switcher
   ========================================================================== */
function setupTabSwitcher() {
  const tabItinerary = document.getElementById('tab-btn-itinerary');
  const tabBudget = document.getElementById('tab-btn-budget');
  const viewItinerary = document.getElementById('itinerary-view');
  const viewBudget = document.getElementById('budget-view');

  if (tabItinerary && tabBudget) {
    tabItinerary.addEventListener('click', () => {
      tabItinerary.classList.add('active');
      tabBudget.classList.remove('active');
      viewItinerary.style.display = 'block';
      viewBudget.style.display = 'none';
    });

    tabBudget.addEventListener('click', () => {
      tabBudget.classList.add('active');
      tabItinerary.classList.remove('active');
      viewItinerary.style.display = 'none';
      viewBudget.style.display = 'block';
    });
  }
}

/* ==========================================================================
   Activity Modal & CRUD
   ========================================================================== */
function setupActivityModal() {
  const modal = document.getElementById('activity-modal');
  const closeBtn = document.getElementById('activity-modal-close');
  const cancelBtn = document.getElementById('activity-modal-cancel');
  const form = document.getElementById('activity-form');
  const openAddBtn = document.getElementById('btn-open-add-activity');

  const closeModal = () => {
    if (modal) modal.classList.remove('active');
  };

  if (closeBtn) closeBtn.addEventListener('click', closeModal);
  if (cancelBtn) cancelBtn.addEventListener('click', closeModal);
  if (openAddBtn) openAddBtn.addEventListener('click', () => openAddActivityModal());

  if (form) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const editId = document.getElementById('activity-edit-id').value;
      const day = parseInt(document.getElementById('activity-day').value, 10);
      const time = document.getElementById('activity-time').value;
      const name = document.getElementById('activity-name').value.trim();
      const desc = document.getElementById('activity-desc').value.trim();
      const cost = parseFloat(document.getElementById('activity-cost').value) || 0.0;

      if (!name) {
        showToast('Activity name is required', 'error');
        return;
      }

      const payload = {
        tripId: currentTripId,
        dayNumber: day,
        timeOfDay: time,
        activityName: name,
        description: desc,
        estimatedCost: cost
      };

      try {
        if (editId) {
          await API.updateItineraryActivity(currentTripId, editId, payload);
          showToast('Activity updated successfully', 'success');
        } else {
          await API.addItineraryActivity(currentTripId, payload);
          showToast('Activity added to itinerary', 'success');
        }
        closeModal();
        await loadTripDetails();
      } catch (err) {
        showToast(err.message || 'Failed to save activity', 'error');
      }
    });
  }
}

window.openAddActivityModal = function(prefillDay = 1) {
  const modal = document.getElementById('activity-modal');
  const title = document.getElementById('activity-modal-title');
  const form = document.getElementById('activity-form');

  if (form) form.reset();
  document.getElementById('activity-edit-id').value = '';
  document.getElementById('activity-day').value = prefillDay;
  if (title) title.textContent = 'Add Itinerary Activity';
  if (modal) modal.classList.add('active');
};

window.openEditActivityModal = function(itemId) {
  const item = (currentTrip.itineraryItems || []).find(i => i.id === itemId);
  if (!item) return;

  document.getElementById('activity-edit-id').value = item.id;
  document.getElementById('activity-day').value = item.dayNumber;
  document.getElementById('activity-time').value = item.timeOfDay;
  document.getElementById('activity-name').value = item.activityName;
  document.getElementById('activity-desc').value = item.description || '';
  document.getElementById('activity-cost').value = item.estimatedCost || 0;

  const title = document.getElementById('activity-modal-title');
  if (title) title.textContent = 'Edit Itinerary Activity';

  const modal = document.getElementById('activity-modal');
  if (modal) modal.classList.add('active');
};

window.deleteActivity = async function(itemId) {
  if (!confirm('Are you sure you want to delete this activity?')) return;
  try {
    await API.deleteItineraryActivity(currentTripId, itemId);
    showToast('Activity deleted', 'info');
    await loadTripDetails();
  } catch (err) {
    showToast(err.message || 'Failed to delete activity', 'error');
  }
};

/* ==========================================================================
   Expense Modal & CRUD
   ========================================================================== */
function setupExpenseModal() {
  const modal = document.getElementById('expense-modal');
  const closeBtn = document.getElementById('expense-modal-close');
  const cancelBtn = document.getElementById('expense-modal-cancel');
  const form = document.getElementById('expense-form');
  const openAddBtn = document.getElementById('btn-open-add-expense');

  const closeModal = () => {
    if (modal) modal.classList.remove('active');
  };

  if (closeBtn) closeBtn.addEventListener('click', closeModal);
  if (cancelBtn) cancelBtn.addEventListener('click', closeModal);
  if (openAddBtn) openAddBtn.addEventListener('click', () => openAddExpenseModal());

  if (form) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const editId = document.getElementById('expense-edit-id').value;
      const category = document.getElementById('expense-category').value;
      const desc = document.getElementById('expense-desc').value.trim();
      const est = parseFloat(document.getElementById('expense-estimated').value) || 0.0;
      const act = parseFloat(document.getElementById('expense-actual').value) || 0.0;

      const payload = {
        tripId: currentTripId,
        category: category,
        description: desc,
        estimatedAmount: est,
        actualAmount: act
      };

      try {
        if (editId) {
          await API.updateExpense(currentTripId, editId, payload);
          showToast('Expense updated successfully', 'success');
        } else {
          await API.addExpense(currentTripId, payload);
          showToast('Expense added successfully', 'success');
        }
        closeModal();
        await loadTripDetails();
      } catch (err) {
        showToast(err.message || 'Failed to save expense', 'error');
      }
    });
  }
}

window.openAddExpenseModal = function() {
  const modal = document.getElementById('expense-modal');
  const form = document.getElementById('expense-form');
  const title = document.getElementById('expense-modal-title');

  if (form) form.reset();
  document.getElementById('expense-edit-id').value = '';
  if (title) title.textContent = 'Add Expense Item';
  if (modal) modal.classList.add('active');
};

window.openEditExpenseModal = function(expenseId) {
  const exp = (currentTrip.expenses || []).find(e => e.id === expenseId);
  if (!exp) return;

  document.getElementById('expense-edit-id').value = exp.id;
  document.getElementById('expense-category').value = exp.category;
  document.getElementById('expense-desc').value = exp.description || '';
  document.getElementById('expense-estimated').value = exp.estimatedAmount || 0;
  document.getElementById('expense-actual').value = exp.actualAmount || 0;

  const title = document.getElementById('expense-modal-title');
  if (title) title.textContent = 'Edit Expense Item';

  const modal = document.getElementById('expense-modal');
  if (modal) modal.classList.add('active');
};

window.deleteExpense = async function(expenseId) {
  if (!confirm('Are you sure you want to delete this expense category?')) return;
  try {
    await API.deleteExpense(currentTripId, expenseId);
    showToast('Expense removed', 'info');
    await loadTripDetails();
  } catch (err) {
    showToast(err.message || 'Failed to delete expense', 'error');
  }
};

/* ==========================================================================
   Edit Trip Details Modal
   ========================================================================== */
function setupEditTripModal() {
  const modal = document.getElementById('edit-trip-modal');
  const closeBtn = document.getElementById('edit-trip-close');
  const cancelBtn = document.getElementById('edit-trip-cancel');
  const form = document.getElementById('edit-trip-form');

  const closeModal = () => {
    if (modal) modal.classList.remove('active');
  };

  if (closeBtn) closeBtn.addEventListener('click', closeModal);
  if (cancelBtn) cancelBtn.addEventListener('click', closeModal);

  if (form) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const name = document.getElementById('edit-trip-name').value.trim();
      const source = document.getElementById('edit-trip-source').value.trim();
      const dest = document.getElementById('edit-trip-destination').value.trim();
      const dep = document.getElementById('edit-trip-departure').value;
      const ret = document.getElementById('edit-trip-return').value;
      const travelers = parseInt(document.getElementById('edit-trip-travelers').value, 10);
      const budget = parseFloat(document.getElementById('edit-trip-budget').value);
      const pref = document.getElementById('edit-trip-preference').value;

      if (!name || !source || !dest || !dep || !ret) {
        showToast('Please fill in all required fields.', 'error');
        return;
      }

      if (ret < dep) {
        showToast('Return date cannot be earlier than departure date.', 'error');
        return;
      }

      const payload = {
        tripName: name,
        source: source,
        destination: dest,
        departureDate: dep,
        returnDate: ret,
        travelers: travelers,
        budget: budget,
        travelPreference: pref
      };

      try {
        await API.updateTrip(currentTripId, payload);
        showToast('Trip details updated successfully', 'success');
        closeModal();
        await loadTripDetails();
      } catch (err) {
        showToast(err.message || 'Failed to update trip.', 'error');
      }
    });
  }
}

function openEditTripModal() {
  if (!currentTrip) return;
  document.getElementById('edit-trip-name').value = currentTrip.tripName;
  document.getElementById('edit-trip-source').value = currentTrip.source;
  document.getElementById('edit-trip-destination').value = currentTrip.destination;
  document.getElementById('edit-trip-departure').value = currentTrip.departureDate;
  document.getElementById('edit-trip-return').value = currentTrip.returnDate;
  document.getElementById('edit-trip-travelers').value = currentTrip.travelers;
  document.getElementById('edit-trip-budget').value = currentTrip.budget;
  document.getElementById('edit-trip-preference').value = currentTrip.travelPreference || 'Relaxation';

  const modal = document.getElementById('edit-trip-modal');
  if (modal) modal.classList.add('active');
}
