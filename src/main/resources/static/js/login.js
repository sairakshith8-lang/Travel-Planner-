/**
 * Login Page Controller
 */

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('login-form');
  const emailInput = document.getElementById('login-email');
  const passwordInput = document.getElementById('login-password');
  const submitBtn = document.getElementById('login-submit-btn');
  const btnText = document.getElementById('btn-text');
  const fillDemoBtn = document.getElementById('fill-demo-btn');

  if (fillDemoBtn) {
    fillDemoBtn.addEventListener('click', () => {
      emailInput.value = 'demo@travelmate.com';
      passwordInput.value = 'password123';
      clearErrors();
    });
  }

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    clearErrors();

    const email = emailInput.value.trim();
    const password = passwordInput.value;

    let hasError = false;

    if (!email) {
      showFieldError('login-email', 'Please enter your email address.');
      hasError = true;
    } else if (!validateEmail(email)) {
      showFieldError('login-email', 'Please enter a valid email address.');
      hasError = true;
    }

    if (!password) {
      showFieldError('login-password', 'Please enter your password.');
      hasError = true;
    }

    if (hasError) return;

    // Loading State
    submitBtn.disabled = true;
    btnText.textContent = 'Signing in...';

    try {
      const res = await API.login(email, password);
      showToast(res.message || 'Login successful! Welcome back.', 'success');

      // Check for redirect parameter or default to my-trips.html
      const urlParams = new URLSearchParams(window.location.search);
      const redirectUrl = urlParams.get('redirect') || 'my-trips.html';

      setTimeout(() => {
        window.location.href = redirectUrl;
      }, 700);
    } catch (err) {
      showToast(err.message || 'Login failed. Please check your credentials.', 'error');
      submitBtn.disabled = false;
      btnText.textContent = 'Sign In';
    }
  });

  function clearErrors() {
    ['login-email', 'login-password'].forEach(id => {
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
