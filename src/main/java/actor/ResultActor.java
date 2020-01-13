package actor;

import akka.actor.AbstractActor;
import data.Data;
import main.Main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Result actor. Prints results to a file
 */
public class ResultActor extends AbstractActor {

    // will there be any more results
    private boolean noResults = true;

    // writer used to write to the result file
    private PrintWriter fileWriter;

    /**
     * Binds message receiving functions
     *
     * @return
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Data.class, message -> {
            noResults = false;

            appendToFile(message, fileWriter);
        }).build();
    }

    /**
     * Called before the actor starts. Initializes file writer
     *
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        super.preStart();

        try {
            fileWriter = new PrintWriter(new FileWriter(Main.FILE_RESULTS, true));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Called after the actor stops. Closes the file writer
     *
     * @throws Exception
     */
    @Override
    public void postStop() throws Exception {
        super.postStop();

        if (noResults && fileWriter != null) {
            fileWriter.println("No results - no elements match the filter");
        }

        if (fileWriter != null) {
            fileWriter.close();
        }
    }

    /**
     * Appends a data element to the result file
     *
     * @param data   data element
     * @param writer writer to write to
     */
    private void appendToFile(Data data, PrintWriter writer) {
        if (writer == null) {
            return;
        }

        writer.printf("%25s|%10f|%10d|%10s\n", data.getTitle(), data.getPrice(), data.getQuantity(), data.getResult());
        writer.flush();
    }
}
