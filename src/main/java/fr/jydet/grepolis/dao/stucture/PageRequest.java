package fr.jydet.grepolis.dao.stucture;

public class PageRequest extends AbstractPageRequest {
    private static final long serialVersionUID = -4541509938956089562L;
    private final Sort sort;

    protected PageRequest(int page, int size, Sort sort) {
        super(page, size);
        this.sort = sort;
    }

    public static PageRequest of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static PageRequest of(int page, int size, Sort sort) {
        return new PageRequest(page, size, sort);
    }

    public static PageRequest of(int page, int size, Sort.Direction direction, String... properties) {
        return of(page, size, Sort.by(direction, properties));
    }

    public Sort getSort() {
        return this.sort;
    }

    public Pageable next() {
        return new PageRequest(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }

    public PageRequest previous() {
        return this.getPageNumber() == 0 ? this : new PageRequest(this.getPageNumber() - 1, this.getPageSize(), this.getSort());
    }

    public Pageable first() {
        return new PageRequest(0, this.getPageSize(), this.getSort());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof PageRequest)) {
            return false;
        } else {
            PageRequest that = (PageRequest)obj;
            return super.equals(that) && this.sort.equals(that.sort);
        }
    }

    public int hashCode() {
        return 31 * super.hashCode() + this.sort.hashCode();
    }

    public String toString() {
        return String.format("Page request [number: %d, size %d, sort: %s]", this.getPageNumber(), this.getPageSize(), this.sort);
    }
}