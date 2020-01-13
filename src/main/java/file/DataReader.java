package file;

import data.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads data elements from a file
 */
public class DataReader implements FileReaderInterface<Data> {

    /**
     * Reads all data elements from the given file and returns them as an array
     *
     * @param file file to read from
     * @return
     */
    @Override
    public Data[] readAll(String file) {
        List<String> fileLines = null;
        List<Data> dataList = new ArrayList<Data>();

        try {
            fileLines = Files.readAllLines(Paths.get(file));
        } catch (IOException ex) {
            ex.printStackTrace();
            return new Data[0];
        }

        JSONArray array = new JSONArray(String.join("", fileLines));

        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;

            dataList.add(new Data(
                    jsonObject.getString("title"),
                    jsonObject.getDouble("price"),
                    jsonObject.getInt("quantity")
            ));
        }

        return convertData(dataList.toArray());
    }

    /**
     * Converts Object[] array to Data[] array
     *
     * @param input array to convert
     * @return
     */
    private Data[] convertData(Object[] input) {
        Data[] data = new Data[input.length];

        for (int i = 0; i < input.length; i++) {
            data[i] = (Data) input[i];
        }

        return data;
    }
}
