/**
 * Create Trip Controller
 */

document.addEventListener('DOMContentLoaded', async () => {
  await checkAuth();
  initFormDefaults();
  setupFormValidation();
});

async function checkAuth() {
  try {
    const res = await API.getCurrentUser();
    if (!res || !res.success || !res.data) {
      showToast('Please log in to create and manage trips.', 'info');
      setTimeout(() => {
        window.location.href = 'login.html?redirect=create-trip.html';
      }, 900);
    }
  } catch (err) {
    showToast('Please log in to create and manage trips.', 'info');
    setTimeout(() => {
      window.location.href = 'login.html?redirect=create-trip.html';
    }, 900);
  }
}

function initFormDefaults() {
  const urlParams = new URLSearchParams(window.location.search);
  const destParam = urlParams.get('dest');

  const destInput = document.getElementById('trip-destination');
  const nameInput = document.getElementById('trip-name');
  const depInput = document.getElementById('trip-departure');
  const retInput = document.getElementById('trip-return');

  // Set default dates: Departure = +7 days, Return = +11 days
  const today = new Date();
  const defaultDep = new Date(today);
  defaultDep.setDate(today.getDate() + 7);

  const defaultRet = new Date(today);
  defaultRet.setDate(today.getDate() + 11);

  if (depInput && !depInput.value) {
    depInput.value = defaultDep.toISOString().split('T')[0];
    depInput.min = today.toISOString().split('T')[0];
  }

  if (retInput && !retInput.value) {
    retInput.value = defaultRet.toISOString().split('T')[0];
    retInput.min = depInput.value;
  }

  if (depInput && retInput) {
    depInput.addEventListener('change', () => {
      retInput.min = depInput.value;
      if (retInput.value && retInput.value < depInput.value) {
        retInput.value = depInput.value;
      }
    });
  }

  if (destParam && destInput) {
    destInput.value = destParam;
    if (nameInput && !nameInput.value) {
      nameInput.value = `${destParam} Holiday Escape`;
    }
  }
}

function setupFormValidation() {
  const form = document.getElementById('create-trip-form');
  const submitBtn = document.getElementById('trip-submit-btn');
  const btnText = document.getElementById('btn-text');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    clearErrors();

    const name = document.getElementById('trip-name').value.trim();
    const source = document.getElementById('trip-source').value.trim();
    const destination = document.getElementById('trip-destination').value.trim();
    const departureDate = document.getElementById('trip-departure').value;
    const returnDate = document.getElementById('trip-return').value;
    const travelers = parseInt(document.getElementById('trip-travelers').value, 10);
    const budget = parseFloat(document.getElementById('trip-budget').value);
    const travelPreference = document.getElementById('trip-preference').value;

    let hasError = false;

    if (!name) {
      showFieldError('trip-name', 'Trip name is required.');
      hasError = true;
    }

    if (!source) {
      showFieldError('trip-source', 'Starting location is required.');
      hasError = true;
    }

    if (!destination) {
      showFieldError('trip-destination', 'Destination is required.');
      hasError = true;
    }

    if (!departureDate) {
      showFieldError('trip-departure', 'Departure date is required.');
      hasError = true;
    }

    if (!returnDate) {
      showFieldError('trip-return', 'Return date is required.');
      hasError = true;
    } else if (departureDate && returnDate < departureDate) {
      showFieldError('trip-return', 'Return date cannot be earlier than departure date.');
      hasError = true;
    }

    if (isNaN(travelers) || travelers < 1) {
      showFieldError('trip-travelers', 'Number of travelers must be at least 1.');
      hasError = true;
    }

    if (isNaN(budget) || budget < 0) {
      showFieldError('trip-budget', 'Budget must be a non-negative number.');
      hasError = true;
    }

    if (hasError) return;

    // Submit payload
    submitBtn.disabled = true;
    btnText.textContent = 'Generating Itinerary...';

    const payload = {
      tripName: name,
      source: source,
      destination: destination,
      departureDate: departureDate,
      returnDate: returnDate,
      travelers: travelers,
      budget: budget,
      travelPreference: travelPreference
    };

    try {
      const res = await API.createTrip(payload);
      showToast('Trip created successfully with smart itinerary!', 'success');

      setTimeout(() => {
        if (res && res.data && res.data.id) {
          window.location.href = `trip-details.html?id=${res.data.id}`;
        } else {
          window.location.href = 'my-trips.html';
        }
      }, 700);
    } catch (err) {
      showToast(err.message || 'Failed to create trip. Please try again.', 'error');
      submitBtn.disabled = false;
      btnText.textContent = 'Generate Itinerary & Trip';
    }
  });

  function clearErrors() {
    ['trip-name', 'trip-source', 'trip-destination', 'trip-departure', 'trip-return', 'trip-travelers', 'trip-budget'].forEach(id => {
      const el = document.getElementById(id);
      if (el) el.classList.remove('is-invalid');
      const errEl = document.getElementById(`${id}-error`);
      if (errEl) errEl.textContent = '';
    });
  }

  function showFieldError(id, msg) {
    const el = document.getElementById(id);
    if (el) el.classList.add('is-invalid');
    const errEl = document.getElementById(`${id}-error`);
    if (errEl) errEl.textContent = msg;
  }
}
