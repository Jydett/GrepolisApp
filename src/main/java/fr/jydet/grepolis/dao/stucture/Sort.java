package fr.jydet.grepolis.dao.stucture;

import lombok.Generated;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Sort implements Streamable<Sort.Order>, Serializable {
    private static final long serialVersionUID = 5737186511678863905L;
    private static final Sort UNSORTED = unsorted();
    public static final Sort.Direction DEFAULT_DIRECTION;
    private final List<Sort.Order> orders;

    private Sort(Sort.Direction direction, List<String> properties) {
        if (properties != null && ! properties.isEmpty()) {
            this.orders = (List) properties.stream().map((it) -> {
                return new Sort.Order(direction, it);
            }).collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }
    }

    public static Sort by(String... properties) {
        return properties.length == 0 ? unsorted() : new Sort(DEFAULT_DIRECTION, Arrays.asList(properties));
    }

    public static Sort by(List<Sort.Order> orders) {
        return orders.isEmpty() ? unsorted() : new Sort(orders);
    }

    public static Sort by(Sort.Order... orders) {
        return new Sort(Arrays.asList(orders));
    }

    public static Sort by(Sort.Direction direction, String... properties) {
        return by((List) Arrays.stream(properties).map((it) -> {
            return new Sort.Order(direction, it);
        }).collect(Collectors.toList()));
    }

//    public static <T> Sort.TypedSort<T> sort(Class<T> type) {
//        return new Sort.TypedSort(type);
//    }

    public static Sort unsorted() {
        return UNSORTED;
    }

    public Sort descending() {
        return this.withDirection(Sort.Direction.DESC);
    }

    public Sort ascending() {
        return this.withDirection(Sort.Direction.ASC);
    }

    public boolean isSorted() {
        return ! this.orders.isEmpty();
    }

    public boolean isUnsorted() {
        return ! this.isSorted();
    }

    public Sort and(Sort sort) {
        ArrayList<Order> these = new ArrayList(this.orders);
        Iterator var3 = sort.iterator();

        while (var3.hasNext()) {
            Sort.Order order = (Sort.Order) var3.next();
            these.add(order);
        }

        return by((List) these);
    }

    public Sort.Order getOrderFor(String property) {
        Iterator var2 = this.iterator();

        Sort.Order order;
        do {
            if (! var2.hasNext()) {
                return null;
            }

            order = (Sort.Order) var2.next();
        } while (! order.getProperty().equals(property));

        return order;
    }

    public Iterator<Sort.Order> iterator() {
        return this.orders.iterator();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (! (obj instanceof Sort)) {
            return false;
        } else {
            Sort that = (Sort) obj;
            return this.orders.equals(that.orders);
        }
    }

    public int hashCode() {
        int result = 31 * 17 + this.orders.hashCode();
        return result;
    }

    public String toString() {
        return this.orders.isEmpty() ? "UNSORTED" : collectionToCommaDelimitedString(this.orders);
    }

    public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
        if (coll.isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator it = coll.iterator();

            while(it.hasNext()) {
                sb.append(prefix).append(it.next()).append(suffix);
                if (it.hasNext()) {
                    sb.append(delim);
                }
            }

            return sb.toString();
        }
    }

    public static String collectionToDelimitedString(Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    public static String collectionToCommaDelimitedString(Collection<?> coll) {
        return collectionToDelimitedString(coll, ",");
    }
    
    
    private Sort withDirection(Sort.Direction direction) {
        return by((List) this.orders.stream().map((it) -> {
            return new Sort.Order(direction, it.getProperty());
        }).collect(Collectors.toList()));
    }

    @Generated
    protected Sort(List<Sort.Order> orders) {
        this.orders = orders;
    }

    static {
        DEFAULT_DIRECTION = Sort.Direction.ASC;
    }

//    public static class TypedSort<T> extends Sort {
//        private static final long serialVersionUID = - 3550403511206745880L;
//        private final Recorded<T> recorded;
//
//        private TypedSort(Class<T> type) {
//            this(MethodInvocationRecorder.forProxyOf(type));
//        }
//
//        private TypedSort(Recorded<T> recorded) {
//            super(Collections.emptyList());
//            this.recorded = recorded;
//        }
//
//        public <S> Sort.TypedSort<S> by(Function<T, S> property) {
//            return new Sort.TypedSort(this.recorded.record(property));
//        }
//
//        public <S> Sort.TypedSort<S> by(ToCollectionConverter<T, S> collectionProperty) {
//            return new Sort.TypedSort(this.recorded.record(collectionProperty));
//        }
//
//        public <S> Sort.TypedSort<S> by(ToMapConverter<T, S> mapProperty) {
//            return new Sort.TypedSort(this.recorded.record(mapProperty));
//        }
//
//        public Sort ascending() {
//            return this.withDirection(Sort :: ascending);
//        }
//
//        public Sort descending() {
//            return this.withDirection(Sort :: descending);
//        }
//
//        private Sort withDirection(Function<Sort, Sort> direction) {
//            return (Sort) this.recorded.getPropertyPath().map((xva$0) -> {
//                return Sort.by(xva$0);
//            }).map(direction).orElseGet(Sort :: unsorted);
//        }
//
//        public Iterator<Sort.Order> iterator() {
//            return ((Set) this.recorded.getPropertyPath().map(Sort.Order :: by).map(Collections :: singleton).orElseGet(Collections :: emptySet)).iterator();
//        }
//
//        public String toString() {
//            return ((Sort) this.recorded.getPropertyPath().map((xva$0) -> {
//                return Sort.by(xva$0);
//            }).orElseGet(Sort :: unsorted)).toString();
//        }
//    }

    public static class Order implements Serializable {
        private static final long serialVersionUID = 1522511010900108987L;
        private static final boolean DEFAULT_IGNORE_CASE = false;
        private static final Sort.NullHandling DEFAULT_NULL_HANDLING;
        private final Sort.Direction direction;
        private final String property;
        private final boolean ignoreCase;
        private final Sort.NullHandling nullHandling;

        public Order(Sort.Direction direction, String property) {
            this(direction, property, false, DEFAULT_NULL_HANDLING);
        }

        public Order(Sort.Direction direction, String property, Sort.NullHandling nullHandlingHint) {
            this(direction, property, false, nullHandlingHint);
        }

        public static Sort.Order by(String property) {
            return new Sort.Order(Sort.DEFAULT_DIRECTION, property);
        }

        public static Sort.Order asc(String property) {
            return new Sort.Order(Sort.Direction.ASC, property, DEFAULT_NULL_HANDLING);
        }

        public static Sort.Order desc(String property) {
            return new Sort.Order(Sort.Direction.DESC, property, DEFAULT_NULL_HANDLING);
        }

        private Order(Sort.Direction direction, String property, boolean ignoreCase, Sort.NullHandling nullHandling) {
            if (! hasText(property)) {
                throw new IllegalArgumentException("Property must not null or empty!");
            } else {
                this.direction = direction == null ? Sort.DEFAULT_DIRECTION : direction;
                this.property = property;
                this.ignoreCase = ignoreCase;
                this.nullHandling = nullHandling;
            }
        }

        public static boolean hasText(String str) {
            return str != null && !str.isEmpty() && containsText(str);
        }
        
        private static boolean containsText(CharSequence str) {
            int strLen = str.length();

            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }


        public Sort.Direction getDirection() {
            return this.direction;
        }

        public String getProperty() {
            return this.property;
        }

        public boolean isAscending() {
            return this.direction.isAscending();
        }

        public boolean isDescending() {
            return this.direction.isDescending();
        }

        public boolean isIgnoreCase() {
            return this.ignoreCase;
        }

        public Sort.Order with(Sort.Direction direction) {
            return new Sort.Order(direction, this.property, this.ignoreCase, this.nullHandling);
        }

        public Sort.Order withProperty(String property) {
            return new Sort.Order(this.direction, property, this.ignoreCase, this.nullHandling);
        }

        public Sort withProperties(String... properties) {
            return Sort.by(this.direction, properties);
        }

        public Sort.Order ignoreCase() {
            return new Sort.Order(this.direction, this.property, true, this.nullHandling);
        }

        public Sort.Order with(Sort.NullHandling nullHandling) {
            return new Sort.Order(this.direction, this.property, this.ignoreCase, nullHandling);
        }

        public Sort.Order nullsFirst() {
            return this.with(Sort.NullHandling.NULLS_FIRST);
        }

        public Sort.Order nullsLast() {
            return this.with(Sort.NullHandling.NULLS_LAST);
        }

        public Sort.Order nullsNative() {
            return this.with(Sort.NullHandling.NATIVE);
        }

        public Sort.NullHandling getNullHandling() {
            return this.nullHandling;
        }

        public int hashCode() {
            int result = 31 * 17 + this.direction.hashCode();
            result = 31 * result + this.property.hashCode();
            result = 31 * result + (this.ignoreCase ? 1 : 0);
            result = 31 * result + this.nullHandling.hashCode();
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof Sort.Order)) {
                return false;
            } else {
                Sort.Order that = (Sort.Order)obj;
                return this.direction.equals(that.direction) && this.property.equals(that.property) && this.ignoreCase == that.ignoreCase && this.nullHandling.equals(that.nullHandling);
            }
        }

        public String toString() {
            String result = String.format("%s: %s", this.property, this.direction);
            if (!Sort.NullHandling.NATIVE.equals(this.nullHandling)) {
                result = result + ", " + this.nullHandling;
            }

            if (this.ignoreCase) {
                result = result + ", ignoring case";
            }

            return result;
        }

        static {
            DEFAULT_NULL_HANDLING = Sort.NullHandling.NATIVE;
        }
    }

    public static enum NullHandling {
        NATIVE,
        NULLS_FIRST,
        NULLS_LAST;

        private NullHandling() {
        }
    }

    public static enum Direction {
        ASC,
        DESC;

        private Direction() {
        }

        public boolean isAscending() {
            return this.equals(ASC);
        }

        public boolean isDescending() {
            return this.equals(DESC);
        }

        public static Sort.Direction fromString(String value) {
            try {
                return valueOf(value.toUpperCase(Locale.US));
            } catch (Exception var2) {
                throw new IllegalArgumentException(String.format("Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), var2);
            }
        }

        public static Optional<Direction> fromOptionalString(String value) {
            try {
                return Optional.of(fromString(value));
            } catch (IllegalArgumentException var2) {
                return Optional.empty();
            }
        }
    }
}
