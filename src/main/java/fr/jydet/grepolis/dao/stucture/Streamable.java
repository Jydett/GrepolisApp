package fr.jydet.grepolis.dao.stucture;

import org.hibernate.engine.jdbc.StreamUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface Streamable<T> extends Iterable<T>, Supplier<Stream<T>> {
    static <T> Streamable<T> empty() {
        return Collections ::emptyIterator;
    }

    @SafeVarargs
    static <T> Streamable<T> of(T... t) {
        return () -> {
            return Arrays.asList(t).iterator();
        };
    }

    static <T> Streamable<T> of(Iterable<T> iterable) {
        return iterable::iterator;
    }

    static <T> Streamable<T> of(Supplier<? extends Stream<T>> supplier) {
        return LazyStreamable.of(supplier);
    }

    default Stream<T> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    default <R> Streamable<R> map(Function<? super T, ? extends R> mapper) {
        return of(() -> {
            return this.stream().map(mapper);
        });
    }

    default <R> Streamable<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return of(() -> {
            return this.stream().flatMap(mapper);
        });
    }

    default Streamable<T> filter(Predicate<? super T> predicate) {
        return of(() -> {
            return this.stream().filter(predicate);
        });
    }

    default boolean isEmpty() {
        return !this.iterator().hasNext();
    }

    default Streamable<T> and(Supplier<? extends Stream<? extends T>> stream) {
        return of(() -> {
            return Stream.concat(this.stream(), (Stream)stream.get());
        });
    }

    default Streamable<T> and(T... others) {
        return of(() -> {
            return Stream.concat(this.stream(), Arrays.stream(others));
        });
    }

    default Streamable<T> and(Iterable<? extends T> iterable) {
        return of(() -> {
            return Stream.concat(this.stream(), StreamSupport.stream(iterable.spliterator(), false));
        });
    }

    default Streamable<T> and(Streamable<? extends T> streamable) {
        return this.and((Supplier)streamable);
    }

    default List<T> toList() {
        return this.stream().collect(Collectors.toUnmodifiableList());
    }

    default Set<T> toSet() {
        return (Set)this.stream().collect(Collectors.toUnmodifiableSet());
    }

    default Stream<T> get() {
        return this.stream();
    }

//    static <S> Collector<S, ?, Streamable<S>> toStreamable() {
//        return toStreamable(Collectors.toList());
//    }
//
//    static <S, T extends Iterable<S>> Collector<S, ?, Streamable<S>> toStreamable(Collector<S, ?, T> intermediate) {
//        return Collector.of(intermediate.supplier(), intermediate.accumulator(), intermediate.combiner(), Streamable::of);
//    }
}