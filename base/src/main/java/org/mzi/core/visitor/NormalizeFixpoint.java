package org.mzi.core.visitor;

import org.jetbrains.annotations.NotNull;
import org.mzi.api.util.NormalizeMode;
import org.mzi.core.term.*;
import org.mzi.ref.EvalRef;

public final class NormalizeFixpoint implements UnfoldFixpoint<NormalizeMode> {
  public static final @NotNull NormalizeFixpoint INSTANCE = new NormalizeFixpoint();

  private NormalizeFixpoint() {
  }

  @Override
  public @NotNull Term visitApp(AppTerm.@NotNull Apply term, NormalizeMode mode) {
    var fn = term.fn();
    if (fn instanceof LamTerm lam) return AppTerm.make(lam, visitArg(term.arg(), mode));
    else return AppTerm.make(fn, mode == NormalizeMode.WHNF ? term.arg() : visitArg(term.arg(), mode));
  }

  @Override
  public @NotNull Term visitRef(@NotNull RefTerm term, NormalizeMode mode) {
    if (!(term.ref() instanceof EvalRef eval)) return term;
    return eval.term().accept(this, mode);
  }

  @Override
  public @NotNull Term visitLam(@NotNull LamTerm term, NormalizeMode mode) {
    if (mode != NormalizeMode.NF) return term;
    else return UnfoldFixpoint.super.visitLam(term, mode);
  }

  @Override
  public @NotNull Term visitDT(@NotNull DT term, NormalizeMode mode) {
    if (mode != NormalizeMode.NF) return term;
    else return UnfoldFixpoint.super.visitDT(term, mode);
  }

  @Override
  public @NotNull Term visitTup(@NotNull TupTerm term, NormalizeMode mode) {
    if (mode != NormalizeMode.NF) return term;
    else return UnfoldFixpoint.super.visitTup(term, mode);
  }

  @Override
  public @NotNull Term visitProj(@NotNull ProjTerm term, NormalizeMode mode) {
    var tup = term.tup();
    var ix = term.ix();
    // should not happen
    if (!(tup instanceof TupTerm t)) throw new IllegalStateException("`TupTerm` expected, got: `" + tup.getClass() + "`");
    assert ix <= ((TupTerm) tup).items().size();
    assert ix > 0;
    return ((TupTerm) tup).items().get(ix).accept(this, mode);
  }
}
