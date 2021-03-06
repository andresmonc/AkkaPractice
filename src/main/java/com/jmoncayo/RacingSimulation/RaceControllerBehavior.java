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
        private final ActorRef<RacerBehavior.Command> racer;
        private final int position;

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

    public static class FinishedCommand implements Command {
        private static final long serialVersionUID = 1L;
        private final ActorRef<RacerBehavior.Command> racer;

        public FinishedCommand(ActorRef<RacerBehavior.Command> racer) {
            this.racer = racer;
        }

        public ActorRef<RacerBehavior.Command> getRacer() {
            return racer;
        }
    }

    private static class GetPositionsCommand implements Command {
        private static final long serialVersionUID = 1L;

    }

    private RaceControllerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(RaceControllerBehavior::new);
    }

    private Map<ActorRef<RacerBehavior.Command>, Integer> currentPositions;
    private Map<ActorRef<RacerBehavior.Command>, Long> finishingTimes;
    private long start;
    private final int raceLength = 100;
    private Object TIMER_KEY;

    private void displayRace() {
        for (int i = 0; i < 50; ++i) System.out.println();
        System.out.println("Race has been running for " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
        int displayLength = 160;
        System.out.println("    " + new String(new char[displayLength]).replace('\0', '='));
        int i = 0;
        for (ActorRef<RacerBehavior.Command> racer : currentPositions.keySet()) {
            System.out.println(i + " : " + new String(new char[currentPositions.get(racer) * displayLength / 100]).replace('\0', '*'));
            i++;
        }
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartCommand.class, message -> {
                    start = System.currentTimeMillis();
                    currentPositions = new HashMap<>();
                    finishingTimes = new HashMap<>();
                    System.out.println("Race Controller lives!!!!!!!");
                    for (int i = 0; i < 10; i++) {
                        ActorRef<RacerBehavior.Command> racer = getContext().spawn(RacerBehavior.create(), "racer" + i);
                        currentPositions.put(racer, 0);
                        racer.tell(new RacerBehavior.StartCommand(raceLength));
                    }
                    return Behaviors.withTimers(timers -> {
                        timers.startTimerAtFixedRate(TIMER_KEY, new GetPositionsCommand(), Duration.ofMillis(500));
                        return Behaviors.same();
                    });
                })
                .onMessage(GetPositionsCommand.class, message -> {
                    currentPositions.forEach((commandActorRef, integer) -> {
                        commandActorRef.tell(new RacerBehavior.PositionCommand(getContext().getSelf()));
                        displayRace();
                    });
                    return Behaviors.same();
                })
                .onMessage(RacerUpdateCommand.class, message -> {
                    currentPositions.put(message.getRacer(), message.getPosition());
                    return Behaviors.same();
                })
                .onMessage(FinishedCommand.class, message -> {
                    ActorRef<RacerBehavior.Command> racer = message.getRacer();
                    finishingTimes.put(racer, System.currentTimeMillis());
                    if (finishingTimes.size() == 10) {
                        return raceCompleted();
                    } else {
                        return Behaviors.same();
                    }
                })
                .build();
    }

    public Receive<Command> raceCompleted() {
        return newReceiveBuilder()
                .onMessage(GetPositionsCommand.class, msg -> {
                    currentPositions.forEach((racer, time) -> getContext().stop(racer));
                    displayResults();
                    return Behaviors.withTimers(timers -> {
                        timers.cancelAll();
                        return Behaviors.stopped();
                    });
                })
                .build();
    }

    private void displayResults() {
        System.out.println("results");
        finishingTimes.values().stream().sorted().forEach(aLong -> {
            for (ActorRef<RacerBehavior.Command> key : finishingTimes.keySet()) {
                if (finishingTimes.get(key) == aLong) {
                    String racerId = key.path().toString().substring(key.path().toString().length() - 1);
                    System.out.println("Racer# "+racerId + " : "+ ((double)aLong-start)/1000 + "s");
                }
            }
        });
    }

}
