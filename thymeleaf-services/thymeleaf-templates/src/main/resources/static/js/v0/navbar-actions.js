/**
 * Sidebar class for managing sidebar state and interactions
 * Implements singleton pattern to ensure only one instance exists
 */
class Sidebar {
    constructor() {
        if (Sidebar.instance) {
            return Sidebar.instance;
        }

        // DOM elements
        this.sidebar = null;
        this.wrapper = null;
        this.hamburger = null;
        this.toggle = null;
        this.modeSwitch = null;
        this.modeText = null;
        this.body = null;
        this.profileIcon = null;
        this.profileDropdown = null;

        // LocalStorage keys
        this.STORAGE_KEY = 'sidebarExpanded';
        this.DARK_MODE_KEY = 'darkMode';

        // Bind methods to preserve 'this' context
        this.handleHamburgerClick = this.handleHamburgerClick.bind(this);
        this.handleToggleClick = this.handleToggleClick.bind(this);
        this.handleModeSwitchClick = this.handleModeSwitchClick.bind(this);
        this.handleProfileIconClick = this.handleProfileIconClick.bind(this);

        Sidebar.instance = this;
    }

    /**
     * Initialize the sidebar by finding DOM elements and setting up event listeners
     */
    init() {
        // Re-fetch DOM elements on each init to handle page navigation
        this.sidebar = document.querySelector('.sidebar');
        this.wrapper = document.querySelector('.wrapper');
        this.hamburger = document.querySelector('.hamburger .hamburger__inner');
        this.toggle = document.querySelector('.sidebar .toggle');
        this.modeSwitch = document.querySelector('.sidebar .bottom-content .toggle-switch');
        this.modeText = document.querySelector('.sidebar .bottom-content .mode-text');
        this.body = document.body;
        this.profileIcon = document.querySelector('.top_navbar .fas.fa-user');
        this.profileDropdown = document.querySelector('.top_navbar .navbar-right-profile');

        // Restore sidebar state from localStorage (only if not already set by inline script)
        // The inline script in layout-v0.html handles immediate restoration to prevent flickering
        // This is just a safety check to ensure state is correct
        this.ensureStateCorrect();

        // Restore dark mode state
        this.restoreDarkMode();

        // Set up event listeners (this will re-attach them if needed)
        this.setupEventListeners();
    }

    /**
     * Toggle sidebar state (expand or collapse)
     * @param {boolean} expand - true to expand, false to collapse
     */
    toggleSidebar(expand) {
        if (this.sidebar) {
            // Use .sidebar.close for new sidebar structure
            if (expand) {
                this.sidebar.classList.remove('close');
                this.saveState(true);
            } else {
                this.sidebar.classList.add('close');
                this.saveState(false);
            }
        } else if (this.wrapper) {
            // Fallback: toggle wrapper.active for old sidebar structure
            if (expand) {
                this.wrapper.classList.add('active');
                this.saveState(true);
            } else {
                this.wrapper.classList.remove('active');
                this.saveState(false);
            }
        }
    }

    /**
     * Save sidebar state to localStorage
     * @param {boolean} isExpanded - true if sidebar is expanded, false if collapsed
     */
    saveState(isExpanded) {
        localStorage.setItem(this.STORAGE_KEY, isExpanded.toString());
    }

    /**
     * Ensure sidebar state matches localStorage (only corrects if mismatch)
     * This prevents unnecessary state changes that could cause flickering
     */
    ensureStateCorrect() {
        if (!this.sidebar) return;

        const sidebarState = localStorage.getItem(this.STORAGE_KEY);
        if (sidebarState !== null) {
            const isExpanded = sidebarState === 'true';
            const currentlyExpanded = !this.sidebar.classList.contains('close');

            // Only apply state if it doesn't match (prevents flickering)
            if (isExpanded !== currentlyExpanded) {
                if (isExpanded) {
                    this.sidebar.classList.remove('close');
                } else {
                    this.sidebar.classList.add('close');
                }
            }
        }
    }

    /**
     * Restore sidebar state from localStorage (with transitions)
     */
    restoreState() {
        const sidebarState = localStorage.getItem(this.STORAGE_KEY);
        if (sidebarState !== null) {
            const isExpanded = sidebarState === 'true';
            this.toggleSidebar(isExpanded);
        }
    }

