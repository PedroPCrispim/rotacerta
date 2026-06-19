/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          black: "#111111",
          blue: "#1E40AF", // Blue 800
          yellow: "#FACC15", // Yellow 400
          dark: "#1F2937", // Gray 800
        }
      }
    },
  },
  plugins: [],
}
