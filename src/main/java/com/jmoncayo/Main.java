package com.jmoncayo;

import akka.actor.typed.ActorSystem;

public class Main {
    public static void main(String[] args) {
        ActorSystem<String> simpleBehaviorActorSystem = ActorSystem.create(FirstSimpleBehavior.create(), "SimpleBehaviorActorSystem");
        simpleBehaviorActorSystem.tell("hey");
        simpleBehaviorActorSystem.tell("who are you?");
        simpleBehaviorActorSystem.tell("hey you");
        simpleBehaviorActorSystem.tell("how are ya");
        simpleBehaviorActorSystem.tell("say hello");
        simpleBehaviorActorSystem.tell("make more");

        ActorSystem<ManagerBehavior.command> probablePrimeSystem = ActorSystem.create(ManagerBehavior.create(),"probablePrimeSystem");
        probablePrimeSystem.tell(new ManagerBehavior.InstructionCommand("start"));
    }
}
