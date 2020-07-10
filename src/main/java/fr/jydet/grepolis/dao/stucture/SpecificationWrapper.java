package fr.jydet.grepolis.dao.stucture;

import cz.jirutka.rsql.parser.RSQLParser;
import fr.jydet.grepolis.dao.stucture.rsql.CustomRsqlVisitor;
import lombok.Getter;
import org.apache.commons.lang3.StringEscapeUtils;

@Getter
public class SpecificationWrapper<T> {

    private static final int PAGE_SIZE_MAX = 20;

    private Specification<T> specification;
    private final Pageable pageable;

    public SpecificationWrapper(Specification<T> specification, int page, int rpp) {
        if (rpp > PAGE_SIZE_MAX || rpp < 1) {
            rpp = PAGE_SIZE_MAX;
        }
        page = page - 1;
        if (page < 0) {
            page = 0;
        }
        this.specification = specification;
        this.pageable = PageRequest.of(page, rpp);
    }

    public void add(Specification<T> other) {
        if (specification == null) {
            specification = other;
        } else {
            specification = specification.and(other);
        }
    }

    public static <T> Specification<T> parse(String request) {
        return new RSQLParser().parse(request).accept(new CustomRsqlVisitor<>());
    }

    public static <T> Specification<T> formatAndParse(String request, String... arguments) {
        Object[] protectedArgs = new String[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            protectedArgs[i] = protect(arguments[i]);
        }
        return new RSQLParser().parse(String.format(request, protectedArgs)).accept(new CustomRsqlVisitor<>());
    }

    public static String protect(String arg) {
        return '\'' + StringEscapeUtils.escapeJava(arg) + '\'';
    }
}
