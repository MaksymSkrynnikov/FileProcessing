package com.test;

import java.io.File;
import java.util.List;

/**
 * test
 * Skrynnikov.M
 * 03.11.15  12:36
 */
public class ReducePayload {

  private final String        id;
  private final List<Integer> amounts;
  private final File          resultFile;

  public ReducePayload(String id, List<Integer> amounts, File resultFile) {
    this.id = id;
    this.amounts = amounts;
    this.resultFile = resultFile;
  }

  public String getId() {
    return id;
  }

  public List<Integer> getAmounts() {
    return amounts;
  }

  public File getResultFile() {
    return resultFile;
  }

  @Override
  public String toString() {
    return "ReducePayload{" +
           "id='" + id + '\'' +
           ", amounts=" + amounts +
           ", resultFile=" + resultFile +
           '}';
  }
}
