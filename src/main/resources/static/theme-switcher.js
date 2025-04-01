// Add logs inside js/theme-switcher.js

(() => {
    'use strict'
    console.log('Theme switcher script START'); // <--- ADD

    const getStoredTheme = () => localStorage.getItem('theme')
    const setStoredTheme = theme => localStorage.setItem('theme', theme)

    const getPreferredTheme = () => {
        console.log('getPreferredTheme: Checking localStorage...');
        const storedTheme = getStoredTheme();
        console.log('getPreferredTheme: Found in localStorage:', storedTheme); // <-- See what it reads
        if (storedTheme) {
            return storedTheme;
        }
    }
    const setTheme = theme => {
        console.log('Attempting to set theme:', theme); // <--- ADD
        if (theme === 'auto' && window.matchMedia('(prefers-color-scheme: dark)').matches) {
            document.documentElement.setAttribute('data-bs-theme', 'dark')
        } else {
            document.documentElement.setAttribute('data-bs-theme', theme)
        }
        console.log('data-bs-theme set to:', document.documentElement.getAttribute('data-bs-theme')); // <--- ADD
        updateSwitcherIcon(theme);
    }

    const updateSwitcherIcon = (theme) => {
        console.log('Updating icon for theme:', theme); // <--- ADD
        const switcherIcon = document.getElementById('theme-switcher-icon');
        if (switcherIcon) {
            console.log('Icon element found'); // <--- ADD
            if (theme === 'dark') {
                switcherIcon.classList.remove('bi-sun-fill');
                switcherIcon.classList.add('bi-moon-stars-fill');
            } else {
                switcherIcon.classList.remove('bi-moon-stars-fill');
                switcherIcon.classList.add('bi-sun-fill');
            }
        } else {
            console.error('Theme switcher icon element NOT FOUND'); // <--- ADD
        }
    }

    // Set theme on initial load
    const initialTheme = getPreferredTheme();
    console.log('Initial theme determined:', initialTheme); // <--- ADD
    setTheme(initialTheme);

    window.addEventListener('DOMContentLoaded', () => {
        console.log('DOMContentLoaded event fired'); // <--- ADD
        const themeSwitcher = document.getElementById('theme-switcher');
        if(themeSwitcher) {
            console.log('Theme switcher button FOUND'); // <--- ADD
            themeSwitcher.addEventListener('click', () => {
                console.log('Theme switcher CLICKED!'); // <--- ADD
                const currentTheme = document.documentElement.getAttribute('data-bs-theme');
                const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
                console.log('Switching theme from', currentTheme, 'to', newTheme); // <--- ADD
                setStoredTheme(newTheme);
                setTheme(newTheme);
            });
        } else {
            console.error('Theme switcher button element NOT FOUND'); // <--- ADD
        }

        // Ensure icon is correct on load (in case script runs after DOMContentLoaded)
        // updateSwitcherIcon(getPreferredTheme()); // Redundant now as setTheme calls it
    })

    console.log('Theme switcher script END'); // <--- ADD
})()