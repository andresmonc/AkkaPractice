package com.jmoncayo;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;

public class ManagerBehavior extends AbstractBehavior<String> {

    private ManagerBehavior(ActorContext<String> context) {
        super(context);
    }

    //our manager can accept different message types so we will use an interface
    public interface command extends Serializable {

    }

    public static Behavior<String> create() {
        return Behaviors.setup(ManagerBehavior::new);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("start", () -> {
                    for (int i = 0; i < 20; i++) {
                        ActorRef<ProbablePrimeBehavior.Command> probablePrimeActor = getContext().spawn(ProbablePrimeBehavior.create(),"prime-"+ i);
                        probablePrimeActor.tell(new ProbablePrimeBehavior.Command("create",getContext().getSelf()));
                    }
                    return this;
                })
                .build();
    }
}
