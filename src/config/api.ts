/**
 * Configuração centralizada da API do backend
 * A URL base é gerenciada em axios.ts baseada no ambiente
 */

// Endpoints da API
export const API_ENDPOINTS = {
  contratos: {
    list: "/contrato",
    create: "/contrato",
    // Adicione mais endpoints conforme necessário
  },
  // Adicione outros recursos aqui
};
