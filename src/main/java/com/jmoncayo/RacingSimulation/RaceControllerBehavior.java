package com.jmoncayo.RacingSimulation;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.io.Serializable;

public class RaceControllerBehavior extends AbstractBehavior<RaceControllerBehavior.command> {

    private RaceControllerBehavior(ActorContext<command> context) {
        super(context);
    }

    public static Behavior<command> create(){
        return Behaviors.setup(RaceControllerBehavior::new);
    }

    public interface command extends Serializable {
    }

    @Override
    public Receive<command> createReceive() {
        return null;
    }
}
