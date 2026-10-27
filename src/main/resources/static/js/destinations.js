/**
 * Destinations Page Controller
 */

let allDestinations = [];

document.addEventListener('DOMContentLoaded', () => {
  initFromUrlParams();
  loadDestinations();
  setupEventListeners();
});

function initFromUrlParams() {
  const urlParams = new URLSearchParams(window.location.search);
  const q = urlParams.get('query');
  const type = urlParams.get('type');

  if (q) {
    const input = document.getElementById('dest-search-input');
    if (input) input.value = q;
  }

  if (type) {
    const select = document.getElementById('type-filter');
    if (select) select.value = type;
  }
}

async function loadDestinations() {
  const grid = document.getElementById('destinations-display-grid');
  const countText = document.getElementById('results-count-text');

  try {
    const res = await API.getDestinations();
    if (res && res.success && res.data) {
      allDestinations = res.data;
      filterAndRenderDestinations();
    } else {
      grid.innerHTML = '<p class="text-muted" style="grid-column: 1 / -1; text-align: center;">No destinations found in database.</p>';
    }
  } catch (err) {
    grid.innerHTML = '<p class="text-muted" style="grid-column: 1 / -1; text-align: center;">Failed to connect to backend server.</p>';
  }
}

function filterAndRenderDestinations() {
  const searchInput = document.getElementById('dest-search-input');
  const typeFilter = document.getElementById('type-filter');
  const budgetFilter = document.getElementById('budget-filter');
  const sortSelect = document.getElementById('sort-select');
  const grid = document.getElementById('destinations-display-grid');
  const countText = document.getElementById('results-count-text');

  const q = searchInput ? searchInput.value.trim().toLowerCase() : '';
  const type = typeFilter ? typeFilter.value.toLowerCase() : '';
  const maxBudget = budgetFilter && budgetFilter.value ? parseFloat(budgetFilter.value) : null;
  const sort = sortSelect ? sortSelect.value : 'recommended';

  let filtered = allDestinations.filter(d => {
    const matchesName = !q || d.name.toLowerCase().includes(q) || (d.state && d.state.toLowerCase().includes(q));
    const matchesType = !type || (d.travelType && d.travelType.toLowerCase() === type);
    const matchesBudget = !maxBudget || (d.estimatedDailyCost <= maxBudget);
    return matchesName && matchesType && matchesBudget;
  });

  // Sorting
  if (sort === 'cost-asc') {
    filtered.sort((a, b) => (a.estimatedDailyCost || 0) - (b.estimatedDailyCost || 0));
  } else if (sort === 'cost-desc') {
    filtered.sort((a, b) => (b.estimatedDailyCost || 0) - (a.estimatedDailyCost || 0));
  } else if (sort === 'name-asc') {
    filtered.sort((a, b) => a.name.localeCompare(b.name));
  }

  if (countText) {
    countText.textContent = `Showing ${filtered.length} destination${filtered.length === 1 ? '' : 's'}`;
  }

  if (filtered.length === 0) {
    grid.innerHTML = `
      <div style="grid-column: 1 / -1; text-align: center; padding: 60px 20px; background: white; border-radius: var(--radius-md); border: 1px dashed var(--border-color);">
        <p style="font-size: 2.2rem; margin-bottom: 8px;">🔍</p>
        <h3 style="font-size: 1.25rem; margin-bottom: 6px;">No destinations matched your criteria</h3>
        <p class="text-muted" style="margin-bottom: 16px;">Try adjusting your keywords or clearing the budget/type filters.</p>
        <button id="clear-search-btn" class="btn btn-secondary btn-sm">Clear Filters</button>
      </div>
    `;

    const clearBtn = document.getElementById('clear-search-btn');
    if (clearBtn) {
      clearBtn.addEventListener('click', resetFilters);
    }
    return;
  }

  grid.innerHTML = filtered.map(dest => createCardHtml(dest)).join('');
}

function createCardHtml(dest) {
  const safeName = escapeHtml(dest.name);
  const safeState = escapeHtml(dest.state);
  const safeDesc = escapeHtml(dest.description);
  const safeType = escapeHtml(dest.travelType || 'Travel');
  const safeBestTime = escapeHtml(dest.bestTimeToVisit || 'All seasons');
  const safeAttractions = escapeHtml(dest.popularAttractions || '');
  const rating = dest.rating ? dest.rating.toFixed(1) : '4.8';
  const imgUrl = dest.imageUrl || 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80';

  return `
    <div class="destination-card">
      <div class="dest-img-container">
        <img src="${imgUrl}" alt="${safeName}" loading="lazy" onerror="this.src='https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80'">
        <span class="dest-type-badge">${safeType}</span>
        <div class="dest-rating"><span class="star">★</span> ${rating}</div>
      </div>
      <div class="dest-card-body">
        <div class="dest-location">${safeState}, India</div>
        <h3 class="dest-title">${safeName}</h3>
        <p class="dest-description">${safeDesc}</p>
        
        ${safeAttractions ? `
          <div style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 14px; line-height: 1.4;">
            <strong style="color: var(--text-main);">Highlights:</strong> ${safeAttractions}
          </div>
        ` : ''}

        <div class="dest-meta">
          <div class="dest-meta-item">
            <label>Best Time to Visit</label>
            <span style="font-size: 0.85rem; font-weight: 600;">${safeBestTime}</span>
          </div>
          <div class="dest-meta-item" style="text-align: right;">
            <label>Estimated Cost</label>
            <span class="cost-value">${formatINR(dest.estimatedDailyCost)}</span>
            <span class="cost-note">/day per person</span>
          </div>
        </div>

        <div class="dest-card-footer">
          <a href="create-trip.html?dest=${encodeURIComponent(dest.name)}" class="btn btn-primary btn-sm btn-block">Add to Trip Plan</a>
        </div>
      </div>
    </div>
  `;
}

function setupEventListeners() {
  const searchInput = document.getElementById('dest-search-input');
  const typeFilter = document.getElementById('type-filter');
  const budgetFilter = document.getElementById('budget-filter');
  const sortSelect = document.getElementById('sort-select');
  const resetBtn = document.getElementById('reset-filters-btn');

  if (searchInput) searchInput.addEventListener('input', debounce(filterAndRenderDestinations, 250));
  if (typeFilter) typeFilter.addEventListener('change', filterAndRenderDestinations);
  if (budgetFilter) budgetFilter.addEventListener('change', filterAndRenderDestinations);
  if (sortSelect) sortSelect.addEventListener('change', filterAndRenderDestinations);
  if (resetBtn) resetBtn.addEventListener('click', resetFilters);
}

function resetFilters() {
  const searchInput = document.getElementById('dest-search-input');
  const typeFilter = document.getElementById('type-filter');
  const budgetFilter = document.getElementById('budget-filter');
  const sortSelect = document.getElementById('sort-select');

  if (searchInput) searchInput.value = '';
  if (typeFilter) typeFilter.value = '';
  if (budgetFilter) budgetFilter.value = '';
  if (sortSelect) sortSelect.value = 'recommended';

  filterAndRenderDestinations();
}

function debounce(fn, delay) {
  let timeoutId;
  return function(...args) {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => fn.apply(this, args), delay);
  };
}
