import type { IAtivo } from "@/services/AtivoService";
import type { IOrdem } from "@/services/OrdemService";

export function normalizarStatusAtivo(status?: string): string {
  return (status ?? "").toUpperCase().replace(/\s+/g, "_");
}

export function isOperacional(status?: string): boolean {
  const s = normalizarStatusAtivo(status);
  return s === "OPERACIONAL" || s === "ATIVO";
}

export function isEmManutencao(status?: string): boolean {
  const s = normalizarStatusAtivo(status);
  return s === "EM_MANUTENCAO" || s === "MANUTENCAO";
}

export function isInativo(status?: string): boolean {
  const s = normalizarStatusAtivo(status);
  return s === "INATIVO" || s === "REMOVIDO";
}

export function labelStatusAtivo(status?: string): string {
  if (isOperacional(status)) return "Operacional";
  if (isEmManutencao(status)) return "Em manutenção";
  if (status?.toUpperCase() === "REMOVIDO") return "Removido";
  if (isInativo(status)) return "Inativo";
  return status ?? "—";
}

export function calcularMttrAtivo(ativoId: number, ordens: IOrdem[]): number | null {
  const concluidas = ordens.filter(
    (o) =>
      o.ativo?.id === ativoId &&
      o.status === "CONCLUIDA" &&
      o.dataInicio &&
      o.dataFim,
  );
  if (concluidas.length === 0) return null;

  const totalHoras = concluidas.reduce((acc, o) => {
    const inicio = new Date(`${o.dataInicio}T00:00:00`).getTime();
    const fim = new Date(`${o.dataFim!}T00:00:00`).getTime();
    return acc + Math.max(0, (fim - inicio) / 3_600_000);
  }, 0);

  return totalHoras / concluidas.length;
}

export function calcularMtbfAtivo(
  ativoId: number,
  ordens: IOrdem[],
  periodicidadeManutencao?: number,
): number | null {
  const corretivas = ordens
    .filter((o) => o.ativo?.id === ativoId && o.tipoManutencao === "CORRETIVA" && o.dataInicio)
    .sort(
      (a, b) =>
        new Date(`${a.dataInicio}T00:00:00`).getTime() -
        new Date(`${b.dataInicio}T00:00:00`).getTime(),
    );

  if (corretivas.length >= 2) {
    let totalDias = 0;
    for (let i = 1; i < corretivas.length; i++) {
      const prev = new Date(`${corretivas[i - 1]!.dataInicio}T00:00:00`).getTime();
      const curr = new Date(`${corretivas[i]!.dataInicio}T00:00:00`).getTime();
      totalDias += Math.max(0, (curr - prev) / 86_400_000);
    }
    return totalDias / (corretivas.length - 1);
  }

  if (periodicidadeManutencao && periodicidadeManutencao > 0) {
    return periodicidadeManutencao;
  }

  return null;
}

export function calcularMttrMedio(ativos: IAtivo[], ordens: IOrdem[]): number | null {
  if (ativos.length === 0) return null;

  const valores = ativos.map((a) => {
    const mttr = calcularMttrAtivo(a.id, ordens);
    if (mttr !== null) return mttr;
    if (a.periodicidadeManutencao && a.periodicidadeManutencao > 0) {
      return a.periodicidadeManutencao * 0.25;
    }
    return null;
  });

  const validos = valores.filter((v): v is number => v !== null);
  if (validos.length === 0) return null;

  return validos.reduce((acc, v) => acc + v, 0) / ativos.length;
}

export function calcularMtbfMedio(ativos: IAtivo[], ordens: IOrdem[]): number | null {
  if (ativos.length === 0) return null;

  const valores = ativos.map(
    (a) => calcularMtbfAtivo(a.id, ordens, a.periodicidadeManutencao) ?? null,
  );

  const validos = valores.filter((v): v is number => v !== null);
  if (validos.length === 0) return null;

  return validos.reduce((acc, v) => acc + v, 0) / ativos.length;
}

export function formatarMttr(horas: number | null): string {
  if (horas === null) return "—";
  return `${horas.toLocaleString("pt-BR", { maximumFractionDigits: 1 })} h`;
}

export function formatarMtbf(dias: number | null): string {
  if (dias === null) return "—";
  return `${Math.round(dias).toLocaleString("pt-BR")} dias`;
}
