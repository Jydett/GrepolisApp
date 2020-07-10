package fr.jydet.grepolis.dao.stucture;

import java.util.List;
import java.util.function.Function;

public interface Slice<T> extends Streamable<T> {
    int getNumber();

    int getSize();

    int getNumberOfElements();

    List<T> getContent();

    boolean hasContent();

    Sort getSort();

    boolean isFirst();

    boolean isLast();

    boolean hasNext();

    boolean hasPrevious();

    default Pageable getPageable() {
        return PageRequest.of(this.getNumber(), this.getSize(), this.getSort());
    }

    Pageable nextPageable();

    Pageable previousPageable();

    <U> Slice<U> map(Function<? super T, ? extends U> var1);

    default Pageable nextOrLastPageable() {
        return this.hasNext() ? this.nextPageable() : this.getPageable();
    }

    default Pageable previousOrFirstPageable() {
        return this.hasPrevious() ? this.previousPageable() : this.getPageable();
    }
}
