package org.mzi.test;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.mzi.api.ref.Ref;
import org.mzi.core.def.FnDef;
import org.mzi.core.term.Tele;
import org.mzi.core.term.Term;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author ice1000
 */
@TestOnly
public interface Lisp {
  static @Nullable Term somehowParse(@NotNull @NonNls @Language("TEXT") String code, @NotNull Map<String, @NotNull Ref> refs) {
    return TermProducer.parse(code, refs);
  }

  static @NotNull Term reallyParse(@NotNull @NonNls @Language("TEXT") String code) {
    return reallyParse(code, new TreeMap<>());
  }

  static @NotNull Term reallyParse(@NotNull @NonNls @Language("TEXT") String code, @NotNull Map<String, @NotNull Ref> refs) {
    return Objects.requireNonNull(somehowParse(code, refs));
  }

  static @Nullable Tele somehowParseTele(@NotNull @NonNls @Language("TEXT") String code, @NotNull Map<String, @NotNull Ref> refs) {
    return TermProducer.parseTele(code, refs);
  }

  static @NotNull FnDef reallyParseDef(
    @NotNull @NonNls String name,
    @NotNull @NonNls @Language("TEXT") String teleCode,
    @NotNull @NonNls @Language("TEXT") String resultTypeCode,
    @NotNull @NonNls @Language("TEXT") String bodyCode,
    @NotNull Map<String, @NotNull Ref> refs) {
    var tele = reallyParseTele(teleCode, refs);
    var result = reallyParse(resultTypeCode, refs);
    var body = reallyParse(bodyCode, refs);
    var def = new FnDef(name, tele, result, body);
    var ref = def.ref();
    refs.put(name, ref);
    return def;
  }

  static @NotNull Tele reallyParseTele(@NotNull @NonNls @Language("TEXT") String code, @NotNull Map<String, @NotNull Ref> refs) {
    return Objects.requireNonNull(somehowParseTele(code, refs));
  }
}
