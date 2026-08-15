/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        canvas: "#F5F7FA",
        surface: "#FFFFFF",
        ink: {
          50: "#F3F5F8",
          100: "#E2E7F0",
          300: "#AEB8CC",
          500: "#51607A",
          700: "#2A3450",
          900: "#131B2E",
          950: "#0B1220",
        },
        brand: {
          50: "#EFFBF9",
          100: "#D7F3EE",
          200: "#AEE6DC",
          400: "#2FA599",
          500: "#0E7C74",
          600: "#0B655F",
          700: "#094F4B",
          900: "#062F2D",
        },
        success: {
          100: "#DEF3E6",
          500: "#1B8A5A",
          600: "#146B46",
        },
        warning: {
          100: "#FBEAD1",
          500: "#B5740B",
          600: "#8F5B08",
        },
        danger: {
          100: "#FBE1DE",
          500: "#C4321D",
          600: "#9E2817",
        },
      },
      fontFamily: {
        display: ["'Space Grotesk'", "sans-serif"],
        sans: ["'Inter'", "sans-serif"],
        mono: ["'JetBrains Mono'", "monospace"],
      },
      boxShadow: {
        card: "0 1px 2px 0 rgba(11, 18, 32, 0.04), 0 1px 6px -1px rgba(11, 18, 32, 0.06)",
        popover: "0 8px 24px -4px rgba(11, 18, 32, 0.14)",
      },
      borderRadius: {
        xl: "0.875rem",
        "2xl": "1.25rem",
      },
    },
  },
  plugins: [],
};
