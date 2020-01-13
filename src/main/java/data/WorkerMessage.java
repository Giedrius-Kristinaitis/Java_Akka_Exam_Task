package data;

import akka.actor.ActorRef;

/**
 * Message that is being sent to a worker actor
 */
public class WorkerMessage {

    // data element
    public Data data;

    // actor to send processed data to
    public ActorRef forwardTo;

    /**
     * Class constructor
     *
     * @param data
     * @param forwardTo
     */
    public WorkerMessage(Data data, ActorRef forwardTo) {
        this.data = data;
        this.forwardTo = forwardTo;
    }
}
