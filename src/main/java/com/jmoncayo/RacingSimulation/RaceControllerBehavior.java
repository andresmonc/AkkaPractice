package com.jmoncayo.RacingSimulation;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.io.Serializable;

public class RaceControllerBehavior extends AbstractBehavior<RaceControllerBehavior.Command> {

    public interface Command extends Serializable {
    }

    public static class StartCommand implements Command {
        private static final long serialVersionUID = 1L;

        public StartCommand() {
        }
    }

    public static class RacerUpdateCommand implements Command {
        private static final long serialVersionUID = 1L;
        private ActorRef<RacerBehavior.Command> racer;
        private int position;

        public RacerUpdateCommand(ActorRef<RacerBehavior.Command> racer, int position) {
            this.racer = racer;
            this.position = position;
        }

        public ActorRef<RacerBehavior.Command> getRacer() {
            return racer;
        }

        public int getPosition() {
            return position;
        }
    }

    private RaceControllerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(RaceControllerBehavior::new);
    }


    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartCommand.class, startCommand -> {
                    System.out.println("Race Controller lives!!!!!!!");
                    for (int i = 0; i < 20; i++) {

                        System.out.println(i);
                    }
                    return this;
                })
                .build();
    }
}
