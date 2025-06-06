// Show modal
function showLoginModalDialog() {
  const modal = document.getElementById('myModal');
  modal.style.display = 'block';

  // Disable interaction with page content
  document.querySelectorAll('header, footer, .modal-open-inert').forEach(item => {
    item.setAttribute('inert', '');
  });
}

// Close modal
document.addEventListener('DOMContentLoaded', () => {
  const modal = document.getElementById('myModal');
  const closeBtn = modal.querySelector('.close');

  closeBtn.onclick = () => {
    modal.style.display = 'none';
    document.querySelectorAll('[inert]').forEach(item => item.removeAttribute('inert'));
  };

  // Prevent closing by clicking outside
  window.onclick = function (event) {
    if (event.target === modal) {
      event.stopPropagation();
    }
  };
});
