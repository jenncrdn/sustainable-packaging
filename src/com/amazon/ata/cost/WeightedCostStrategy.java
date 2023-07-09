package com.amazon.ata.cost;

import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;

/**
 * A strategy to calculate the cost of a ShipmentOption based on weighted options, 80% Monetary, 20% Carbon.
 */
public class WeightedCostStrategy implements CostStrategy {
    private MonetaryCostStrategy monetaryCostStrategy;
    private CarbonCostStrategy carbonCostStrategy;
    private BigDecimal monetaryWeight = new BigDecimal(0.8);
    private BigDecimal carbonWeight = new BigDecimal(0.2);

    public WeightedCostStrategy(MonetaryCostStrategy monetaryCostStrategy, CarbonCostStrategy carbonCostStrategy) {
        this.monetaryCostStrategy = monetaryCostStrategy;
        this.carbonCostStrategy = carbonCostStrategy;
    }

    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        ShipmentCost monetaryCost = monetaryCostStrategy.getCost(shipmentOption);
        ShipmentCost carbonCost = carbonCostStrategy.getCost(shipmentOption);

        BigDecimal weightedMonetaryCost = monetaryCost.getCost().multiply(monetaryWeight);
        BigDecimal weightedCarbonCost = carbonCost.getCost().multiply(carbonWeight);

        BigDecimal cost = weightedMonetaryCost.add(weightedCarbonCost);

        return new ShipmentCost(shipmentOption, cost);
    }
}
