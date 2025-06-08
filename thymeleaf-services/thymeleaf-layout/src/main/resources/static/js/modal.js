/**
 * Show Modal Dialog
 */
function showModalDialog(nodeName) {
    const modal = document.getElementById(nodeName);
    modal.style.display = 'block';

    // Disable interaction with page content
    document.querySelectorAll('header, footer, .modal-open-inert').forEach(item => {
        item.setAttribute('inert', '');
    });

    // Find Close Button and Add Listener to Close on click.
    const closeButton = modal.querySelector('.close');
    if(closeButton) {
        // add event listener on click
        closeButton.addEventListener('click', () => {
            modal.style.display = 'none'; // Hide the modal
            document.querySelectorAll('[inert]').forEach(item => item.removeAttribute('inert'));
        });

        // Prevent closing by clicking outside
        window.onclick = function (event) {
            if (event.target === modal) {
                event.stopPropagation();
            }
        }; // end of onclick

    } //end if

} //end showModalDialog()
