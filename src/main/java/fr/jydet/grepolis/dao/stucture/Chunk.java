package fr.jydet.grepolis.dao.stucture;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
abstract class Chunk<T> implements Slice<T>, Serializable {
    private static final long serialVersionUID = 867755909294344406L;
    private final List<T> content = new ArrayList<>();
    @Getter
    private final Pageable pageable;

    public Chunk(List<T> content, Pageable pageable) {
        this.content.addAll(content);
        this.pageable = pageable;
    }

    public int getNumber() {
        return this.pageable.isPaged() ? this.pageable.getPageNumber() : 0;
    }

    public int getSize() {
        return this.pageable.isPaged() ? this.pageable.getPageSize() : this.content.size();
    }

    public int getNumberOfElements() {
        return this.content.size();
    }

    public boolean hasPrevious() {
        return this.getNumber() > 0;
    }

    public boolean isFirst() {
        return !this.hasPrevious();
    }

    public boolean isLast() {
        return !this.hasNext();
    }

    public Pageable nextPageable() {
        return this.hasNext() ? this.pageable.next() : Pageable.unpaged();
    }

    public Pageable previousPageable() {
        return this.hasPrevious() ? this.pageable.previousOrFirst() : Pageable.unpaged();
    }

    public boolean hasContent() {
        return !this.content.isEmpty();
    }

    public List<T> getContent() {
        return Collections.unmodifiableList(this.content);
    }

    public Sort getSort() {
        return this.pageable.getSort();
    }

    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        Stream<T> var10000 = this.stream();
        converter.getClass();
        return var10000.map(converter).collect(Collectors.toList());
    }
}