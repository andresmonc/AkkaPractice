package com.jmoncayo;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;

public class ManagerBehavior extends AbstractBehavior<ManagerBehavior.command> {

    private ManagerBehavior(ActorContext<ManagerBehavior.command> context) {
        super(context);
    }

    //our manager can accept different message types so we will use an interface
    public interface command extends Serializable {
    }

    public static class InstructionCommand implements command {
        public static final long serialVersionUID = 1L;
        //immutable- final/getter only
        private final String message;

        public InstructionCommand(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static Behavior<ManagerBehavior.command> create() {
        return Behaviors.setup(ManagerBehavior::new);
    }

    @Override
    public Receive<ManagerBehavior.command> createReceive() {
        return newReceiveBuilder()
                .onMessage(InstructionCommand.class,instructionCommand -> {
                    if(instructionCommand.getMessage().equals("start")){
                        for (int i = 0; i < 20; i++) {
                            ActorRef<ProbablePrimeBehavior.Command> probablePrimeActor = getContext().spawn(ProbablePrimeBehavior.create(), "prime-" + i);
                            probablePrimeActor.tell(new ProbablePrimeBehavior.Command("create", getContext().getSelf()));
                        }
                    }
                    return this;
                })
                .build();
    }
}
