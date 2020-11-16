package com.jmoncayo.RacingSimulation;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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

    private class GetPositionsCommand implements Command {
        private static final long serialVersionUID = 1L;

    }

    private RaceControllerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(RaceControllerBehavior::new);
    }

    private Map<ActorRef<RacerBehavior.Command>, Integer> currentPositions;
    private long start;
    private int raceLength = 100;
    private Object TIMER_KEY;
    private int displayLength = 160;
    private void displayRace() {
        for (int i = 0; i < 50; ++i) System.out.println();
        System.out.println("Race has been running for " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
        System.out.println("    " + new String (new char[displayLength]).replace('\0', '='));
        int i = 0;
        for (ActorRef<RacerBehavior.Command> racer: currentPositions.keySet()) {
            System.out.println(i + " : "  + new String (new char[currentPositions.get(racer) * displayLength / 100]).replace('\0', '*'));
            i++;
        }
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartCommand.class, message -> {
                    start = System.currentTimeMillis();
                    currentPositions = new HashMap<>();
                    System.out.println("Race Controller lives!!!!!!!");
                    for (int i = 0; i < 10; i++) {
                        ActorRef<RacerBehavior.Command> racer = getContext().spawn(RacerBehavior.create(), "racer" + i);
                        currentPositions.put(racer, 0);
                        racer.tell(new RacerBehavior.StartCommand(raceLength));
                    }
                    return Behaviors.withTimers(timers -> {
                        timers.startTimerAtFixedRate(TIMER_KEY,new GetPositionsCommand(), Duration.ofMillis(500));
                        return this;
                    });
                })
                .onMessage(GetPositionsCommand.class, message -> {
                    currentPositions.forEach((commandActorRef, integer) -> {
                        commandActorRef.tell(new RacerBehavior.PositionCommand(getContext().getSelf()));
                        displayRace();
                    });
                    return this;
                })
                .onMessage(RacerUpdateCommand.class, message -> {
                    currentPositions.put(message.getRacer(), message.getPosition());
                    return this;
                })
                .build();
    }
}
