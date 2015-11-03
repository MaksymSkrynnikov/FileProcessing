package com.test;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

/**
 * test
 * Skrynnikov.M
 * 03.11.15  12:38
 */
public class ReduceActor extends AbstractActor {

  public ReduceActor() {
    receive(ReceiveBuilder
                   .match(ReducePayload.class, this::reduceAmount)
                   .matchAny(this::unhandled)
                   .build()
    );
  }

  private void reduceAmount(ReducePayload payload) throws IOException {
      File file = payload.getResultFile();
      String id = payload.getId();
      List<Integer> amounts = payload.getAmounts();
      Optional<Integer> optionalResult = amounts.stream().reduce((a, b) -> a + b);
      if (optionalResult.isPresent()) {
        String stringToWrite = id + ":" + optionalResult.get() + "\n";
        Files.write(Paths.get(file.toURI()), stringToWrite.getBytes("utf-8"), StandardOpenOption.APPEND);
    }
  }
}
