// Helper to show action menu outside table so it isn't clipped by overflow
function showActionMenuForButton(btn) {
  try {
    // find button and target id
    const targetId = btn.getAttribute && btn.getAttribute('data-target');
    let menu = null;
    if (targetId) {
      menu = document.getElementById(targetId);
    }
    // fallback: find a nearby .action-menu-list inside the same action-menu container
    if (!menu) {
      const parent = btn.closest && btn.closest('.action-menu');
      if (parent) menu = parent.querySelector('.action-menu-list');
    }
    if (!menu) return;

    // clone menu into body if not already moved
    if (!menu.__clone) {
      const clone = menu.cloneNode(true);
      clone.style.position = 'absolute';
      clone.style.zIndex = 9999;
      clone.classList.add('action-menu-list-clone');
      clone.__original = menu;
      document.body.appendChild(clone);
      menu.__clone = clone;
    }

    const clone = menu.__clone;
    // toggle
    const isOpen = clone.style.display === 'block';
    if (isOpen) {
      clone.style.display = 'none';
      btn.setAttribute('aria-expanded', 'false');
      return;
    }

    // position near button (reflow to measure)
    clone.style.display = 'block';
    clone.style.left = '0px';
    clone.style.top = '0px';
    clone.style.visibility = 'hidden';
    // allow browser to compute sizes
    requestAnimationFrame(() => {
      const rect = btn.getBoundingClientRect();
      const cw = clone.offsetWidth;
      const ch = clone.offsetHeight;
      let left = rect.right - cw;
      if (left < 8) left = rect.left; // avoid off-screen
      let top = rect.bottom + 6;
      if (top + ch > window.innerHeight - 8) top = rect.top - ch - 6;
      clone.style.left = `${Math.max(8, left)}px`;
      clone.style.top = `${Math.max(8, top)}px`;
      clone.style.visibility = 'visible';
      btn.setAttribute('aria-expanded', 'true');
    });

    // click outside to close
    function onDocClick(e) {
      if (!clone.contains(e.target) && e.target !== btn) {
        clone.style.display = 'none';
        btn.setAttribute('aria-expanded', 'false');
        document.removeEventListener('click', onDocClick);
      }
    }
    setTimeout(() => document.addEventListener('click', onDocClick), 10);
  } catch (err) {
    // keep debug-friendly but avoid throwing to global
    try { console.error('showActionMenuForButton error', err); } catch (e) {}
  }
}

// small helper to close all menus (optional)
function closeAllActionMenus() {
  document.querySelectorAll('.action-menu-list').forEach(el => {
    if (el.__clone) el.__clone.style.display = 'none';
  });
}

// expose globally for debug (but avoid relying on inline onclick)
try { window.closeAllActionMenus = closeAllActionMenus; } catch (e) {}

// Delegated binding: wait until DOM is ready, then attach handler
function initActionMenuDelegation() {
  if (initActionMenuDelegation.__done) return;
  initActionMenuDelegation.__done = true;
  try {
    // attach delegated listener for menu buttons
    document.addEventListener('click', function (e) {
      // confirmable links/buttons/forms
      const confirmTarget = e.target.closest && e.target.closest('[data-confirm]');
      if (confirmTarget) {
        const msg = confirmTarget.getAttribute('data-confirm');
        if (msg && !window.confirm(msg)) {
          e.preventDefault();
          return;
        }
        // allow to continue
      }

      const btn = e.target.closest && e.target.closest('.action-menu-button');
      if (btn) {
        e.preventDefault();
        e.stopPropagation();
        showActionMenuForButton(btn);
      }
    });
    // small helpful log for debugging in dev
    try { console.info && console.info('action-menu delegated handler initialized'); } catch (e) {}
  } catch (err) {
    try { console.error('initActionMenuDelegation failed', err); } catch (e) {}
  }
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initActionMenuDelegation);
} else {
  initActionMenuDelegation();
}
