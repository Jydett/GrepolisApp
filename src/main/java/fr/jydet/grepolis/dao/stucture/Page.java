package fr.jydet.grepolis.dao.stucture;

import java.util.Collections;
import java.util.function.Function;

public interface Page<T> extends Slice<T> {
    static <T> Page<T> empty() {
        return empty(Pageable.unpaged());
    }

    static <T> Page<T> empty(Pageable pageable) {
        return new PageImpl(Collections.emptyList(), pageable, 0L);
    }

    int getTotalPages();

    long getTotalElements();

    <U> Page<U> map(Function<? super T, ? extends U> var1);
}