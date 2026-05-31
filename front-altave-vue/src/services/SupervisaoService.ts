import api from "@/config/axios";
import type { IPlanta } from "./PlantaService";
import type { IUsuario } from "./OrdemService";

export interface ISupervisao {
  id: number;
  nome: string;
  descricao?: string;
  supervisor?: IUsuario;
  plantas?: IPlanta[];
}

export interface ISupervisaoPayload {
  nome: string;
  descricao?: string;
  idContrato: number;
  idSupervisor: number;
  idPlantas: number[];
}

export async function buscarSupervisoesPorContrato(idContrato: number): Promise<ISupervisao[]> {
  const response = await api.get<ISupervisao[]>(`/contrato/${idContrato}/supervisoes`);
  return response.data;
}

export async function criarSupervisao(payload: ISupervisaoPayload): Promise<ISupervisao> {
  const response = await api.post<ISupervisao>("/supervisao", payload);
  return response.data;
}

export async function atualizarSupervisorSupervisao(
  idSupervisao: number,
  idSupervisor: number,
): Promise<ISupervisao> {
  const response = await api.patch<ISupervisao>(`/supervisao/${idSupervisao}/supervisor`, {
    idSupervisor,
  });
  return response.data;
}
