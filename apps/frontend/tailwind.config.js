/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          light: '#6F9BFF',
          DEFAULT: '#3B82F6',
          dark: '#2C68C7',
        },
        secondary: {
          light: '#FDE68A',
          DEFAULT: '#FBBF24',
          dark: '#CA8A04',
        },
        accent: {
          light: '#F9A8D4',
          DEFAULT: '#EC4899',
          dark: '#C02669',
        },
        neutral: {
          light: '#F3F4F6',
          DEFAULT: '#D1D5DB',
          dark: '#374151',
        },
      },
    },
  },
  plugins: [],
}

