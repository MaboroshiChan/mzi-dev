// Copyright (c) 2020-2020 Yinsen (Tesla) Zhang.
// Use of this source code is governed by the Apache-2.0 license that can be found in the LICENSE file.
package org.mzi.concrete;

import asia.kala.Tuple2;
import asia.kala.collection.mutable.Buffer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mzi.api.error.SourcePos;
import org.mzi.api.util.Assoc;
import org.mzi.generic.Modifier;
import org.mzi.api.ref.DefVar;

import java.util.EnumSet;
import java.util.Objects;

/**
 * concrete definition, corresponding to {@link org.mzi.core.def.Def}.
 *
 * @author re-xyr
 */
public sealed interface Decl extends Stmt {
  @Contract(pure = true) @NotNull DefVar<? extends Decl> ref();

  record DataCtor(
    @NotNull String name,
    @NotNull Buffer<Param> telescope,
    @NotNull Buffer<String> elim,
    @NotNull Buffer<Clause> clauses,
    boolean coerce
  ) {
  }

  sealed interface DataBody {
    record Ctors(
      @NotNull Buffer<DataCtor> ctors
    ) implements DataBody {}

    record Clauses(
      @NotNull Buffer<String> elim,
      @NotNull Buffer<Tuple2<Pattern, DataCtor>> clauses
    ) implements DataBody {}
  }

  /**
   * concrete data definition
   *
   * @author kiva
   */
  final class DataDecl implements Decl {
    public final @NotNull SourcePos sourcePos;
    public final @NotNull DefVar<DataDecl> ref;
    public final @NotNull Buffer<Param> telescope;
    public final @NotNull Expr result;
    public final @NotNull DataBody body;
    public final @NotNull Buffer<Stmt> abuseBlock;

    public DataDecl(
      @NotNull SourcePos sourcePos,
      @NotNull String name,
      @NotNull Buffer<Param> telescope,
      @NotNull Expr result,
      @NotNull DataBody body,
      @NotNull Buffer<Stmt> abuseBlock
    ) {
      this.sourcePos = sourcePos;
      this.telescope = telescope;
      this.result = result;
      this.body = body;
      this.abuseBlock = abuseBlock;
      this.ref = new DefVar<>(this, name);
    }

    @Contract(pure = true) private DataDecl(
      @NotNull SourcePos sourcePos,
      @NotNull DefVar<DataDecl> ref,
      @NotNull Buffer<Param> telescope,
      @NotNull Expr result,
      @NotNull DataBody body,
      @NotNull Buffer<Stmt> abuseBlock
    ) {
      this.sourcePos = sourcePos;
      this.ref = ref;
      this.telescope = telescope;
      this.result = result;
      this.body = body;
      this.abuseBlock = abuseBlock;
    }

    @Contract(value = "_, _, _, _, _, _ -> new", pure = true) public @NotNull DataDecl copy(
      @NotNull SourcePos sourcePos,
      @NotNull DefVar<DataDecl> ref,
      @NotNull Buffer<Param> telescope,
      @NotNull Expr result,
      @NotNull DataBody body,
      @NotNull Buffer<Stmt> abuseBlock
    ) {
      return new DataDecl(sourcePos, ref, telescope, result, body, abuseBlock);
    }

    @Override
    public @NotNull DefVar<? extends Decl> ref() {
      return this.ref;
    }

    @Override
    public @NotNull SourcePos sourcePos() {
      return this.sourcePos;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      DataDecl dataDecl = (DataDecl) o;
      return sourcePos.equals(dataDecl.sourcePos) &&
        telescope.equals(dataDecl.telescope) &&
        result.equals(dataDecl.result) &&
        body.equals(dataDecl.body) &&
        abuseBlock.equals(dataDecl.abuseBlock);
    }

    @Override
    public int hashCode() {
      return Objects.hash(sourcePos, telescope, result, body, abuseBlock);
    }

    @Override public String toString() {
      return "DataDecl{" +
        "sourcePos=" + sourcePos +
        ", telescope=" + telescope +
        ", result=" + result +
        ", body=" + body +
        ", abuseBlock=" + abuseBlock +
        '}';
    }
  }

  /**
   * concrete function definition, corresponding to {@link org.mzi.core.def.FnDef}.
   *
   * @author re-xyr
   */
  final class FnDecl implements Decl {
    public final @NotNull SourcePos sourcePos;
    public final @NotNull EnumSet<Modifier> modifiers;
    public final @Nullable Assoc assoc;
    public final @NotNull DefVar<FnDecl> ref;
    public final @NotNull Buffer<Param> telescope;
    public final @NotNull Expr result;
    public final @NotNull Expr body;
    public final @NotNull Buffer<Stmt> abuseBlock;

    public FnDecl(
      @NotNull SourcePos sourcePos,
      @NotNull EnumSet<Modifier> modifiers,
      @Nullable Assoc assoc,
      @NotNull String name,
      @NotNull Buffer<Param> telescope,
      @NotNull Expr result,
      @NotNull Expr body,
      @NotNull Buffer<Stmt> abuseBlock
    ) {
      this.sourcePos = sourcePos;
      this.modifiers = modifiers;
      this.assoc = assoc;
      this.ref = new DefVar<>(this, name);
      this.telescope = telescope;
      this.result = result;
      this.body = body;
      this.abuseBlock = abuseBlock;
    }

    private FnDecl(
      @NotNull SourcePos sourcePos,
      @NotNull EnumSet<Modifier> modifiers,
      @Nullable Assoc assoc,
      @NotNull DefVar<FnDecl> ref,
      @NotNull Buffer<Param> telescope,
      @NotNull Expr result,
      @NotNull Expr body,
      @NotNull Buffer<Stmt> abuseBlock
    ) {
      this.sourcePos = sourcePos;
      this.modifiers = modifiers;
      this.assoc = assoc;
      this.ref = ref;
      this.telescope = telescope;
      this.result = result;
      this.body = body;
      this.abuseBlock = abuseBlock;
    }

    @Contract(value = "_, _, _, _, _, _, _ -> new", pure = true) public @NotNull FnDecl copy(
      @NotNull SourcePos sourcePos,
      @NotNull EnumSet<Modifier> modifiers,
      @Nullable Assoc assoc,
      @NotNull Buffer<Param> telescope,
      @NotNull Expr result,
      @NotNull Expr body,
      @NotNull Buffer<Stmt> abuseBlock
    ) {
      return new FnDecl(sourcePos, modifiers, assoc, ref, telescope, result, body, abuseBlock);
    }

    public @NotNull SourcePos sourcePos() {
      return this.sourcePos;
    }

    public @NotNull DefVar<FnDecl> ref() {
      return this.ref;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof FnDecl fnDecl)) return false;
      return sourcePos.equals(fnDecl.sourcePos) &&
        modifiers.equals(fnDecl.modifiers) &&
        assoc == fnDecl.assoc &&
        telescope.equals(fnDecl.telescope) &&
        result.equals(fnDecl.result) &&
        body.equals(fnDecl.body) &&
        abuseBlock.equals(fnDecl.abuseBlock);
    }

    @Override
    public int hashCode() {
      return Objects.hash(sourcePos, modifiers, assoc, telescope, result, body, abuseBlock);
    }

    @Override public String toString() {
      return "FnDecl{" +
        "sourcePos=" + sourcePos +
        ", modifiers=" + modifiers +
        ", assoc=" + assoc +
        ", telescope=" + telescope +
        ", result=" + result +
        ", body=" + body +
        ", abuseBlock=" + abuseBlock +
        '}';
    }
  }
}
