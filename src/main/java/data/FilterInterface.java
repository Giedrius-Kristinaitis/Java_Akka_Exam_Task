package data;

/**
 * Abstract filter
 *
 * @param <T> type of the filtered data
 */
public interface FilterInterface<T> {

    /**
     * Filters the given data element
     *
     * @param data element to filter
     * @return true if the element matches the filter
     */
    boolean filter(T data);
}
