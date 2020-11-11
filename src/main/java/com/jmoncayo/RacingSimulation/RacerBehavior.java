package com.jmoncayo.RacingSimulation;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;

public class RacerBehavior extends AbstractBehavior<RacerBehavior.Command> {

    public interface Command extends Serializable {
    }

    public static class StartCommand implements Command {
        private static final long serialVersionUID = 1L;
        private final int raceLength;

        public StartCommand(int raceLength) {
            this.raceLength = raceLength;
        }

        public int getRaceLength() {
            return raceLength;
        }
    }

    public static class PositionCommand implements Command {
        private static final long serialVersionUID = 1L;
        private ActorRef<RaceControllerBehavior.command> raceController;

        public PositionCommand(ActorRef<RaceControllerBehavior.command> raceController) {
            this.raceController = raceController;
        }

        public ActorRef<RaceControllerBehavior.command> getRaceController() {
            return raceController;
        }
    }

    private RacerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public Behavior<Command> create() {
        return Behaviors.setup(RacerBehavior::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return null;
    }
}
