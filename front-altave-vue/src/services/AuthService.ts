import api from "@/config/axios";
import type { IUsuario } from "./OrdemService";

const AUTH_KEY = "sgm_auth_user";

export type Cargo = "ADM" | "Supervisor" | "Planejador" | "Técnico";

export async function login(email: string, senha: string): Promise<IUsuario> {
  const response = await api.post<IUsuario>("/auth/login", { email, senha });
  localStorage.setItem(AUTH_KEY, JSON.stringify(response.data));
  return response.data;
}

export function getCurrentUser(): IUsuario | null {
  const raw = localStorage.getItem(AUTH_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw) as IUsuario;
  } catch {
    return null;
  }
}

export function logout(): void {
  localStorage.removeItem(AUTH_KEY);
}

export function isAuthenticated(): boolean {
  return getCurrentUser() !== null;
}

/** Rota inicial de cada cargo. */
export function roleHome(cargo?: string): string {
  switch (cargo) {
    case "ADM":
      return "/adm/index";
    case "Supervisor":
      return "/supervisor/inicio";
    case "Planejador":
      return "/planejador/inicio";
    case "Técnico":
      return "/tecnico/inicio";
    default:
      return "/login";
  }
}

/** Prefixo de rota que cada cargo tem permissão de acessar. */
export function rolePrefix(cargo?: string): string | null {
  switch (cargo) {
    case "ADM":
      return "/adm";
    case "Supervisor":
      return "/supervisor";
    case "Planejador":
      return "/planejador";
    case "Técnico":
      return "/tecnico";
    default:
      return null;
  }
}
