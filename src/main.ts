import "./assets/main.css";
import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";

console.log("🚀 [main.ts] Iniciando aplicação Vue...");

const app = createApp(App);
console.log("✅ [main.ts] App criado");

app.use(createPinia());
console.log("✅ [main.ts] Pinia configurado");

app.use(router);
console.log("✅ [main.ts] Router configurado");

app.mount("#app");
console.log("✅ [main.ts] App montado no #app");
