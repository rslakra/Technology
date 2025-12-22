/**
 * Breadcrumb Navigation Class (Singleton)
 * Generates breadcrumbs based on the current URL path and handles dynamic updates
 */
(function() {
    'use strict';

    /**
     * Breadcrumb class for managing breadcrumb navigation
     * Singleton pattern - only one instance can exist
     */
    class Breadcrumb {
        constructor() {
            // Prevent multiple instantiations
            if (Breadcrumb.instance) {
                return Breadcrumb.instance;
            }

            // Map URL paths to friendly labels
            this.pathLabels = {
                '/': 'Home',
                '/tasks': 'Tasks',
                '/tutorials': 'Tutorials',
                '/reports': 'Reports',
                '/admin': 'Admin',
                '/settings': 'Settings',
                '/dashboard': 'Dashboard'
            };

            // Map URL hash fragments to friendly labels (for settings tabs)
            this.hashLabels = {
                '#account-general': 'General',
                '#account-change-password': 'Change Password',
                '#account-info': 'Info',
                '#account-social-links': 'Social Links',
                '#account-connections': 'Connections',
                '#account-notifications': 'Notifications'
            };

            this.breadcrumbList = null;
            this.tabObserver = null;

            // Store the instance
            Breadcrumb.instance = this;
        }

        /**
         * Get the singleton instance
         * @returns {Breadcrumb} - The singleton instance
         */
        static getInstance() {
            if (!Breadcrumb.instance) {
                Breadcrumb.instance = new Breadcrumb();
            }
            return Breadcrumb.instance;
        }

        /**
         * Convert path segment to friendly label
         * @param {string} path - The path to convert
         * @returns {string} - Friendly label
         */
        getLabelFromPath(path) {
            // Check if we have a direct mapping
            if (this.pathLabels[path]) {
                return this.pathLabels[path];
            }
            
            // Convert path segment to title case
            // e.g., "my-folder" -> "My Folder"
            // Remove leading and trailing slashes, then process
            var cleanPath = path.replace(/^\/+|\/+$/g, ''); // Remove leading and trailing slashes
            if (!cleanPath) {
                return '';
            }
            
            return cleanPath
                .split('/')
                .map(function(segment) {
                    return segment
                        .split('-')
                        .map(function(word) {
                            return word.charAt(0).toUpperCase() + word.slice(1);
                        })
                        .join(' ');
                })
                .join(' > ');
        }

        /**
         * Get label for hash fragment
         * @param {string} hash - The hash fragment
         * @returns {string} - Friendly label
         */
        getHashLabel(hash) {
            if (this.hashLabels[hash]) {
                return this.hashLabels[hash];
            }
            // Convert hash to friendly label (remove # and convert to title case)
            return hash
                .replace(/^#/, '')
                .replace(/-/g, ' ')
                .replace(/\b\w/g, function(char) {
                    return char.toUpperCase();
                });
        }

        /**
         * Create and append Home breadcrumb item
         * @param {HTMLElement} breadcrumbList - The breadcrumb list element
         */
        createHomeItem(breadcrumbList) {
            var homeItem = breadcrumbList.querySelector('.breadcrumb-item:first-child');
            var homeItemClone = null;
            if (homeItem) {
                // Clone the Home item to preserve its original structure
                homeItemClone = homeItem.cloneNode(true);
                // Ensure the text is clean - remove any trailing slashes from text nodes
                var homeSpan = homeItemClone.querySelector('span');
                if (homeSpan && homeSpan.textContent) {
                    homeSpan.textContent = homeSpan.textContent.replace(/\/+$/, '').trim();
                }
            }
            
            breadcrumbList.innerHTML = '';
            if (homeItemClone) {
                breadcrumbList.appendChild(homeItemClone);
            } else {
                // Fallback: recreate Home item if original not found
                var newHomeItem = document.createElement('li');
                newHomeItem.className = 'breadcrumb-item';
                var newHomeLink = document.createElement('a');
                newHomeLink.className = 'breadcrumb-link';
                newHomeLink.href = '/';
                var newHomeIcon = document.createElement('i');
                newHomeIcon.className = 'fas fa-home';
                var newHomeSpan = document.createElement('span');
                newHomeSpan.textContent = 'Home';
                newHomeLink.appendChild(newHomeIcon);
                newHomeLink.appendChild(newHomeSpan);
                newHomeItem.appendChild(newHomeLink);
                breadcrumbList.appendChild(newHomeItem);
            }
        }

        /**
         * Create breadcrumb item for a path segment
         * @param {string} currentPath - The current path
         * @param {string} segment - The path segment
         * @param {boolean} isLast - Whether this is the last item
         * @param {string} hash - Optional hash fragment
         * @returns {HTMLElement} - The created list item
         */
        createPathItem(currentPath, segment, isLast, hash) {
            var listItem = document.createElement('li');
            listItem.className = 'breadcrumb-item';

            // Add separator (except for first item after Home)
            if (this.breadcrumbList.children.length > 0) {
                var separator = document.createElement('span');
                separator.className = 'breadcrumb-separator';
                separator.innerHTML = '<i class="fas fa-chevron-right"></i>';
                listItem.appendChild(separator);
            }

            // Get clean label (just the segment name, not the full path)
            var label = this.getLabelFromPath('/' + segment);

            if (isLast) {
                // Current page - not a link
                var current = document.createElement('span');
                current.className = 'breadcrumb-current';
                current.textContent = label;
                listItem.appendChild(current);
            } else {
                // Intermediate page - make it a link
                var link = document.createElement('a');
                link.className = 'breadcrumb-link';
                link.href = currentPath + (hash || '');
                link.textContent = label;
                listItem.appendChild(link);
            }

            return listItem;
        }

        /**
         * Create breadcrumb item for hash fragment
         * @param {string} hash - The hash fragment
         * @returns {HTMLElement} - The created list item
         */
        createHashItem(hash) {
            var hashItem = document.createElement('li');
            hashItem.className = 'breadcrumb-item';

            // Add separator
            var separator = document.createElement('span');
            separator.className = 'breadcrumb-separator';
            separator.innerHTML = '<i class="fas fa-chevron-right"></i>';
            hashItem.appendChild(separator);

            // Current hash - not a link
            var current = document.createElement('span');
            current.className = 'breadcrumb-current';
            current.textContent = this.getHashLabel(hash);
            hashItem.appendChild(current);

            return hashItem;
        }

        /**
         * Generate breadcrumb items from current URL
         */
        generateBreadcrumbs() {
            var path = window.location.pathname;
            var hash = window.location.hash;
            this.breadcrumbList = document.querySelector('.breadcrumb-list');
            
            if (!this.breadcrumbList) {
                return; // Breadcrumb element not found
            }

            // Create Home item
            this.createHomeItem(this.breadcrumbList);

            // If we're on home page, we're done
            if (path === '/' || path === '') {
                return;
            }

            // Split path into segments
            var segments = path.split('/').filter(function(segment) {
                return segment.length > 0;
            });

            // Build breadcrumb path
            var currentPath = '';
            segments.forEach((segment, index) => {
                currentPath += '/' + segment;
                var isLast = index === segments.length - 1 && !hash; // Last only if no hash
                
                var listItem = this.createPathItem(currentPath, segment, isLast, hash);
                this.breadcrumbList.appendChild(listItem);
            });

            // Add hash fragment as last breadcrumb item if present
            if (hash) {
                var hashItem = this.createHashItem(hash);
                this.breadcrumbList.appendChild(hashItem);
            }
        }

        /**
         * Get active tab hash from the page
         * @returns {string|null} - The active tab hash or null
         */
        getActiveTabHash() {
            // Check for active tab pane
            var activePane = document.querySelector('.tab-pane.active, .tab-pane.show');
            if (activePane && activePane.id) {
                return '#' + activePane.id;
            }
            
            // Check for active list item
            var activeLink = document.querySelector('.account-settings-links a.active');
            if (activeLink) {
                var href = activeLink.getAttribute('href');
                if (href && href.startsWith('#')) {
                    return href;
                }
            }
            
            return null;
        }

        /**
         * Setup MutationObserver for tab pane changes
         */
        setupTabObserver() {
            var tabPanes = document.querySelectorAll('.tab-pane');
            if (tabPanes.length > 0) {
                this.tabObserver = new MutationObserver((mutations) => {
                    mutations.forEach((mutation) => {
                        if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
                            var target = mutation.target;
                            if (target.classList.contains('active') || target.classList.contains('show')) {
                                var hash = '#' + target.id;
                                if (hash && hash !== '#') {
                                    // Update URL hash
                                    if (history.pushState) {
                                        history.pushState(null, null, window.location.pathname + hash);
                                    }
                                    this.generateBreadcrumbs();
                                }
                            }
                        }
                    });
                });

                tabPanes.forEach((pane) => {
                    this.tabObserver.observe(pane, {
                        attributes: true,
                        attributeFilter: ['class']
                    });
                });
            }
        }

        /**
         * Setup event listeners for settings tab links
         */
        setupSettingsTabListeners() {
            // Wait for DOM to be ready
            function setupListeners(breadcrumb) {
                // Listen for clicks on settings tab links
                var settingsLinks = document.querySelectorAll('.account-settings-links a[data-toggle="list"]');
                settingsLinks.forEach(function(link) {
                    link.addEventListener('click', function(e) {
                        var hash = this.getAttribute('href');
                        if (hash && hash.startsWith('#')) {
                            // Update URL hash immediately
                            if (history.pushState) {
                                history.pushState(null, null, window.location.pathname + hash);
                            } else {
                                window.location.hash = hash;
                            }
                            // Update breadcrumb after a delay to allow Bootstrap to update active classes
                            setTimeout(function() {
                                breadcrumb.generateBreadcrumbs();
                            }, 300);
                        }
                    });
                });

                // Setup MutationObserver for tab pane changes
                breadcrumb.setupTabObserver();

                // Also listen for hashchange events (back/forward browser buttons)
                window.addEventListener('hashchange', function() {
                    breadcrumb.generateBreadcrumbs();
                });

                // Check hash on page load and update if needed
                var currentHash = window.location.hash;
                var activeHash = breadcrumb.getActiveTabHash();
                if (currentHash && currentHash !== activeHash) {
                    // Hash in URL doesn't match active tab, update breadcrumb with URL hash
                    breadcrumb.generateBreadcrumbs();
                } else if (activeHash && !currentHash) {
                    // Active tab but no hash in URL, update URL
                    if (history.pushState) {
                        history.pushState(null, null, window.location.pathname + activeHash);
                    }
                    breadcrumb.generateBreadcrumbs();
                } else if (currentHash) {
                    breadcrumb.generateBreadcrumbs();
                }
            }

            // Wait for DOM to be ready
            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', () => setupListeners(this));
            } else {
                // If already loaded, wait a bit for Bootstrap to initialize
                setTimeout(() => setupListeners(this), 100);
            }
        }

        /**
         * Initialize breadcrumb navigation
         */
        init() {
            this.generateBreadcrumbs();
            this.setupSettingsTabListeners();
        }
    }

    // Initialize breadcrumb when DOM is ready
    function initBreadcrumbs() {
        var breadcrumb = Breadcrumb.getInstance();
        breadcrumb.init();
        
        // Make breadcrumb instance available globally if needed for debugging
        if (typeof window !== 'undefined') {
            window.breadcrumb = breadcrumb;
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initBreadcrumbs);
    } else {
        initBreadcrumbs();
    }
})();
