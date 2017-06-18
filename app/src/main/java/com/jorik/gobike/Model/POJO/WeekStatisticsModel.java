package com.jorik.gobike.Model.POJO;


import java.util.List;

public class WeekStatisticsModel {
  private List<TotalStatisticsModel> mTotalStatisticsModels;
  private List<GraphModel> mGraphModels;

  public WeekStatisticsModel(
      List<TotalStatisticsModel> totalStatisticsModels,
      List<GraphModel> graphModels) {
    mTotalStatisticsModels = totalStatisticsModels;
    mGraphModels = graphModels;
  }

  public List<TotalStatisticsModel> getTotalStatisticsModels() {
    return mTotalStatisticsModels;
  }

  public void setTotalStatisticsModels(
      List<TotalStatisticsModel> totalStatisticsModels) {
    mTotalStatisticsModels = totalStatisticsModels;
  }

  public List<GraphModel> getGraphModels() {
    return mGraphModels;
  }

  public void setGraphModels(List<GraphModel> graphModels) {
    mGraphModels = graphModels;
  }
}
