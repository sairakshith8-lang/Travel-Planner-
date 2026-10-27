/**
 * About & Contact Page Controller
 */

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('contact-form');
  const nameInput = document.getElementById('contact-name');
  const emailInput = document.getElementById('contact-email');
  const subjectInput = document.getElementById('contact-subject');
  const messageInput = document.getElementById('contact-message');
  const submitBtn = document.getElementById('contact-submit-btn');
  const btnText = document.getElementById('contact-btn-text');

  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    clearErrors();

    const name = nameInput.value.trim();
    const email = emailInput.value.trim();
    const subject = subjectInput ? subjectInput.value.trim() : '';
    const message = messageInput.value.trim();

    let hasError = false;

    if (!name || name.length < 2) {
      showFieldError('contact-name', 'Please enter your name (at least 2 characters).');
      hasError = true;
    }

    if (!email) {
      showFieldError('contact-email', 'Please enter your email address.');
      hasError = true;
    } else if (!validateEmail(email)) {
      showFieldError('contact-email', 'Please enter a valid email address.');
      hasError = true;
    }

    if (!message || message.length < 10) {
      showFieldError('contact-message', 'Message must be at least 10 characters long.');
      hasError = true;
    }

    if (hasError) return;

    submitBtn.disabled = true;
    btnText.textContent = 'Sending message...';

    const payload = {
      name: name,
      email: email,
      subject: subject || 'General Inquiry',
      message: message
    };

    try {
      const res = await API.submitContact(payload);
      showToast(res.message || 'Thank you! Your message has been received.', 'success');
      form.reset();
    } catch (err) {
      showToast(err.message || 'Failed to submit message. Please try again.', 'error');
    } finally {
      submitBtn.disabled = false;
      btnText.textContent = 'Send Message';
    }
  });

  function clearErrors() {
    ['contact-name', 'contact-email', 'contact-message'].forEach(id => {
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
