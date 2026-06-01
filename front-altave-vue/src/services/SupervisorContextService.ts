import { buscarAtivosPorContrato } from "@/services/AtivoService";
import type { IAtivo } from "@/services/AtivoService";
import { getCurrentUser } from "@/services/AuthService";
import { buscarContratos, resolverStatus } from "@/services/ContratoService";
import type { IContrato } from "@/services/ContratoService";
import { buscarOrdens } from "@/services/OrdemService";
import type { IOrdem } from "@/services/OrdemService";
import { buscarSupervisoesPorContrato } from "@/services/SupervisaoService";

export interface IMembroContrato {
  id: number;
  nomeCompleto: string;
  cargo?: string;
  funcao?: string;
  email?: string;
}

export interface ISupervisorContext {
  contrato: IContrato | null;
  ativos: IAtivo[];
  ordens: IOrdem[];
  ordensContrato: IOrdem[];
  ordensAbertas: IOrdem[];
  membros: IMembroContrato[];
}

export async function resolverContratoDoSupervisor(
  contratos: IContrato[],
  userId: number,
): Promise<IContrato | null> {
  const vinculado = contratos.find((c) => c.usuarios?.some((u) => u.id === userId));
  if (vinculado) return vinculado;

  for (const c of contratos) {
    try {
      const supervisoes = await buscarSupervisoesPorContrato(c.id);
      if (supervisoes.some((s) => s.supervisor?.id === userId)) return c;
    } catch {
      /* contrato sem supervisões */
    }
  }

  return (
    contratos.find((c) => resolverStatus(c.dataFim) === "Ativo" && c.status !== "INATIVO") ??
    contratos.find((c) => resolverStatus(c.dataFim) === "Ativo") ??
    contratos[0] ??
    null
  );
}

export function filtrarOrdensDoContrato(ordens: IOrdem[], ativos: IAtivo[]): IOrdem[] {
  const ids = new Set(ativos.map((a) => a.id));
  return ordens.filter((o) => o.ativo?.id != null && ids.has(o.ativo.id));
}

export function filtrarOrdensAbertas(ordens: IOrdem[]): IOrdem[] {
  return ordens.filter((o) => o.status === "PENDENTE" || o.status === "EM_ANDAMENTO");
}

export async function carregarContextoSupervisor(): Promise<ISupervisorContext> {
  const user = getCurrentUser();
  const [contratos, ordens] = await Promise.all([buscarContratos(), buscarOrdens()]);

  const contrato = user
    ? await resolverContratoDoSupervisor(contratos, user.id)
    : (contratos[0] ?? null);

  const ativos = contrato ? await buscarAtivosPorContrato(contrato.id) : [];
  const ordensContrato = filtrarOrdensDoContrato(ordens, ativos);
  const ordensAbertas = filtrarOrdensAbertas(ordensContrato);

  const membros: IMembroContrato[] = (contrato?.usuarios ?? []).map((u) => ({
    id: u.id,
    nomeCompleto: u.nomeCompleto,
    cargo: u.cargo,
    funcao: (u as { funcao?: string }).funcao,
    email: (u as { email?: string }).email,
  }));

  return { contrato, ativos, ordens, ordensContrato, ordensAbertas, membros };
}
