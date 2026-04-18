/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: "class",
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/**/*.{js,css}",
    "./node_modules/flowbite/**/*.js"
  ],

  // 🔥 THIS IS MANDATORY
  safelist: [
    'bg-blue-400', 'text-blue-800', 'border-blue-500',
    'bg-green-400', 'text-green-800', 'border-green-500',
    'bg-red-400', 'text-red-800', 'border-red-500',
    'bg-yellow-400', 'text-yellow-800', 'border-yellow-500'

      // ✅ ADD THESE
  ,
  'border-t-red-500',
  'border-t-blue-400'
  ],

  theme: {
    extend: {},
  },

  plugins: [
    require("flowbite/plugin")
  ],
};
