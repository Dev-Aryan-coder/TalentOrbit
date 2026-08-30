package com.example.TalentOrbit.dto.response;

import java.math.BigDecimal;

public class PlacementsSummaryResponseDTO {
    private Integer totalOffers;
    private Double placementRate;
    private BigDecimal averageCtc;
    private BigDecimal highestCtc;

    public PlacementsSummaryResponseDTO() {
        this.totalOffers = 0;
        this.placementRate = 0.0;
        this.averageCtc = BigDecimal.ZERO;
        this.highestCtc = BigDecimal.ZERO;
    }

    public PlacementsSummaryResponseDTO(Integer totalOffers, Double placementRate, BigDecimal averageCtc, BigDecimal highestCtc) {
        this.totalOffers = totalOffers != null ? totalOffers : 0;
        this.placementRate = placementRate != null ? placementRate : 0.0;
        this.averageCtc = averageCtc != null ? averageCtc : BigDecimal.ZERO;
        this.highestCtc = highestCtc != null ? highestCtc : BigDecimal.ZERO;
    }

    public Integer getTotalOffers() { return totalOffers; }
    public void setTotalOffers(Integer totalOffers) { this.totalOffers = totalOffers; }

    public Double getPlacementRate() { return placementRate; }
    public void setPlacementRate(Double placementRate) { this.placementRate = placementRate; }

    public BigDecimal getAverageCtc() { return averageCtc; }
    public void setAverageCtc(BigDecimal averageCtc) { this.averageCtc = averageCtc; }

    public BigDecimal getHighestCtc() { return highestCtc; }
    public void setHighestCtc(BigDecimal highestCtc) { this.highestCtc = highestCtc; }
}
