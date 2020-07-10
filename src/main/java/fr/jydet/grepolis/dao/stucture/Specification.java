package fr.jydet.grepolis.dao.stucture;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public interface Specification<T> extends Serializable {
    long serialVersionUID = 1L;

    static <T> Specification<T> not( Specification<T> spec) {
        return spec == null ? (root, query, builder) -> null : (root, query, builder) -> builder.not(spec.toPredicate(root, query, builder));
    }

    
    static <T> Specification<T> where( Specification<T> spec) {
        return spec == null ? (root, query, builder) -> null : spec;
    }

    
    default Specification<T> and( Specification<T> other) {
        return SpecificationComposition.composed(this, other, CriteriaBuilder :: and);
    }

    
    default Specification<T> or( Specification<T> other) {
        return SpecificationComposition.composed(this, other, CriteriaBuilder :: or);
    }

    
    Predicate toPredicate(Root<T> var1, CriteriaQuery<?> var2, CriteriaBuilder var3);
}
