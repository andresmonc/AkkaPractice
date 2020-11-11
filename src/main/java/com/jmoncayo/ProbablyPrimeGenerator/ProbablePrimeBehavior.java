package com.jmoncayo.ProbablyPrimeGenerator;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public class ProbablePrimeBehavior extends AbstractBehavior<ProbablePrimeBehavior.Command> {

    public static class Command implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String message; // the message for the behavior to work with. messages should be immutable. No setters
        private final ActorRef<ManagerBehavior.command> sender;

        public Command(String message, ActorRef<ManagerBehavior.command> sender) {
            this.message = message;
            this.sender = sender;
        }

        public String getMessage() {
            return message;
        }

        public ActorRef<ManagerBehavior.command> getSender() {
            return sender;
        }
    }

    private ProbablePrimeBehavior(ActorContext<ProbablePrimeBehavior.Command> context) {
        super(context);
    }

    public static Behavior<ProbablePrimeBehavior.Command> create() {
        return Behaviors.setup(ProbablePrimeBehavior::new);
    }

    private BigInteger prime;


    @Override
    public Receive<ProbablePrimeBehavior.Command> createReceive() {
        return newReceiveBuilder()
                .onAnyMessage(command -> {

                    if (command.getMessage().equals("create")) {
                        if (prime == null) {
                            prime = new BigInteger(2000, new Random()).nextProbablePrime();
                        }
                        command.getSender().tell(new ManagerBehavior.ResultCommand(prime));
                    }

                    return this;
                })
                .build();
    }
}
