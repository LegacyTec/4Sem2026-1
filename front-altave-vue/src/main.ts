import "./assets/main.css";
import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";

console.log("🚀 [main.ts] Vue iniciando...");

const app = createApp(App);
app.use(createPinia());
app.use(router);
app.mount("#app");

console.log("✅ [main.ts] App montado");
