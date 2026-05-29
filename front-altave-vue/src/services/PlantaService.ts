import api from "@/config/axios";

export interface IPlanta {
  id: number;
  nome: string;
}

export async function buscarPlantasPorContrato(idContrato: number): Promise<IPlanta[]> {
  const response = await api.get<IPlanta[]>(`/contrato/${idContrato}/plantas`);
  return response.data;
}

export async function criarPlanta(nome: string, idContrato: number): Promise<IPlanta> {
  const response = await api.post<IPlanta>("/planta", { nome, idContrato });
  return response.data;
}
