package actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import data.Data;
import data.WorkerMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Main actor (sends messages to worker actors)
 */
public class MainActor extends AbstractActor {

    // worker count
    private static final int WORKER_COUNT = 4;

    // router used to route messages to workers
    private Router router;

    // result actor
    private ActorRef resultActor;

    // how many elements there are in total
    private int elementCount;

    // how many elements were processed
    private int elementsProcessed;

    /**
     * Called before the actor starts. Initializes all actors and router
     *
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        super.preStart();

        resultActor = createResultActor();

        List<ActorRef> workers = createWorkerActors(WORKER_COUNT);

        router = createRouter(workers);
    }

    /**
     * Binds message receiving functions
     *
     * @return
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Data.class, message -> {
                    router.route(new WorkerMessage(message, resultActor), getSelf());
                })
                .match(Integer.class, message -> elementCount = message)
                .match(Boolean.class, message -> {
                    elementsProcessed++;

                    if (elementsProcessed >= elementCount) {
                        getContext().getSystem().terminate();
                    }
                })
                .build();
    }

    /**
     * Creates given number of worker actors
     *
     * @param count worker count
     * @return
     */
    private List<ActorRef> createWorkerActors(int count) {
        List<ActorRef> actors = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            ActorRef actor = getContext().actorOf(Props.create(WorkerActor.class));

            getContext().watch(actor);

            actors.add(actor);
        }

        return actors;
    }

    /**
     * Creates the result-printing actor
     *
     * @return
     */
    private ActorRef createResultActor() {
        ActorRef actor = getContext().actorOf(Props.create(ResultActor.class));

        getContext().watch(actor);

        return actor;
    }

    /**
     * Creates router to route messages
     *
     * @param actors actors to route messages to
     * @return
     */
    private Router createRouter(Iterable<ActorRef> actors) {
        List<Routee> routees = new ArrayList<>();

        for (ActorRef actor : actors) {
            routees.add(new ActorRefRoutee(actor));
        }

        return new Router(new RoundRobinRoutingLogic(), routees);
    }
}
