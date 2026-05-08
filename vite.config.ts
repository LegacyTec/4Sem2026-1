import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueDevTools from "vite-plugin-vue-devtools";

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    proxy: {
      "/contrato": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
      "/usuario": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
      "/ativo": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
      "/ordem-manutencao": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
});
