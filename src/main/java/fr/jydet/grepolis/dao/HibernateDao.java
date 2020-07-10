package fr.jydet.grepolis.dao;

import fr.jydet.grepolis.dao.stucture.IDao;
import fr.jydet.grepolis.dao.stucture.Page;
import fr.jydet.grepolis.dao.stucture.PageImpl;
import fr.jydet.grepolis.dao.stucture.PageRequest;
import fr.jydet.grepolis.dao.stucture.Pageable;
import fr.jydet.grepolis.dao.stucture.Specification;
import fr.jydet.grepolis.model.Identifiable;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.LongSupplier;

public abstract class HibernateDao<Id extends Serializable, T extends Identifiable<Id>> implements IDao<Id, T>, Serializable {

    protected Session SESSION;

    protected final String className;
    protected final Class<T> clazz;
    protected final String tableName;

    public HibernateDao(Class<T> clazz) {
        this.clazz = clazz;
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation == null || tableAnnotation.name().isEmpty()) {
            tableName = clazz.getSimpleName().toLowerCase();
        } else {
            tableName = tableAnnotation.name();
        }
        className = clazz.getSimpleName();
    }

    public void save(T toSave) {
        Transaction transaction = getSession().beginTransaction();
        getSession().save(toSave);
        transaction.commit();
    }

    public Optional<T> getById(Id id) {
        return Optional.ofNullable(getSession().get(clazz, id));
    }

    public void remove(T toRemove) {
        Transaction transaction = getSession().beginTransaction();
        getSession().delete(toRemove);
        transaction.commit();
    }

    public List<T> getAll() {
        return getSession().createQuery("FROM " + className, clazz).list();
    }

    public Session getSession() {
        if (SESSION == null || ! SESSION.isOpen()) {
            SESSION = new Configuration().configure().buildSessionFactory().openSession();
        }
        return SESSION;
    }

    public T proxy(Id id) {
        return getSession().load(clazz, id);
    }

    protected Optional<T> getOne(Query<T> query) {
        return Optional.ofNullable(query.uniqueResult());
    }

    public long count() {
        return this.getSession().createQuery(String.format("select count(x) from %s x", className), Long.class).getSingleResult();
    }

    public void saveAll(Collection<T> toSave) {
        final Session session = getSession();
        Transaction transaction = session.beginTransaction();
        final Iterator<T> iterator = toSave.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            session.save(iterator.next());
            if (i % 50 == 0) {
                session.flush();
                session.clear();
            }
        }
        transaction.commit();
    }

    public Page<T> findAll(Specification<T> specification) {
        return findAll(specification, PageRequest.of(0, 5));
    }

    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> criteriaBuilderQuery = criteriaBuilder.createQuery(clazz);
        Root<T> from = criteriaBuilderQuery.from(clazz);
        if (specification != null) {
            criteriaBuilderQuery.where(specification.toPredicate(from, criteriaBuilderQuery, criteriaBuilder));
        }
        TypedQuery<T> query = getSession().createQuery(criteriaBuilderQuery);
        if (pageable.isUnpaged()) {
            return new PageImpl<>(query.getResultList());
        } else {
            query.setFirstResult((int)pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
            return getPage(query.getResultList(), pageable, () -> executeCountQuery(this.getCountQuery(specification, clazz)));
        }
    }

    protected <S extends T> TypedQuery<Long> getCountQuery(Specification<S> spec, Class<S> domainClass) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<S> root = this.applySpecificationToCriteria(spec, domainClass, query);
        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }

        query.orderBy(Collections.emptyList());
        return this.getSession().createQuery(query);
    }

    private <S, U extends T> Root<U> applySpecificationToCriteria(Specification<U> spec, Class<U> domainClass, CriteriaQuery<S> query) {
        Root<U> root = query.from(domainClass);
        if (spec == null) {
            return root;
        } else {
            CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
            Predicate predicate = spec.toPredicate(root, query, builder);
            if (predicate != null) {
                query.where(predicate);
            }

            return root;
        }
    }

    public static <T> Page<T> getPage(List<T> content, Pageable pageable, LongSupplier totalSupplier) {
        if (!pageable.isUnpaged() && pageable.getOffset() != 0L) {
            return content.size() != 0 && pageable.getPageSize() > content.size() ? new PageImpl(content, pageable, pageable.getOffset() + (long)content.size()) : new PageImpl(content, pageable, totalSupplier.getAsLong());
        } else {
            return !pageable.isUnpaged() && pageable.getPageSize() <= content.size() ? new PageImpl(content, pageable, totalSupplier.getAsLong()) : new PageImpl(content, pageable, (long)content.size());
        }
    }

    private static long executeCountQuery(TypedQuery<Long> query) {
        List<Long> totals = query.getResultList();
        long total = 0L;

        Long element;
        for (Iterator<Long> var4 = totals.iterator(); var4.hasNext(); total += element == null ? 0L : element) {
            element = var4.next();
        }

        return total;
    }
}
