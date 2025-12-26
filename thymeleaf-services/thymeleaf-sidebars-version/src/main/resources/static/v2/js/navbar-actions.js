/**
 * Sidebar state management with localStorage persistence
 * This script restores and maintains the sidebar collapsed/expanded state
 */
(function () {
    'use strict';

    // Function to restore sidebar state - runs immediately when script loads
    function restoreSidebarState() {
        var sidebarStateKey = 'v2-sidebar-collapsed';
        var isCollapsed = localStorage.getItem(sidebarStateKey) === 'true';
        var wrapper = document.querySelector('.wrapper');

        if (wrapper) {
            if (isCollapsed) {
                wrapper.classList.add('active');
            } else {
                wrapper.classList.remove('active');
            }
        }
    }

    // Try to restore immediately if DOM is already ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', restoreSidebarState);
    } else {
        // DOM is already ready
        restoreSidebarState();
    }

    // jQuery ready handler for event bindings
    $(document).ready(function () {
        var $wrapper = $(".wrapper");
        var sidebarStateKey = 'v2-sidebar-collapsed';

        // Ensure state is restored (in case the above didn't catch it)
        var isCollapsed = localStorage.getItem(sidebarStateKey) === 'true';
        if (isCollapsed) {
            $wrapper.addClass("active");
        } else {
            $wrapper.removeClass("active");
        }

        // Hamburger toggle with state persistence
        $(".hamburger .hamburger__inner").click(function () {
            $wrapper.toggleClass("active");
            // Save state to localStorage
            var isNowCollapsed = $wrapper.hasClass("active");
            localStorage.setItem(sidebarStateKey, isNowCollapsed ? 'true' : 'false');
        });

        // Profile dropdown toggle - handle click on user icon
        $(".top_navbar .right_menu .fas, .top_navbar .menu .right_menu ul li .fas").click(function (e) {
            e.stopPropagation();
            $(".profile-dropdown").toggleClass("active");
        });

        // Close dropdown when clicking outside
        $(document).click(function (e) {
            if (!$(e.target).closest('.right_menu').length) {
                $(".profile-dropdown").removeClass("active");
            }
        });

        // Ensure menu item clicks maintain the sidebar state
        // Don't let navigation change the sidebar state
        $(".sidebar ul li a").on('click', function () {
            // Preserve current sidebar state - don't change it
            // The state will be restored on the next page load from localStorage
        });
    });
})();
