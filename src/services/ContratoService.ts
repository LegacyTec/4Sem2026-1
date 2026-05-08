import axiosInstance from "@/config/axios";
import { API_ENDPOINTS } from "@/config/api";

/**
 * Interface que representa um Contrato do backend
 * Alinhado com a estrutura Java
 */
export interface IContrato {
  id: number | string;
  nomeEmpresa: string;
  quantidadePlanta: number;
  dataInicio: string; // ISO format: YYYY-MM-DD
  dataFim: string; // ISO format: YYYY-MM-DD
  quantidadeAtivos: number;
  descricao: string;
  status: string;
  criador?: string;
  usuarios?: IUsuario[];
}

export interface IUsuario {
  id: number | string;
  [key: string]: unknown;
}

/**
 * Payload para criar um novo contrato
 */
export interface IContratoPayload {
  nomeEmpresa: string;
  quantidadePlanta: number;
  dataInicio: string;
  dataFim: string;
  descricao: string;
}

class ContratoService {
  /**
   * Busca todos os contratos
   * GET /contrato
   */
  async buscarTodos(): Promise<IContrato[]> {
    try {
      console.log(
        "📡 [ContratoService.buscarTodos] Chamando endpoint:",
        API_ENDPOINTS.contratos.list,
      );
      const response = await axiosInstance.get<IContrato[]>(API_ENDPOINTS.contratos.list);
      console.log("✅ [ContratoService.buscarTodos] Resposta recebida:", response.data);
      return response.data;
    } catch (error) {
      console.error("❌ [ContratoService.buscarTodos] Erro ao buscar contratos:", error);
      throw error;
    }
  }

  /**
   * Cria um novo contrato
   * POST /contrato
   */
  async criar(payload: IContratoPayload): Promise<IContrato> {
    try {
      const response = await axiosInstance.post<IContrato>(API_ENDPOINTS.contratos.create, payload);
      return response.data;
    } catch (error) {
      console.error("Erro ao criar contrato:", error);
      throw error;
    }
  }

  /**
   * Formato helper para converter data ISO para exibição
   */
  formatarData(isoDate: string | null | undefined): string {
    if (!isoDate) return "—";
    try {
      return new Intl.DateTimeFormat("pt-BR", { timeZone: "UTC" }).format(
        new Date(`${isoDate}T00:00:00`),
      );
    } catch {
      return "—";
    }
  }

  /**
   * Determina o status do contrato baseado na data de término
   */
  resolverStatus(dataFim: string): "Ativo" | "Expirando" | "Inativo" {
    if (!dataFim) return "Ativo";
    try {
      const hoje = new Date();
      const fim = new Date(`${dataFim}T00:00:00`);
      const dias = Math.ceil((fim.getTime() - hoje.getTime()) / (1000 * 60 * 60 * 24));
      if (dias < 0) return "Inativo";
      if (dias <= 60) return "Expirando";
      return "Ativo";
    } catch {
      return "Ativo";
    }
  }
}

export default new ContratoService();
