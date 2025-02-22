import type { Config } from "tailwindcss";

export default {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        'black-purple-gradient': 'linear-gradient(90deg, rgba(33,33,33,1) 0%, rgba(101,10,176,1) 100%)',
        'black-green-gradient':'linear-gradient(90deg, rgba(33,33,33,1) 0%, rgba(17,158,31,1) 100%)'
      },
      colors: {
        background: "var(--background)",
        foreground: "var(--foreground)",
        "line-color":"#25F712",
        "nav-buttons":"#B4DBB8",
        "green-hover":"#119E1F",
        "buttons-hover":"#10A51F",
        "middle-title":"#1A7717"
      },
    },
  },
  plugins: [],
} satisfies Config;
