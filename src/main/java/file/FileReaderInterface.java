package file;

/**
 * Abstract data reader
 *
 * @param <T> type of returned data
 */
public interface FileReaderInterface<T> {

    /**
     * Reads all data elements from the given file
     *
     * @param file file to read from
     * @return
     */
    T[] readAll(String file);
}
