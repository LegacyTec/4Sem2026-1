import axios, { AxiosInstance, AxiosError } from "axios";

/**
 * Cria uma instância do Axios com configurações padrão
 * Em desenvolvimento: usa http://localhost:8080
 * Em produção: usa a URL configurada em VITE_API_URL
 */
const API_BASE_URL = import.meta.env.DEV
  ? "http://localhost:8080"
  : import.meta.env.VITE_API_URL || "http://localhost:8080";

console.log("🔧 Axios configurado com API_BASE_URL:", API_BASE_URL);

const axiosInstance: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: false,
});

/**
 * Interceptador de requisição
 */
axiosInstance.interceptors.request.use(
  (config) => {
    console.log(
      `📡 Requisição ${config.method?.toUpperCase()} para ${config.baseURL}${config.url}`,
    );
    return config;
  },
  (error) => {
    console.error("Erro na requisição:", error);
    return Promise.reject(error);
  },
);

/**
 * Interceptador de resposta para tratamento de erros
 */
axiosInstance.interceptors.response.use(
  (response) => {
    console.log(`✅ Resposta recebida:`, response.status, response.config.url);
    return response;
  },
  (error: AxiosError) => {
    // Tratamento centralizado de erros
    console.error("❌ Erro na resposta:", error.response?.status, error.message);

    if (error.response?.status === 401) {
      console.error("Não autorizado. Realize o login novamente.");
    } else if (error.response?.status === 403) {
      console.error("Acesso proibido.");
    } else if (error.response?.status === 404) {
      console.error("Recurso não encontrado.");
    } else if (error.response?.status === 500) {
      console.error("Erro no servidor. Tente novamente mais tarde.");
    }
    return Promise.reject(error);
  },
);

export default axiosInstance;
