/**
 * Home Page Controller
 */

document.addEventListener('DOMContentLoaded', () => {
  loadFeaturedDestinations();
  setupQuickSearch();
});

async function loadFeaturedDestinations() {
  const grid = document.getElementById('popular-destinations-grid');
  if (!grid) return;

  try {
    const res = await API.getDestinations();
    if (res && res.success && res.data && res.data.length > 0) {
      // Pick top 6 destinations
      const featured = res.data.slice(0, 6);
      grid.innerHTML = featured.map(dest => createDestinationCardHtml(dest)).join('');
    } else {
      grid.innerHTML = '<p class="text-muted" style="grid-column: 1 / -1; text-align: center;">No destinations found.</p>';
    }
  } catch (err) {
    grid.innerHTML = `<p class="text-muted" style="grid-column: 1 / -1; text-align: center;">Failed to load destinations. Ensure backend is running.</p>`;
  }
}

function createDestinationCardHtml(dest) {
  const safeName = escapeHtml(dest.name);
  const safeState = escapeHtml(dest.state);
  const safeDesc = escapeHtml(dest.description);
  const safeType = escapeHtml(dest.travelType || 'Travel');
  const safeBestTime = escapeHtml(dest.bestTimeToVisit || 'Year round');
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
        
        <div class="dest-meta">
          <div class="dest-meta-item">
            <label>Best Season</label>
            <span style="font-size: 0.85rem; font-weight: 600;">${safeBestTime}</span>
          </div>
          <div class="dest-meta-item" style="text-align: right;">
            <label>Est. Daily Cost</label>
            <span class="cost-value">${formatINR(dest.estimatedDailyCost)}</span>
            <span class="cost-note">/person (est.)</span>
          </div>
        </div>

        <div class="dest-card-footer">
          <a href="create-trip.html?dest=${encodeURIComponent(dest.name)}" class="btn btn-primary btn-sm btn-block">Plan Trip Here</a>
        </div>
      </div>
    </div>
  `;
}

function setupQuickSearch() {
  const btn = document.getElementById('quick-search-btn');
  const input = document.getElementById('quick-dest-input');
  const select = document.getElementById('quick-type-select');

  if (!btn) return;

  const performSearch = () => {
    const q = input ? input.value.trim() : '';
    const type = select ? select.value : '';
    const params = new URLSearchParams();
    if (q) params.append('query', q);
    if (type) params.append('type', type);
    window.location.href = `destinations.html?${params.toString()}`;
  };

  btn.addEventListener('click', performSearch);
  if (input) {
    input.addEventListener('keypress', (e) => {
      if (e.key === 'Enter') performSearch();
    });
  }
}
