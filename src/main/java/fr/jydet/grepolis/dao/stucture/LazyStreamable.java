package fr.jydet.grepolis.dao.stucture;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

@EqualsAndHashCode
@RequiredArgsConstructor
@Getter
public class LazyStreamable<T> implements Streamable<T> {
    private final Supplier<? extends Stream<T>> stream;

    public Iterator<T> iterator() {
        return this.stream().iterator();
    }
    public Stream<T> stream() {
        return this.stream.get();
    }

    public static <T> LazyStreamable<T> of(Supplier<? extends Stream<T>> stream) {
        return new LazyStreamable(stream);
    }
}
