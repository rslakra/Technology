/**
 * Form Actions Handler
 * Handles common form actions like Cancel buttons
 */
(function() {
    'use strict';

    /**
     * FormActions class for managing form page interactions
     */
    class FormActions {
        constructor() {
            this.initialized = false;
        }

        /**
         * Setup Cancel button handlers
         * Handles buttons with id="btnCancel" that should navigate back
         */
        setupCancelButtons() {
            // Use event delegation to handle dynamically added cancel buttons
            $(document).on('click', '#btnCancel', function(e) {
                e.preventDefault();
                var button = $(this);
                
                // Get URL from data attribute or use default
                var cancelUrl = button.data('cancel-url');
                
                if (cancelUrl) {
                    window.location = cancelUrl;
                } else {
                    // Fallback: try to determine URL from current path
                    var currentPath = window.location.pathname;
                    if (currentPath.includes('/tasks/')) {
                        window.location = '/tasks';
                    } else if (currentPath.includes('/tutorials/')) {
                        window.location = '/tutorials';
                    } else {
                        // Default to home
                        window.location = '/';
                    }
                }
            });
        }

        /**
         * Initialize form actions
         */
        init() {
            if (this.initialized) {
                return;
            }

            var self = this;

            $(document).ready(function() {
                // Setup Cancel button handlers
                self.setupCancelButtons();

                self.initialized = true;
            });
        }
    }

    // Initialize form actions
    var formActions = new FormActions();
    formActions.init();

    // Make instance available globally if needed for debugging
    if (typeof window !== 'undefined') {
        window.formActions = formActions;
    }
})();

