/**
 * On document Ready
 * Fade-Out Event
 */
//$(document).ready(function () {
//    $('#alert').fadeOut(5000); // 5 seconds x 1000 milisec = 5000 milisec
//
//});

/**
 * Closes an alert box after 5 seconds.
 */
window.addEventListener('DOMContentLoaded', () => {
    const alert = document.getElementById('alertMessage');
    if (alert) {
      // Wait 5 seconds then start fade out
      setTimeout(() => {
        alert.classList.remove('show'); // triggers fade out
        alert.classList.add('fade');    // ensure fade class exists

        // After fade transition (500ms), remove from DOM or hide
        setTimeout(() => {
          alert.remove(); // or alert.style.display = 'none';
        }, 500);
      }, 5000); // Delay before fading out (5 seconds)
    }
});
