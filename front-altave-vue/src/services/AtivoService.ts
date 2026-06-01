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
  supervisao?: { id: number; nome: string } | null;
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
  idSupervisao?: number;
  // contrato vem pelo id na URL — não deve ser enviado no body (campo @JsonIgnore na entidade)
}

export async function buscarAtivosPorContrato(idContrato: number): Promise<IAtivo[]> {
  const response = await api.get<IAtivo[]>(`/contrato/${idContrato}/ativos`);
  return response.data;
}

export async function criarAtivo(idContrato: number, payload: IAtivoPayload): Promise<IAtivo> {
  const response = await api.post<IAtivo>(`/contrato/${idContrato}/ativos`, payload);
  return response.data;
}
