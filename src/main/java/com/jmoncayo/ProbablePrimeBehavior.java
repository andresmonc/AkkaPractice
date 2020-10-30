package com.jmoncayo;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.math.BigInteger;
import java.util.Random;

public class ProbablePrimeBehavior extends AbstractBehavior<String> {

    private ProbablePrimeBehavior(ActorContext<String> context) {
        super(context);
    }

    public static Behavior<String> create(){
        return Behaviors.setup(ProbablePrimeBehavior::new);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("create", () ->{
                    BigInteger bigInt = new BigInteger(2000,new Random());
                    System.out.println(bigInt.nextProbablePrime());
                    return this;
                })
                .build();
    }
}
