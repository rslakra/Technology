/**
 * Modal Handlers (Singleton)
 * Centralized modal management for reusable modals across the application
 * Handles delete confirmation, generic modals, and alert auto-dismiss
 */
(function () {
    'use strict';

    /**
     * Modals class for managing modal interactions
     * Singleton pattern - only one instance can exist
     */
    class Modals {
        constructor() {
            // Prevent multiple instantiations
            if (Modals.instance) {
                return Modals.instance;
            }

            this.confirmModal = null;
            this.genericModal = null;
            this.initialized = false;

            // Store the instance
            Modals.instance = this;
        }

        /**
         * Get the singleton instance
         * @returns {Modals} - The singleton instance
         */
        static getInstance() {
            if (!Modals.instance) {
                Modals.instance = new Modals();
            }
            return Modals.instance;
        }

        /**
         * Initialize Bootstrap modal instances
         */
        initModals() {
            // Initialize delete confirmation modal
            var confirmModalElement = document.getElementById('confirmModal');
            if (confirmModalElement) {
                this.confirmModal = new bootstrap.Modal(confirmModalElement, {
                    backdrop: true,
                    keyboard: true,
                    focus: true
                });
            }

            // Initialize generic modal (for signup, contact, etc.)
            var genericModalElement = document.getElementById('myModal');
            if (genericModalElement) {
                this.genericModal = new bootstrap.Modal(genericModalElement, {
                    backdrop: true,
                    keyboard: true,
                    focus: true
                });
            }
        }

        /**
         * Setup delete confirmation modal handlers
         * Handles .btn-delete buttons that trigger delete confirmation
         */
        setupDeleteConfirmation() {
            var self = this;

            // Use event delegation to handle dynamically added delete buttons
            $(document).on('click', '.btn-delete', function (e) {
                e.preventDefault();
                var link = $(this);

                // Get the item title from data attributes
                var itemTitle = link.attr('taskTitle') ||
                    link.attr('tutorialTitle') ||
                    link.attr('data-title') ||
                    'this item';

                // Get the delete URL
                var deleteUrl = link.attr('href');

                // Get the item type (Task, Tutorial, etc.)
                var itemType = link.attr('data-type') || 'item';

                // Set the confirmation text
                var confirmText = 'Do you want to delete the \'' + itemType + '\'?<br><br><strong>' + itemTitle + '</strong>';
                $('#confirmText').html(confirmText);

                // Set the delete URL on the Yes button
                $('#yesBtn').attr('href', deleteUrl);

                // Show the modal
                if (self.confirmModal) {
                    self.confirmModal.show();
                } else {
                    // Fallback if modal not initialized
                    var modalElement = document.getElementById('confirmModal');
                    if (modalElement) {
                        var modal = new bootstrap.Modal(modalElement);
                        modal.show();
                    }
                }
            });
        }

        /**
         * Setup Yes button handler for delete confirmation
         */
        setupDeleteConfirmButton() {
            var self = this;

            $(document).on('click', '#yesBtn', function (e) {
                var deleteUrl = $(this).attr('href');
                if (deleteUrl) {
                    // Navigate to delete URL
                    window.location.href = deleteUrl;
                }
            });
        }

        /**
         * Setup generic modal handlers (for signup, contact forms, etc.)
         */
        setupGenericModal() {
            var self = this;

            // Handler for buttons that open generic modal
            $(document).on('click', '[data-modal="myModal"]', function (e) {
                e.preventDefault();
                var button = $(this);

                // Get modal title if specified
                var modalTitle = button.attr('data-modal-title') || 'Signup';
                $('#myModalLabel').text(modalTitle);

                // Get content URL if specified
                var contentUrl = button.attr('data-modal-url');
                if (contentUrl) {
                    $('#myModalBody').text('');
                    $.ajax({
                        url: contentUrl,
                        cache: false
                    }).done(function (html) {
                        $('#myModalBody').append(html);
                    }).fail(function () {
                        $('#myModalBody').text('Failed to load content.');
                    });
                }

                // Show the modal
                if (self.genericModal) {
                    self.genericModal.show();
                } else {
                    // Fallback if modal not initialized
                    var modalElement = document.getElementById('myModal');
                    if (modalElement) {
                        var modal = new bootstrap.Modal(modalElement);
                        modal.show();
                    }
                }
            });
        }

        /**
         * Setup auto-dismiss for alert messages
         * Automatically closes success alerts after 5 seconds
         */
        setupAutoDismissAlerts() {
            var alertElement = $('.alert.alert-success.alert-dismissible');
            if (alertElement.length > 0) {
                setTimeout(function () {
                    try {
                        var alert = bootstrap.Alert.getOrCreateInstance(alertElement[0]);
                        alert.close();
                    } catch (e) {
                        // Fallback: manually hide the alert
                        alertElement.fadeOut(300, function () {
                            $(this).remove();
                        });
                    }
                }, 5000);
            }
        }

        /**
         * Initialize all modal handlers
         */
        init() {
            if (this.initialized) {
                return;
            }

            var self = this;

            $(document).ready(function () {
                // Initialize Bootstrap modal instances
                self.initModals();

                // Setup delete confirmation handlers
                self.setupDeleteConfirmation();
                self.setupDeleteConfirmButton();

                // Setup generic modal handlers
                self.setupGenericModal();

                // Setup auto-dismiss alerts
                self.setupAutoDismissAlerts();

                self.initialized = true;
            });
        }
    }

    // Get singleton instance
    var modals = Modals.getInstance();

    // Initialize modals
    modals.init();

    // Make instance available globally if needed for debugging
    if (typeof window !== 'undefined') {
        window.modals = modals;
    }
})();

