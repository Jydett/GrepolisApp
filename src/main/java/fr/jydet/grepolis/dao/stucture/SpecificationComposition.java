package fr.jydet.grepolis.dao.stucture;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

class SpecificationComposition {
    SpecificationComposition() {
    }

    
    static <T> Specification<T> composed(Specification<T> lhs,  Specification<T> rhs, SpecificationComposition.Combiner combiner) {
        return (root, query, builder) -> {
            Predicate otherPredicate = toPredicate(lhs, root, query, builder);
            Predicate thisPredicate = toPredicate(rhs, root, query, builder);
            if (thisPredicate == null) {
                return otherPredicate;
            } else {
                return otherPredicate == null ? thisPredicate : combiner.combine(builder, thisPredicate, otherPredicate);
            }
        };
    }

    private static <T> Predicate toPredicate(Specification<T> specification, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return specification == null ? null : specification.toPredicate(root, query, builder);
    }

    interface Combiner extends Serializable {
        Predicate combine(CriteriaBuilder var1,  Predicate var2,  Predicate var3);
    }
}