    /**
     * Handle hamburger menu click event
     */
    handleHamburgerClick() {
        if (this.sidebar) {
            const isCurrentlyClose = this.sidebar.classList.contains('close');
            this.toggleSidebar(isCurrentlyClose);
        } else if (this.wrapper) {
            const isActive = this.wrapper.classList.contains('active');
            this.toggleSidebar(!isActive);
        }
    }

    /**
     * Handle sidebar toggle button click event
     */
    handleToggleClick() {
        if (this.sidebar) {
            const isCurrentlyClose = this.sidebar.classList.contains('close');
            this.toggleSidebar(isCurrentlyClose);
        }
    }

    /**
     * Set up event listeners for hamburger, toggle button, and dark mode switch
     */
    setupEventListeners() {
        // Handle hamburger click to toggle sidebar
        if (this.hamburger) {
            // Remove existing listener to avoid duplicates
            this.hamburger.removeEventListener('click', this.handleHamburgerClick);
            this.hamburger.addEventListener('click', this.handleHamburgerClick);
        }

        // Handle sidebar toggle button click
        if (this.toggle && this.sidebar) {
            this.toggle.removeEventListener('click', this.handleToggleClick);
            this.toggle.addEventListener('click', this.handleToggleClick);
        }

        // Handle dark mode toggle switch click
        if (this.modeSwitch) {
            this.modeSwitch.removeEventListener('click', this.handleModeSwitchClick);
            this.modeSwitch.addEventListener('click', this.handleModeSwitchClick);
        }

        // Handle profile icon click to toggle dropdown
        if (this.profileIcon) {
            this.profileIcon.removeEventListener('click', this.handleProfileIconClick);
            this.profileIcon.addEventListener('click', this.handleProfileIconClick);
        }

        // Handle dropdown menu item clicks
        if (this.profileDropdown) {
            const menuItems = this.profileDropdown.querySelectorAll('.navbar-profile-item');
            if (menuItems.length === 0) {
                console.warn('No menu items found in profile dropdown');
            }
            menuItems.forEach((item, index) => {
                // Remove existing listener to avoid duplicates
                const existingHandler = item._menuItemHandler;
                if (existingHandler) {
                    item.removeEventListener('click', existingHandler);
                }
                // Create new handler and store reference
                const handler = (e) => {
                    e.preventDefault();
                    e.stopPropagation();
                    this.handleMenuItemClick(e, item);
                };
                item._menuItemHandler = handler;
                item.addEventListener('click', handler);
                // Ensure cursor pointer style
                item.style.cursor = 'pointer';
            });
        } else {
            console.warn('Profile dropdown not found');
        }

        // Close dropdown when clicking outside (use a single listener)
        if (!this._outsideClickHandler) {
            this._outsideClickHandler = (e) => {
                if (this.profileDropdown && this.profileIcon) {
                    if (!this.profileIcon.contains(e.target) && !this.profileDropdown.contains(e.target)) {
                        this.profileDropdown.classList.remove('active');
                    }
                }
            };
            document.addEventListener('click', this._outsideClickHandler);
        }
    }

    /**
     * Handle dark mode toggle switch click event
     */
    handleModeSwitchClick() {
        if (!this.body) return;

        const isDark = this.body.classList.contains('dark');

        if (isDark) {
            this.body.classList.remove('dark');
            this.saveDarkMode(false);
            if (this.modeText) {
                this.modeText.textContent = 'Dark mode';
            }
        } else {
            this.body.classList.add('dark');
            this.saveDarkMode(true);
            if (this.modeText) {
                this.modeText.textContent = 'Light mode';
            }
        }
    }

    /**
     * Save dark mode state to localStorage
     * @param {boolean} isDark - true if dark mode is enabled, false otherwise
     */
    saveDarkMode(isDark) {
        localStorage.setItem(this.DARK_MODE_KEY, isDark.toString());
    }

    /**
     * Restore dark mode state from localStorage
     */
    restoreDarkMode() {
        if (!this.body) return;

        const darkModeState = localStorage.getItem(this.DARK_MODE_KEY);
        if (darkModeState === 'true') {
            this.body.classList.add('dark');
            if (this.modeText) {
                this.modeText.textContent = 'Light mode';
            }
        } else {
            this.body.classList.remove('dark');
            if (this.modeText) {
                this.modeText.textContent = 'Dark mode';
            }
        }
    }

