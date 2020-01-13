package data;

/**
 * Processes data
 */
public class DataProcessor implements ProcessorInterface<Data> {

    /**
     * Processes the given data element
     *
     * @param data element to process
     */
    @Override
    public void process(Data data) {
        if (data != null) {
            String result = "";

            for (int i = 0; i < data.getPrice() * Math.pow(data.getQuantity(), 2); i++) {
                result = Character.toString(data.getTitle().charAt((char) (i % (data.getTitle().length() - 1))));
            }

            data.setResult(result);
        }
    }
}
