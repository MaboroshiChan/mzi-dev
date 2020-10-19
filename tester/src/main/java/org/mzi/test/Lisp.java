package org.mzi.test;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.mzi.api.ref.Ref;
import org.mzi.core.tele.Tele;
import org.mzi.core.term.Term;

import java.util.Map;
import java.util.Objects;

/**
 * @author ice1000
 */
@TestOnly
public interface Lisp {
  static @Nullable Term somehowParse(@NotNull String code) {
    return ToTermVisitor.parse(code);
  }

  static @Nullable Term somehowParse(@NotNull String code, @NotNull Map<String, @NotNull Ref> refs) {
    return ToTermVisitor.parse(code, refs);
  }

  static @NotNull Term reallyParse(@NotNull String code) {
    return Objects.requireNonNull(somehowParse(code));
  }

  static @Nullable Tele somehowParseTele(@NotNull String code) {
    return ToTermVisitor.parseTele(code);
  }

  static @NotNull Tele reallyParseTele(@NotNull String code) {
    return Objects.requireNonNull(somehowParseTele(code));
  }
}
