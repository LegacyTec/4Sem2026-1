import api from "@/config/axios";

export interface IUsuario {
  id: number;
  nomeCompleto: string;
  email?: string;
  cargo?: string;
  funcao?: string;
}

export interface IOrdem {
  id: number;
  nome: string;
  descricao?: string;
  dataInicio: string;
  dataFim?: string;
  tipoManutencao: "PREVENTIVA" | "CORRETIVA" | "INSTALACAO" | "REMOCAO";
  status: "PENDENTE" | "EM_ANDAMENTO" | "CONCLUIDA" | "CANCELADA";
  comentario?: string;
  planta?: string;
  predio?: string;
  requisito?: string;
  ativo?: { id: number; tipo?: string; planta?: string; predio?: string };
  usuarios?: IUsuario[];
}

export interface IOrdemPayload {
  nome: string;
  descricao?: string;
  dataInicio: string;
  dataFim?: string;
  tipoManutencao: "PREVENTIVA" | "CORRETIVA" | "INSTALACAO" | "REMOCAO";
  status: "PENDENTE";
  planta?: string;
  predio?: string;
  requisito?: string;
  ativo: { id: number };
  usuarios?: { id: number }[];
}

export async function buscarOrdens(): Promise<IOrdem[]> {
  const response = await api.get<IOrdem[]>("/ordem");
  return response.data;
}

export async function criarOrdem(payload: IOrdemPayload): Promise<IOrdem> {
  const response = await api.post<IOrdem>("/ordem", payload);
  return response.data;
}

export async function atualizarStatus(id: number, status: IOrdem["status"]): Promise<IOrdem> {
  const response = await api.patch<IOrdem>(`/ordem/${id}/status`, { status });
  return response.data;
}

export async function buscarUsuarios(): Promise<IUsuario[]> {
  const response = await api.get<IUsuario[]>("/usuario");
  return response.data;
}

export function labelTipo(tipo: string): string {
  const map: Record<string, string> = {
    PREVENTIVA: "Preventiva",
    CORRETIVA: "Corretiva",
    INSTALACAO: "Instalação",
    REMOCAO: "Remoção",
  };
  return map[tipo] ?? tipo;
}

export function labelStatus(status: string): string {
  const map: Record<string, string> = {
    PENDENTE: "Pendente",
    EM_ANDAMENTO: "Em andamento",
    CONCLUIDA: "Concluída",
    CANCELADA: "Cancelada",
  };
  return map[status] ?? status;
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

export function iniciaisNome(nome: string): string {
  return nome
    .split(" ")
    .filter(Boolean)
    .slice(0, 2)
    .map((p) => p[0].toUpperCase())
    .join("");
}

const AVATAR_COLORS = [
  "#3b82f6",
  "#8b5cf6",
  "#ec4899",
  "#f59e0b",
  "#10b981",
  "#ef4444",
  "#06b6d4",
  "#f97316",
];
export function avatarColor(id: number): string {
  return AVATAR_COLORS[id % AVATAR_COLORS.length];
}
