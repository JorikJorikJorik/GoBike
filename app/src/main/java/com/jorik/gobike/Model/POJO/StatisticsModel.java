package com.jorik.gobike.Model.POJO;

import com.jorik.gobike.Network.DTO.EveryDayProfileStatisticsDTO;
import java.util.List;

public class StatisticsModel {
  private List<TotalStatisticsModel> totalStatistics;
  private List<EveryDayProfileStatisticsDTO> everyDayStatistics;

  public StatisticsModel(
      List<TotalStatisticsModel> totalStatistics,
      List<EveryDayProfileStatisticsDTO> everyDayStatistics) {
    this.totalStatistics = totalStatistics;
    this.everyDayStatistics = everyDayStatistics;
  }

  public List<TotalStatisticsModel> getTotalStatistics() {
    return totalStatistics;
  }

  public void setTotalStatistics(
      List<TotalStatisticsModel> totalStatistics) {
    this.totalStatistics = totalStatistics;
  }

  public List<EveryDayProfileStatisticsDTO> getEveryDayStatistics() {
    return everyDayStatistics;
  }

  public void setEveryDayStatistics(
      List<EveryDayProfileStatisticsDTO> everyDayStatistics) {
    this.everyDayStatistics = everyDayStatistics;
  }
}