    /**
     * Handle profile icon click event to toggle dropdown
     */
    handleProfileIconClick(e) {
        e.stopPropagation();
        if (this.profileDropdown) {
            this.profileDropdown.classList.toggle('active');
        }
    }

    /**
     * Get the base path (context path) from the current URL
     * @returns {string} The base path (e.g., '/thymeleaf-templates' or '')
     */
    getBasePath() {
        const pathname = window.location.pathname;
        // Find the position of '/v0' in the pathname
        const v0Index = pathname.indexOf('/v0');
        if (v0Index > 0) {
            // Return everything before '/v0' as the base path
            return pathname.substring(0, v0Index);
        }
        // If '/v0' is at the start, there's no context path
        return '';
    }

    /**
     * Handle menu item click event
     * @param {Event} e - Click event
     * @param {HTMLElement} item - The clicked menu item
     */
    handleMenuItemClick(e, item) {
        e.preventDefault();
        e.stopPropagation();

        const action = item.getAttribute('data-action');
        if (!action) {
            console.warn('Menu item has no data-action attribute');
            return;
        }

        if (this.profileDropdown) {
            this.profileDropdown.classList.remove('active');
        }

        const basePath = this.getBasePath();

        switch (action) {
            case 'profile':
                // Navigate to settings page with account tab
                window.location.href = basePath + '/v0/setting#account';
                break;
            case 'password':
                // Navigate to settings page with password tab
                window.location.href = basePath + '/v0/setting#password';
                break;
            case 'logout':
                // Navigate to home screen
                window.location.href = basePath + '/v0';
                break;
            default:
                console.warn('Unknown menu action:', action);
        }
    }

    /**
     * Get current sidebar state
     * @returns {boolean} true if expanded, false if collapsed
     */
    isExpanded() {
        if (this.sidebar) {
            return !this.sidebar.classList.contains('close');
        } else if (this.wrapper) {
            return this.wrapper.classList.contains('active');
        }
        return false;
    }

    /**
     * Expand the sidebar
     */
    expand() {
        this.toggleSidebar(true);
    }

    /**
     * Collapse the sidebar
     */
    collapse() {
        this.toggleSidebar(false);
    }
}

/**
 * Activate tab based on hash fragment
 */
function activateTabFromHash() {
    if (!window.location.pathname.includes('/v0/setting')) {
        return;
    }

    const hash = window.location.hash;
    if (!hash) {
        return;
    }

    // Remove the # symbol
    const tabId = hash.substring(1);

    // Find the tab link element (e.g., #account-tab)
    const tabLink = document.querySelector(`#${tabId}-tab`);
    // Find the tab pane element (e.g., #account)
    const tabPane = document.querySelector(`#${tabId}`);

    if (!tabLink || !tabPane) {
        console.warn(`Tab elements not found for hash: ${hash}`);
        return;
    }

    // Remove active class from all tabs and panes
    document.querySelectorAll('.nav-pills .nav-link').forEach(link => {
        link.classList.remove('active');
        link.setAttribute('aria-selected', 'false');
    });

    document.querySelectorAll('.tab-pane').forEach(pane => {
        pane.classList.remove('show', 'active');
    });

    // Activate the target tab link
    tabLink.classList.add('active');
    tabLink.setAttribute('aria-selected', 'true');

    // Activate the target tab pane
    tabPane.classList.add('show', 'active');

    // Try Bootstrap's tab method if available
    if (typeof $ !== 'undefined' && $.fn.tab) {
        try {
            $(tabLink).tab('show');
        } catch (e) {
            console.warn('Bootstrap tab method failed, using manual activation:', e);
        }
    }
}

// Initialize sidebar when DOM is ready
$(document).ready(function () {
    const sidebar = new Sidebar();
    sidebar.init();

    // Handle hash-based tab activation on settings page
    // Try immediately and also after a short delay to ensure Bootstrap is loaded
    activateTabFromHash();

    // Also try after a delay in case Bootstrap loads later
    setTimeout(activateTabFromHash, 100);
    setTimeout(activateTabFromHash, 300);
});

// Also handle hash changes (when navigating within the same page)
window.addEventListener('hashchange', function () {
    activateTabFromHash();
});
