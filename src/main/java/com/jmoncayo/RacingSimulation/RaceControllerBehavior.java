package com.jmoncayo.RacingSimulation;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.io.Serializable;

public class RaceControllerBehavior extends AbstractBehavior<RaceControllerBehavior.command> {

    public interface command extends Serializable {
    }

    public static class StartCommand implements command {
        private static final long serialVersionUID = 1L;
        public StartCommand() {
        }
    }

    private RaceControllerBehavior(ActorContext<command> context) {
        super(context);
    }

    public static Behavior<command> create() {
        return Behaviors.setup(RaceControllerBehavior::new);
    }


    @Override
    public Receive<command> createReceive() {
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
