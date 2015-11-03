package com.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * test
 * Skrynnikov.M
 * 03.11.15  12:05
 */
public class FileManager {

  private static final String DELIMITER      = ";";
  private static final int    ID_ELEMENT     = 0;
  private static final int    AMOUNT_ELEMENT = 1;

  private Map<String, List<Integer>> aggregationMap = new HashMap();

  public void computeAmounts(String sourceFileName, String resultFileName) throws IOException {
    Files.lines(Paths.get(sourceFileName)).forEach(this::mapLine);
    File file = new File(resultFileName);
    if (!file.exists()) {
      file.createNewFile();
    }
    reduce(file);
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

  public static void main(String[] args) throws IOException {
    FileManager manager = new FileManager();
    manager.computeAmounts("test.txt", "result.txt");
  }
}
