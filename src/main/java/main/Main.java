package main;

import actor.MainActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import data.Data;
import file.DataReader;
import file.FileReaderInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Future;

/**
 * Main program class
 */
public class Main {

    /**
     * Entry point of the program
     *
     * @param args arguments for the program
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new Main();
    }

    // data files
    private final String[] FILES = new String[] {
            "IFF-7-2_Kristinaitis_Giedrius_dat_1.json",
            "IFF-7-2_Kristinaitis_Giedrius_dat_2.json",
            "IFF-7-2_Kristinaitis_Giedrius_dat_3.json"
    };

    // result file
    public static final String FILE_RESULTS = "IFF-7-2_Kristinaitis_Giedrius_rez.txt";

    /**
     * Class constructor
     *
     * @throws Exception
     */
    private Main() throws Exception {
        updateResultFile(FILE_RESULTS);

        for (String file : FILES) {
            printFileHeader(file, FILE_RESULTS);

            execute(file);
        }
    }

    /**
     * Processed one data file
     *
     * @param dataFile file to process
     * @throws Exception
     */
    private void execute(String dataFile) throws Exception {
        ActorSystem system = ActorSystem.create("main-system");

        ActorRef mainActorRef = system.actorOf(Props.create(MainActor.class), "main-actor");

        readAndSendData(dataFile, mainActorRef);

        Future<Terminated> terminate = system.getWhenTerminated().toCompletableFuture();

        terminate.get();
    }

    /**
     * Reads all data from the given file and sends it to the given actor
     *
     * @param dataFile file to read from
     * @param actor    actor to send data to
     */
    private void readAndSendData(String dataFile, ActorRef actor) {
        FileReaderInterface<Data> reader = new DataReader();

        Data[] elements = reader.readAll(dataFile);

        actor.tell(elements.length, ActorRef.noSender());

        for (Data data : elements) {
            actor.tell(data, ActorRef.noSender());
        }
    }

    /**
     * (Re)creates the result file
     *
     * @param fileName name of the result file
     */
    private void updateResultFile(String fileName) {
        File resultFile = new File(fileName);

        if (resultFile.exists()) {
            resultFile.delete();
        }
    }

    /**
     * Prints a header with data file name and column labels to the result file
     *
     * @param dataFileName name of the processed data file
     * @param resultFile   name of the result file
     */
    private void printFileHeader(String dataFileName, String resultFile) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(resultFile, true));

            writer.println("-------------------------------------------------------------");
            writer.println(dataFileName + " Results");
            writer.println("-------------------------------------------------------------");
            writer.printf("%25s|%10s|%10s|%10s\n", "Title", "Price", "Quantity", "Result");
            writer.println("-------------------------------------------------------------");

            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
