/**
 * Tutorial Actions Handler
 * Handles tutorial-specific actions like Clear and Add buttons
 */
(function() {
    'use strict';

    /**
     * TutorialActions class for managing tutorial page interactions
     */
    class TutorialActions {
        constructor() {
            this.initialized = false;
        }

        /**
         * Setup Clear button handler
         */
        setupClearButton() {
            $('#btnClear').on('click', function(e) {
                e.preventDefault();
                $('#keyword').val('');
                // Get URL from data attribute or use default
                var tutorialsUrl = $('#btnClear').data('tutorials-url') || '/tutorials';
                window.location = tutorialsUrl;
            });
        }

        /**
         * Setup Add button handler
         */
        setupAddButton() {
            $('#btnAddTutorial').on('click', function(e) {
                e.preventDefault();
                // Get URL from data attribute or use default
                var addUrl = $('#btnAddTutorial').data('add-url') || '/tutorials/add';
                window.location = addUrl;
            });
        }

        /**
         * Initialize tutorial actions
         */
        init() {
            if (this.initialized) {
                return;
            }

            var self = this;

            $(document).ready(function() {
                // Setup Clear button
                self.setupClearButton();

                // Setup Add button
                self.setupAddButton();

                self.initialized = true;
            });
        }
    }

    // Initialize tutorial actions
    var tutorialActions = new TutorialActions();
    tutorialActions.init();

    // Make instance available globally if needed for debugging
    if (typeof window !== 'undefined') {
        window.tutorialActions = tutorialActions;
    }
})();

