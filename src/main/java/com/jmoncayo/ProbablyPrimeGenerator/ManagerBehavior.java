package com.jmoncayo.ProbablyPrimeGenerator;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;

public class ManagerBehavior extends AbstractBehavior<ManagerBehavior.command> {

    private ManagerBehavior(ActorContext<ManagerBehavior.command> context) {
        super(context);
    }

    //our manager can accept different message types so we will use an interface
    public interface command extends Serializable {
    }

    public static class InstructionCommand implements command {
        public static final long serialVersionUID = 1L;
        private final String message;

        public InstructionCommand(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ResultCommand implements command {
        public static final long serialVersionUID = 1L;
        private final BigInteger probablePrime;

        public ResultCommand(BigInteger probablePrime) {
            this.probablePrime = probablePrime;
        }

        public BigInteger getProbablePrime() {
            return probablePrime;
        }
    }

    public static Behavior<ManagerBehavior.command> create() {
        return Behaviors.setup(ManagerBehavior::new);
    }

    private final SortedSet<BigInteger> sortedPrimes = new TreeSet<>();

    @Override
    public Receive<ManagerBehavior.command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ResultCommand.class, resultCommand -> {
                    sortedPrimes.add(resultCommand.getProbablePrime());
                    System.out.println(sortedPrimes.size());
                    return this;
                })
                .onMessage(InstructionCommand.class, instructionCommand -> {
                    if (instructionCommand.getMessage().equals("start")) {
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
