package data;

/**
 * Abstract data processor
 *
 * @param <T> type of processed data
 */
public interface ProcessorInterface<T> {

    /**
     * Processes the given data element
     *
     * @param data element to process
     */
    void process(T data);
}
