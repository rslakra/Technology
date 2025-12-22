/**
 * Sidebar Class (Singleton)
 * Handles sidebar state, hamburger toggle, active menu items, and profile dropdown
 * Includes early initialization to prevent FOUC (Flash of Unstyled Content)
 * CSS does most of the work - JavaScript only handles state management
 */
(function() {
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
                wrapper.classList.add('active');
                this.applied = true;
                return true;
            }
            return false;
        }

        /**
         * Inject CSS to force collapsed state before rendering
         */
        injectInitialCSS() {
            // Check if style already exists
            if (document.getElementById('sidebar-initial-state')) {
                return;
            }

            var style = document.createElement('style');
            style.id = 'sidebar-initial-state';
            style.textContent = '.wrapper.active .sidebar { width: 80px !important; } .wrapper.active .main_container .container { width: calc(100% - 80px) !important; margin-left: 80px !important; } .wrapper.active .sidebar ul li a span.title, .wrapper.active .profile_info { display: none !important; }';
            (document.head || document.getElementsByTagName('head')[0]).appendChild(style);
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
                if (this.getSidebarState()) {
                    // Inject CSS immediately to force collapsed state before any rendering
                    this.injectInitialCSS();
                    
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
            } catch(e) {
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
         */
        setupHamburgerToggle() {
            $(".hamburger .hamburger-inner").click((e) => {
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
            $(".top_navbar .fas").click(() => {
                $(".profile-menu").toggleClass("active");
            });
        }

        /**
         * Set active menu item based on current URL
         */
        setActiveMenuItem() {
            var currentPath = window.location.pathname;
            $(".sidebar ul li a").removeClass("active");
            
            // Find the link that matches the current path
            $(".sidebar ul li a").each(function() {
                var href = $(this).attr("href");
                if (href && (currentPath === href || currentPath.startsWith(href + "/"))) {
                    $(this).addClass("active");
                    return false; // Break the loop
                }
            });
            
            // If no match found and we're on root, activate dashboard
            if (currentPath === "/" || currentPath === "") {
                $(".sidebar ul li a[href='/']").addClass("active");
            }
        }

        /**
         * Setup active menu item listeners
         */
        setupActiveMenuItemListeners() {
            // Set active menu item on page load
            this.setActiveMenuItem();

            // Set active menu item when clicking on sidebar links
            var self = this;
            $(".sidebar ul li a").click(function() {
                $(".sidebar ul li a").removeClass("active");
                $(this).addClass("active");
            });
        }

        /**
         * Activate settings tab based on URL hash
         */
        activateSettingsTab() {
            var hash = window.location.hash;
            if (hash && hash.startsWith('#account-')) {
                // Find the tab link that matches the hash
                var tabLink = $('.account-settings-links a[href="' + hash + '"]');
                if (tabLink.length > 0) {
                    // Remove active class from all tabs
                    $('.account-settings-links a').removeClass('active');
                    $('.tab-pane').removeClass('active show');
                    
                    // Activate the matching tab
                    tabLink.addClass('active');
                    var tabPane = $(hash);
                    if (tabPane.length > 0) {
                        tabPane.addClass('active show');
                    }
                }
            }
        }

        /**
         * Initialize sidebar (runs on document ready)
         */
        init() {
            $(document).ready(() => {
                this.wrapper = $(".wrapper");
                
                // Restore sidebar state from localStorage
                this.restoreSidebarState();
                
                // Mark sidebar as ready to enable smooth transitions
                this.markSidebarReady();

                // Setup event listeners
                this.setupHamburgerToggle();
                this.setupProfileDropdown();
                this.setupActiveMenuItemListeners();
                
                // Activate settings tab if hash is present in URL
                this.activateSettingsTab();
                
                // Listen for hash changes (e.g., when clicking profile menu links)
                window.addEventListener('hashchange', () => {
                    this.activateSettingsTab();
                });
            });
        }
    }

    // Get singleton instance
    var sidebar = Sidebar.getInstance();
    
    // Early initialization - runs immediately to prevent FOUC
    sidebar.initEarly();
    
    // Regular initialization - runs on document ready
    sidebar.init();
    
    // Make instance available globally if needed for debugging
    if (typeof window !== 'undefined') {
        window.sidebar = sidebar;
    }
})();
