/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        admin: {
          bg: '#020617',
          card: '#0F172A',
          accent: '#3B82F6',
          accentLight: '#60A5FA',
          border: '#1E293B',
          text: '#F8FAFC',
          subtext: '#94A3B8',
        }
      },
      fontFamily: {
        outfit: ['Outfit', 'sans-serif'],
        inter: ['Inter', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
