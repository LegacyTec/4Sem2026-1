import api from "@/config/axios";

export interface IPlanta {
  id: number;
  nome: string;
  latitude?: number | null;
  longitude?: number | null;
}

export async function buscarPlantasPorContrato(idContrato: number): Promise<IPlanta[]> {
  const response = await api.get<IPlanta[]>(`/contrato/${idContrato}/plantas`);
  return response.data;
}

export async function criarPlanta(
  nome: string,
  idContrato: number,
  latitude?: number,
  longitude?: number,
): Promise<IPlanta> {
  const response = await api.post<IPlanta>("/planta", {
    nome,
    idContrato,
    ...(latitude != null ? { latitude } : {}),
    ...(longitude != null ? { longitude } : {}),
  });
  return response.data;
}
