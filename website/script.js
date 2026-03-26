/**
 * Socius Website - Interactive JavaScript
 * Handles smooth scrolling, animations, and interactivity
 */

// Initialize theme on page load
document.addEventListener('DOMContentLoaded', () => {
    initializeTheme();
    initializeNavigation();
    initializeNavbarScroll();
    initializeExternalLinks();
    observeElements();
    setupIntersectionObserver();
});

/**
 * Initialize navigation functionality
 */
function initializeNavigation() {
    const navLinks = document.querySelectorAll('.nav-links a');
    
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            // Allow default link behavior for hash navigation
            // Smooth scroll is handled by CSS scroll-behavior
        });
    });
}

/**
 * Initialize external links with security attributes
 */
function initializeExternalLinks() {
    const externalButtons = document.querySelectorAll('[data-external]');
    
    externalButtons.forEach(button => {
        button.addEventListener('click', () => {
            const url = button.getAttribute('data-external');
            window.open(url, '_blank', 'noopener,noreferrer');
        });
    });
}

/**
 * Scroll to a specific section
 */
function scrollToSection(sectionId) {
    const element = document.getElementById(sectionId);
    if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
}

/**
 * Setup Intersection Observer for fade-in animations
 */
function setupIntersectionObserver() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                // Only observe once
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);

    // Observe feature cards
    document.querySelectorAll('.feature-card').forEach(card => {
        observer.observe(card);
    });

    // Observe download cards
    document.querySelectorAll('.download-card').forEach(card => {
        observer.observe(card);
    });
}

/**
 * Observe and add styles to elements as they come into view
 */
function observeElements() {
    const elementsToObserve = document.querySelectorAll(
        '.feature-card, .download-card, .design-content section'
    );

    elementsToObserve.forEach((element, index) => {
        element.style.opacity = '0';
        element.style.transform = 'translateY(20px)';
        element.style.transition = `opacity 0.6s ease-out ${index * 0.1}s, transform 0.6s ease-out ${index * 0.1}s`;
        
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                }
            });
        }, { threshold: 0.1 });

        observer.observe(element);
    });
}

/**
 * Handle smooth scrolling for navbar links
 */
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        const href = this.getAttribute('href');
        
        // Only prevent default for section links
        if (href !== '#' && document.querySelector(href)) {
            e.preventDefault();
            const element = document.querySelector(href);
            
            if (element) {
                element.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
                
                // Update navbar active state
                document.querySelectorAll('.nav-links a').forEach(link => {
                    link.style.color = '';
                });
                this.style.color = 'var(--primary)';
            }
        }
    });
});

/**
 * Initialize navbar scroll effects
 */
function initializeNavbarScroll() {
    window.addEventListener('scroll', updateNavbarStyle);
    updateNavbarStyle(); // Call once on load
}

/**
 * Update navbar styling based on scroll position
 */
function updateNavbarStyle() {
    const navbar = document.querySelector('.navbar');
    const scrolled = window.scrollY > 10;
    
    if (scrolled) {
        navbar.classList.add('scrolled');
    } else {
        navbar.classList.remove('scrolled');
    }
}

/**
 * Add ripple effect to buttons
 */
document.querySelectorAll('.btn').forEach(button => {
    button.addEventListener('click', function(e) {
        const ripple = document.createElement('span');
        const rect = this.getBoundingClientRect();
        const size = Math.max(rect.width, rect.height);
        const x = e.clientX - rect.left - size / 2;
        const y = e.clientY - rect.top - size / 2;
        
        ripple.style.width = ripple.style.height = size + 'px';
        ripple.style.left = x + 'px';
        ripple.style.top = y + 'px';
        ripple.classList.add('ripple');
        
        this.appendChild(ripple);
        
        // Remove ripple element after animation completes
        setTimeout(() => ripple.remove(), 600);
    });
});

/**
 * Initialize theme on page load
 * Respects system preference and localStorage
 */
function initializeTheme() {
    const savedTheme = localStorage.getItem('theme');
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    
    let theme = 'light';
    
    // Priority: saved preference > system preference
    if (savedTheme) {
        theme = savedTheme;
    } else if (prefersDark) {
        theme = 'dark';
    }
    
    applyTheme(theme);
    setupThemeListener();
}

/**
 * Apply theme to the document
 */
function applyTheme(theme) {
    if (theme === 'dark') {
        document.documentElement.setAttribute('data-theme', 'dark');
        localStorage.setItem('theme', 'dark');
    } else {
        document.documentElement.removeAttribute('data-theme');
        localStorage.setItem('theme', 'light');
    }
}

/**
 * Setup listener for system theme changes
 */
function setupThemeListener() {
    const darkModeQuery = window.matchMedia('(prefers-color-scheme: dark)');
    
    darkModeQuery.addEventListener('change', (e) => {
        // Only update if user hasn't explicitly set a preference
        const savedTheme = localStorage.getItem('theme');
        if (!savedTheme) {
            applyTheme(e.matches ? 'dark' : 'light');
        }
    });
}

/**
 * Toggle theme between light and dark
 * Overrides system preference
 */
function toggleTheme() {
    const currentTheme = document.documentElement.getAttribute('data-theme') || 'light';
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    applyTheme(newTheme);
}
