package com.jmoncayo;

import akka.actor.typed.ActorSystem;
import com.jmoncayo.ProbablyPrimeGenerator.FirstSimpleBehavior;
import com.jmoncayo.ProbablyPrimeGenerator.ManagerBehavior;
import com.jmoncayo.RacingSimulation.RaceControllerBehavior;
import com.jmoncayo.RacingSimulation.RacerBehavior;

public class Main {
    public static void main(String[] args) {
        // First Simple Behavior Project
        ActorSystem<String> simpleBehaviorActorSystem = ActorSystem.create(FirstSimpleBehavior.create(), "SimpleBehaviorActorSystem");
        simpleBehaviorActorSystem.tell("hey");
        simpleBehaviorActorSystem.tell("who are you?");
        simpleBehaviorActorSystem.tell("hey you");
        simpleBehaviorActorSystem.tell("how are ya");
        simpleBehaviorActorSystem.tell("say hello");
        simpleBehaviorActorSystem.tell("make more");

        // probable prime example
        ActorSystem<ManagerBehavior.command> probablePrimeSystem = ActorSystem.create(ManagerBehavior.create(),"probablePrimeSystem");
        probablePrimeSystem.tell(new ManagerBehavior.InstructionCommand("start"));

        // Racing Simulation
        ActorSystem<RaceControllerBehavior.Command> raceController = ActorSystem.create(RaceControllerBehavior.create(),"raceControllerSystem");
        raceController.tell(new RaceControllerBehavior.StartCommand());

    }
}
