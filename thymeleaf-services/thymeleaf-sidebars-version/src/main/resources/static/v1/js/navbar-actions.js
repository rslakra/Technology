/**
 * Sidebar Class (Singleton)
 * Handles sidebar state, hamburger toggle, active menu items, and profile dropdown
 * Includes early initialization to prevent FOUC (Flash of Unstyled Content)
 * CSS does most of the work - JavaScript only handles state management
 */
(function () {
    'use strict';

    /**
     * Sidebar class for managing sidebar interactions
     * Singleton pattern - only one instance can exist
     */
    class Sidebar {
        constructor() {
            // Prevent multiple instantiations
            if (Sidebar.instance) {
                return Sidebar.instance;
            }

            this.wrapper = null;
            this.applied = false;
            this.observer = null;

            // Store the instance
            Sidebar.instance = this;
        }

        /**
         * Get the singleton instance
         * @returns {Sidebar} - The singleton instance
         */
        static getInstance() {
            if (!Sidebar.instance) {
                Sidebar.instance = new Sidebar();
            }
            return Sidebar.instance;
        }

        /**
         * Get sidebar state from localStorage
         * @returns {boolean} - True if sidebar should be collapsed
         */
        getSidebarState() {
            var sidebarState = localStorage.getItem('sidebarCollapsed');
            return sidebarState === 'true';
        }

        /**
         * Apply sidebar collapsed state to wrapper element (vanilla JS - for early init)
         * @returns {boolean} - True if state was applied, false otherwise
         */
        applyStateEarly() {
            if (this.applied) return true;
            var wrapper = document.querySelector('.wrapper');
            if (wrapper) {
                if (this.getSidebarState()) {
                    wrapper.classList.add('active');
                } else {
                    wrapper.classList.remove('active');
                }
                this.applied = true;
                return true;
            }
            return false;
        }

        /**
         * Setup MutationObserver to catch wrapper when added
         */
        setupObserver() {
            if (document.body) {
                this.observer = new MutationObserver(() => {
                    if (this.applyStateEarly()) {
                        this.disconnectObserver();
                    }
                });
                this.observer.observe(document.body, { childList: true, subtree: true });
            }
        }

        /**
         * Cleanup observer
         */
        disconnectObserver() {
            if (this.observer) {
                this.observer.disconnect();
                this.observer = null;
            }
        }

        /**
         * Early initialization - prevents FOUC (Flash of Unstyled Content)
         * This runs immediately when script loads, before DOM is ready
         */
        initEarly() {
            try {
                // Apply sidebar state early if it should be collapsed
                // CSS is handled in sidebar-styles.css with !important rules to prevent FOUC
                if (this.getSidebarState()) {
                    // Strategy 1: Try immediately
                    if (this.applyStateEarly()) return;

                    // Strategy 2: Use MutationObserver to catch wrapper when added
                    if (document.body) {
                        this.setupObserver();

                        // Strategy 3: Also try on DOMContentLoaded
                        if (document.readyState === 'loading') {
                            document.addEventListener('DOMContentLoaded', () => {
                                this.applyStateEarly();
                                this.disconnectObserver();
                            });
                        }
                    } else {
                        // Strategy 4: Wait for body, then use observer
                        if (document.readyState === 'loading') {
                            document.addEventListener('DOMContentLoaded', () => {
                                this.setupObserver();
                                setTimeout(() => {
                                    this.disconnectObserver();
                                }, 1000);
                            });
                        }
                    }
                }
            } catch (e) {
                // Silently fail if there's an error
            }
        }

        /**
         * Restore sidebar state from localStorage (jQuery version - for DOM ready)
         */
        restoreSidebarState() {
            if (this.getSidebarState()) {
                this.wrapper.addClass("active");
            } else {
                this.wrapper.removeClass("active");
            }
        }

        /**
         * Mark sidebar as ready to enable smooth transitions
         */
        markSidebarReady() {
            // Small delay ensures state is applied before transitions start
            setTimeout(() => {
                this.wrapper.addClass("sidebar-ready");
            }, 50);
        }

        /**
         * Setup hamburger button toggle
         * IMPORTANT: Only hamburger clicks should toggle sidebar, not sidebar menu items
         */
        setupHamburgerToggle() {
            // Handle hamburger click - be very specific to only target the hamburger button
            $(".hamburger .hamburger-inner, .hamburger .hamburger__inner").click((e) => {
                e.preventDefault();
                e.stopPropagation();

                // Ensure sidebar is ready for transitions
                if (!this.wrapper.hasClass("sidebar-ready")) {
                    this.wrapper.addClass("sidebar-ready");
                }

                // Toggle state - CSS handles the smooth animation
                this.wrapper.toggleClass("active");

                // Save state to localStorage
                var isCollapsed = this.wrapper.hasClass("active");
                localStorage.setItem('sidebarCollapsed', isCollapsed);
            });
        }

        /**
         * Setup profile dropdown toggle
         */
        setupProfileDropdown() {
            // Handle click on user icon to toggle profile dropdown
            $(".top_navbar .right_menu .fas, .top_navbar .menu .right_menu ul li .fas").click((e) => {
                e.stopPropagation();
                $(".profile-dropdown").toggleClass("active");
            });

            // Close dropdown when clicking outside
            $(document).click((e) => {
                if (!$(e.target).closest('.right_menu').length) {
                    $(".profile-dropdown").removeClass("active");
                }
            });
        }

        /**
         * Set active menu item based on current URL
         */
        setActiveMenuItem() {
            var currentPath = window.location.pathname;

            // Remove active class from all menu items
            $(".sidebar ul li a").removeClass("active");

            // Try to find matching menu item
            $(".sidebar ul li a").each(function () {
                var href = $(this).attr("href");
                if (href && currentPath.indexOf(href.replace(/\/$/, '')) !== -1 && href !== '/') {
                    $(this).addClass("active");
                    return false; // Break the loop
                }
            });

            // If no match found and we're on root, set home as active
            if (currentPath === '/' || currentPath.match(/\/v\d+\/?$/)) {
                $(".sidebar ul li a[href='/']").addClass("active");
            }
        }

        /**
         * Initialize sidebar (runs on document ready)
         */
        init() {
            $(document).ready(() => {
                this.wrapper = $(".wrapper");

                if (this.wrapper.length === 0) {
                    return;
                }

                // Restore sidebar state from localStorage
                this.restoreSidebarState();

                // Mark sidebar as ready to enable smooth transitions
                this.markSidebarReady();

                // Setup event handlers
                this.setupHamburgerToggle();
                this.setupProfileDropdown();
                this.setActiveMenuItem();

                // Set active menu item when clicking on sidebar links
                // IMPORTANT: Only update active state - do NOT touch sidebar expand/collapse state
                $(".sidebar ul li a").click(function (e) {
                    // Stop propagation to prevent any parent handlers from firing
                    e.stopPropagation();

                    // Don't prevent default - allow navigation
                    // Just update active state - sidebar state remains unchanged
                    $(".sidebar ul li a").removeClass("active");
                    $(this).addClass("active");

                    // Do NOT modify wrapper.active class here
                    // Sidebar expand/collapse is ONLY controlled by hamburger button
                });
            });
        }
    }

    // Initialize sidebar
    var sidebar = Sidebar.getInstance();

    // Early initialization (runs immediately)
    sidebar.initEarly();

    // Full initialization (runs on DOM ready)
    sidebar.init();

    // Expose sidebar instance globally for debugging
    if (typeof window !== 'undefined') {
        window.sidebar = sidebar;
    }
})();

