/**
 * Task Actions Handler
 * Handles task-specific actions like Clear and Add buttons
 */
(function() {
    'use strict';

    /**
     * TaskActions class for managing task page interactions
     */
    class TaskActions {
        constructor() {
            this.initialized = false;
        }

        /**
         * Setup Clear button handler
         */
        setupClearButton() {
            $('#btnClearTask').on('click', function(e) {
                e.preventDefault();
                $('#keyword').val('');
                // Get URL from data attribute or use default
                var tasksUrl = $('#btnClearTask').data('tasks-url') || '/tasks';
                window.location = tasksUrl;
            });
        }

        /**
         * Setup Add button handler
         */
        setupAddButton() {
            $('#btnAddTask').on('click', function(e) {
                e.preventDefault();
                // Get URL from data attribute or use default
                var addUrl = $('#btnAddTask').data('add-url') || '/tasks/add';
                window.location = addUrl;
            });
        }

        /**
         * Initialize task actions
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

    // Initialize task actions
    var taskActions = new TaskActions();
    taskActions.init();

    // Make instance available globally if needed for debugging
    if (typeof window !== 'undefined') {
        window.taskActions = taskActions;
    }
})();

