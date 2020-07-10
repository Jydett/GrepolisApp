package fr.jydet.grepolis.dao.stucture;

import java.util.Optional;

public interface Pageable {
    static Pageable unpaged() {
        return Unpaged.INSTANCE;
    }

    default boolean isPaged() {
        return true;
    }

    default boolean isUnpaged() {
        return !this.isPaged();
    }

    int getPageNumber();

    int getPageSize();

    long getOffset();

    Sort getSort();

    default Sort getSortOr(Sort sort) {
        return this.getSort().isSorted() ? this.getSort() : sort;
    }

    Pageable next();

    Pageable previousOrFirst();

    Pageable first();

    boolean hasPrevious();

    default Optional<Pageable> toOptional() {
        return this.isUnpaged() ? Optional.empty() : Optional.of(this);
    }
}
