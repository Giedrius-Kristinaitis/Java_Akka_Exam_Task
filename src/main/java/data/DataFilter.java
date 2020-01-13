package data;

/**
 * Filters data elements
 */
public class DataFilter implements FilterInterface<Data> {

    private static final char[] LETTERS = new char[] {
            'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p',
            'r', 's', 't', 'v', 'z'
    };

    /**
     * Filters the given data element
     *
     * @param data element to filter
     * @return
     */
    @Override
    public boolean filter(Data data) {
        return lettersContain(data.getResult().charAt(0));
    }

    /**
     * Checks if the letters array contains a character
     *
     * @param c character to check
     * @return
     */
    private boolean lettersContain(char c) {
        for (char letter : LETTERS) {
            if (letter == c) {
                return true;
            }
        }

        return false;
    }
}
