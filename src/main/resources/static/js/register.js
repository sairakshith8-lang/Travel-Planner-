/**
 * Registration Page Controller
 */

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('register-form');
  const nameInput = document.getElementById('reg-name');
  const emailInput = document.getElementById('reg-email');
  const passwordInput = document.getElementById('reg-password');
  const confirmPasswordInput = document.getElementById('reg-confirm-password');
  const submitBtn = document.getElementById('reg-submit-btn');
  const btnText = document.getElementById('btn-text');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    clearErrors();

    const name = nameInput.value.trim();
    const email = emailInput.value.trim();
    const password = passwordInput.value;
    const confirmPassword = confirmPasswordInput.value;

    let hasError = false;

    if (!name || name.length < 2) {
      showFieldError('reg-name', 'Please enter your full name (at least 2 characters).');
      hasError = true;
    }

    if (!email) {
      showFieldError('reg-email', 'Please enter your email address.');
      hasError = true;
    } else if (!validateEmail(email)) {
      showFieldError('reg-email', 'Please enter a valid email address.');
      hasError = true;
    }

    if (!password) {
      showFieldError('reg-password', 'Please enter a password.');
      hasError = true;
    } else if (password.length < 6) {
      showFieldError('reg-password', 'Password must be at least 6 characters long.');
      hasError = true;
    }

    if (password !== confirmPassword) {
      showFieldError('reg-confirm-password', 'Passwords do not match.');
      hasError = true;
    }

    if (hasError) return;

    // Loading State
    submitBtn.disabled = true;
    btnText.textContent = 'Creating account...';

    try {
      const res = await API.register(name, email, password);
      showToast(res.message || 'Account created successfully! Welcome to TravelMate.', 'success');

      setTimeout(() => {
        window.location.href = 'my-trips.html';
      }, 700);
    } catch (err) {
      showToast(err.message || 'Registration failed. Email might already be registered.', 'error');
      submitBtn.disabled = false;
      btnText.textContent = 'Create Free Account';
    }
  });

  function clearErrors() {
    ['reg-name', 'reg-email', 'reg-password', 'reg-confirm-password'].forEach(id => {
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

  function validateEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }
});
