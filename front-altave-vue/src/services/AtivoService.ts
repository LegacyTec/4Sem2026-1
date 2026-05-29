import api from "@/config/axios";

export interface IAtivo {
  id: number;
  nome: string;
  tipo: string;
  status: string;
  fabricante?: string;
  periodicidadeManutencao?: number;
  descricao?: string;
  dataInstalacao?: string;
  predio?: string;
  planta?: string;
}

export interface IAtivoPayload {
  nome: string;
  tipo: string;
  status: string;
  fabricante?: string;
  periodicidadeManutencao: number;
  descricao?: string;
  dataInstalacao: string;
  predio?: string;
  planta?: string;
  contrato: { id: number };
}

export async function buscarAtivosPorContrato(idContrato: number): Promise<IAtivo[]> {
  const response = await api.get<IAtivo[]>(`/contrato/${idContrato}/ativos`);
  return response.data;
}

export async function criarAtivo(payload: IAtivoPayload): Promise<IAtivo> {
  const response = await api.post<IAtivo>("/ativo", payload);
  return response.data;
}
