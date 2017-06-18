package com.jorik.gobike.Model.POJO;

import java.util.ArrayList;
import java.util.List;

public class GraphModel {

  private String nameGraph;
  private String index;
  private List<String> nameColumn;
  private List<Integer> values;

  public GraphModel(String nameGraph, String index, List<String> nameColumn,
      List<Integer> values) {
    this.nameGraph = nameGraph;
    this.index = index;
    this.nameColumn = nameColumn;
    this.values = values;
  }

  public String getNameGraph() {
    return nameGraph;
  }

  public void setNameGraph(String nameGraph) {
    this.nameGraph = nameGraph;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }

  public List<String> getNameColumn() {
    return nameColumn;
  }

  public void setNameColumn(List<String> nameColumn) {
    this.nameColumn = nameColumn;
  }

  public List<Integer> getValues() {
    return values;
  }

  public void setValues(List<Integer> values) {
    this.values = values;
  }
}
