package com.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * test
 * Skrynnikov.M
 * 03.11.15  14:45
 */
public class ReadActor {

  private static final ConcurrentMap<String, List<Integer>> aggregationMap = new ConcurrentHashMap<>();

  private static final String DELIMITER      = ";";
  private static final int    ID_ELEMENT     = 0;
  private static final int    AMOUNT_ELEMENT = 1;

  private void read(File file) throws IOException {
    RandomAccessFile accessFile = new RandomAccessFile(file, "r");
    String line = accessFile.readLine();
    mapLine(line);
  }

  private void mapLine(String line) {
    String[] splitLine = line.split(DELIMITER);
    if (splitLine.length == 2) {
      String id = splitLine[ID_ELEMENT];
      Integer amount = Integer.valueOf(splitLine[AMOUNT_ELEMENT]);
      if (!aggregationMap.containsKey(id)) {
        aggregationMap.put(id, new LinkedList<>());
      }
      aggregationMap.get(id).add(amount);
    }
  }

  private void reduce(File resultFile) {
    Set<String> keySet = aggregationMap.keySet();
    keySet.forEach(key -> {
      List<Integer> amounts = aggregationMap.get(key);
      ReducePayload payload = new ReducePayload(key, amounts, resultFile);
      ActorSystem.create("akka-system").actorOf(Props.create(ReduceActor.class)).tell(payload, ActorRef.noSender());
    });
  }
}
