/**
 * Navigation Bar Controller & User Session Sync
 */

document.addEventListener('DOMContentLoaded', async () => {
  initNavbar();
});

async function initNavbar() {
  const navAuthContainer = document.getElementById('nav-auth-slot');
  const navToggleBtn = document.querySelector('.nav-toggle-btn');
  const navLinks = document.querySelector('.nav-links');

  // Mobile menu toggle
  if (navToggleBtn && navLinks) {
    navToggleBtn.addEventListener('click', () => {
      navLinks.classList.toggle('active');
    });
  }

  // Check login state
  try {
    const res = await API.getCurrentUser();
    if (res && res.success && res.data) {
      const user = res.data;
      if (navAuthContainer) {
        navAuthContainer.innerHTML = `
          <div class="user-menu">
            <a href="create-trip.html" class="btn btn-primary btn-sm">+ Plan Trip</a>
            <div class="user-badge" title="${user.email}">
              <div class="user-avatar-circle">${user.name.charAt(0).toUpperCase()}</div>
              <span>${escapeHtml(user.name.split(' ')[0])}</span>
            </div>
            <button id="logout-btn" class="btn btn-secondary btn-sm" title="Sign out">Logout</button>
          </div>
        `;

        const logoutBtn = document.getElementById('logout-btn');
        if (logoutBtn) {
          logoutBtn.addEventListener('click', async () => {
            try {
              await API.logout();
              showToast('Logged out successfully', 'info');
              setTimeout(() => {
                window.location.href = 'login.html';
              }, 600);
            } catch (err) {
              window.location.href = 'login.html';
            }
          });
        }
      }
    } else {
      renderGuestNav(navAuthContainer);
    }
  } catch (err) {
    renderGuestNav(navAuthContainer);
  }
}

function renderGuestNav(container) {
  if (!container) return;
  container.innerHTML = `
    <a href="login.html" class="btn btn-secondary btn-sm">Login</a>
    <a href="register.html" class="btn btn-primary btn-sm">Register</a>
  `;
}

function escapeHtml(str) {
  if (!str) return '';
  return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}
