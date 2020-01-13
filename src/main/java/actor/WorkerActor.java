package actor;

import akka.actor.AbstractActor;
import data.*;

/**
 * Worker actor. Processes data and sends it to the result actor if necessary
 */
public class WorkerActor extends AbstractActor {

    // data processor and filter
    private ProcessorInterface<Data> processor = new DataProcessor();
    private FilterInterface<Data> filter = new DataFilter();

    /**
     * Binds message receiving functions
     *
     * @return
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(WorkerMessage.class, message -> {
            processor.process(message.data);

            if (filter.filter(message.data)) {
                message.forwardTo.tell(message.data, getSelf());
            }

            getSender().tell(true, getSelf());
        }).build();
    }
}
