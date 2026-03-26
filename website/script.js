/**
 * Socius Website - Interactive JavaScript
 * Handles smooth scrolling, animations, and interactivity
 */

document.addEventListener('DOMContentLoaded', () => {
    initializeNavigation();
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
 * Update navbar on scroll
 */
window.addEventListener('scroll', () => {
    const navbar = document.querySelector('.navbar');
    const scrolled = window.scrollY > 50;
    
    if (scrolled) {
        navbar.style.boxShadow = 'var(--elevation-2)';
    } else {
        navbar.style.boxShadow = 'var(--elevation-1)';
    }
});

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
        
        // Add ripple CSS if not already present
        if (!document.querySelector('style[data-ripple]')) {
            const style = document.createElement('style');
            style.setAttribute('data-ripple', 'true');
            style.textContent = `
                .btn {
                    position: relative;
                    overflow: hidden;
                }
                
                .btn .ripple {
                    position: absolute;
                    border-radius: 50%;
                    background: rgba(255, 255, 255, 0.6);
                    transform: scale(0);
                    animation: ripple-animation 0.6s ease-out;
                    pointer-events: none;
                }
                
                @keyframes ripple-animation {
                    to {
                        transform: scale(4);
                        opacity: 0;
                    }
                }
            `;
            document.head.appendChild(style);
        }
        
        this.appendChild(ripple);
    });
});

/**
 * Detect dark mode preference and adjust theme if needed
 */
function checkDarkMode() {
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)');
    
    if (prefersDark.matches) {
        // You could add dark mode CSS variables here
        document.documentElement.style.setProperty('--background', '#1F1A20');
        document.documentElement.style.setProperty('--surface', '#211E24');
        document.documentElement.style.setProperty('--on-surface', '#FFFBFE');
        document.documentElement.style.setProperty('--on-surface-variant', '#CAC4CF');
    }
}

// Check dark mode on load and when it changes
checkDarkMode();
window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', checkDarkMode);
