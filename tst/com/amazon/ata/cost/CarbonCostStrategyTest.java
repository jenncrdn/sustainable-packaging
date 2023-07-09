package com.amazon.ata.cost;

import com.amazon.ata.types.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarbonCostStrategyTest {
    private Packaging BOX_5x5x10 = new Box(Material.CORRUGATE, BigDecimal.valueOf(5), BigDecimal.valueOf(5), BigDecimal.valueOf(10));
    private Packaging POLYBAG_2Kcc = new PolyBag(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(2000));
    private CarbonCostStrategy strategy = new CarbonCostStrategy();

    @Test
    void getCost_corrugateMaterial_returnsCorrectCarbonCost() {
        //GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(BOX_5x5x10)
                .build();

        //WHEN
        ShipmentCost shipmentCost = strategy.getCost(option);

        //THEN
        assertTrue(BigDecimal.valueOf(4.25).compareTo(shipmentCost.getCost()) == 0,
                "Incorrect carbon cost calculation for a box with dimensions 5x5x10.");
    }
    @Test
    void getCost_laminatedPlasticMaterial_returnsCorrectCarbonCost() {
        //GIVEN
        ShipmentOption option = ShipmentOption.builder()
                .withPackaging(POLYBAG_2Kcc)
                .build();

        //WHEN
        ShipmentCost shipmentCost = strategy.getCost(option);

        //THEN
        assertTrue(BigDecimal.valueOf(0.324).compareTo(shipmentCost.getCost()) == 0,
                "Incorrect carbon cost calculation for a polybag with volume of 2000cc.");
    }

}
