import api from "@/config/axios";

export interface IContrato {
  id: number;
  nomeEmpresa: string;
  quantidadePlanta: number;
  dataInicio: string;
  dataFim: string;
  quantidadeAtivos: number;
  descricao: string;
  status: string;
  criador?: string;
}

export interface IContratoPayload {
  nomeEmpresa: string;
  quantidadePlanta: number;
  dataInicio: string;
  dataFim: string;
  descricao: string;
}

export async function buscarContratos(): Promise<IContrato[]> {
  const response = await api.get<IContrato[]>("/contrato");
  return response.data;
}

export async function criarContrato(payload: IContratoPayload): Promise<IContrato> {
  const response = await api.post<IContrato>("/contrato", payload);
  return response.data;
}

export function formatarData(iso: string | null | undefined): string {
  if (!iso) return "—";
  try {
    return new Intl.DateTimeFormat("pt-BR", { timeZone: "UTC" }).format(
      new Date(`${iso}T00:00:00`),
    );
  } catch {
    return "—";
  }
}

export function resolverStatus(dataFim: string): "Ativo" | "Expirando" | "Inativo" {
  if (!dataFim) return "Ativo";
  const hoje = new Date();
  const fim = new Date(`${dataFim}T00:00:00`);
  const dias = Math.ceil((fim.getTime() - hoje.getTime()) / (1000 * 60 * 60 * 24));
  if (dias < 0) return "Inativo";
  if (dias <= 60) return "Expirando";
  return "Ativo";
}
