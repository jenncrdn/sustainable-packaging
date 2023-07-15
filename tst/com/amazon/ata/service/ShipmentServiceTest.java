package com.amazon.ata.service;

import com.amazon.ata.cost.MonetaryCostStrategy;
import com.amazon.ata.dao.PackagingDAO;
import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class ShipmentServiceTest {

    private Item smallItem = Item.builder()
            .withHeight(BigDecimal.valueOf(1))
            .withWidth(BigDecimal.valueOf(1))
            .withLength(BigDecimal.valueOf(1))
            .withAsin("abcde")
            .build();

    private Item largeItem = Item.builder()
            .withHeight(BigDecimal.valueOf(1000))
            .withWidth(BigDecimal.valueOf(1000))
            .withLength(BigDecimal.valueOf(1000))
            .withAsin("12345")
            .build();

    private FulfillmentCenter existentFC = new FulfillmentCenter("ABE2");
    private FulfillmentCenter nonExistentFC = new FulfillmentCenter("NonExistentFC");
    private List<ShipmentOption> shipmentOptionList = new ArrayList<>();
    private ShipmentOption shipmentOption;
    private Packaging smallPackaging = new Box(Material.CORRUGATE, BigDecimal.valueOf(2), BigDecimal.valueOf(2), BigDecimal.valueOf(2));
    @Mock
    private PackagingDAO packagingDAO;
    @InjectMocks
    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        shipmentService = new ShipmentService(packagingDAO,
                new MonetaryCostStrategy());
    }

    @Test
    void findBestShipmentOption_existentFCAndItemCanFit_returnsShipmentOption() throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        // GIVEN & WHEN
        shipmentOption = ShipmentOption.builder()
                .withItem(smallItem)
                .withPackaging(smallPackaging)
                .withFulfillmentCenter(existentFC)
                .build();
        shipmentOptionList.add(shipmentOption);
        when(packagingDAO.findShipmentOptions(smallItem, existentFC)).thenReturn(shipmentOptionList);
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(smallItem, existentFC);

        // THEN
        assertNotNull(shipmentOption);
    }

    @Test
    void findBestShipmentOption_existentFCAndItemCannotFit_returnsShipmentOption() throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        // GIVEN & WHEN
        shipmentOption = ShipmentOption.builder()
                .withItem(largeItem)
                .withPackaging(smallPackaging)
                .withFulfillmentCenter(existentFC)
                .build();
        shipmentOptionList.add(shipmentOption);
        when(packagingDAO.findShipmentOptions(largeItem, existentFC)).thenThrow(NoPackagingFitsItemException.class);
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, existentFC);

        // THEN
        assertNull(shipmentOption.getPackaging());
    }

    @Test
    void findBestShipmentOption_nonExistentFCAndItemCanFit_returnsShipmentOption() throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        // GIVEN & WHEN
        shipmentOption = ShipmentOption.builder()
                .withItem(smallItem)
                .withPackaging(smallPackaging)
                .withFulfillmentCenter(nonExistentFC)
                .build();
        shipmentOptionList.add(shipmentOption);
        when(packagingDAO.findShipmentOptions(smallItem, nonExistentFC)).thenThrow(UnknownFulfillmentCenterException.class);
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(smallItem, nonExistentFC);

        // THEN
        assertNull(shipmentOption);
    }

    @Test
    void findBestShipmentOption_nonExistentFCAndItemCannotFit_returnsShipmentOption() throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        // GIVEN & WHEN
        shipmentOption = ShipmentOption.builder()
                .withItem(largeItem)
                .withPackaging(smallPackaging)
                .withFulfillmentCenter(nonExistentFC)
                .build();
        shipmentOptionList.add(shipmentOption);
        when(packagingDAO.findShipmentOptions(largeItem, nonExistentFC)).thenThrow(UnknownFulfillmentCenterException.class);
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, nonExistentFC);

        // THEN
        assertNull(shipmentOption);
    }

    @Test
    public void findShipmentOptions_packagingDoesntFit_throwsNoPackagingFitsItemException() throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        // GIVEN & WHEN
        shipmentOption = ShipmentOption.builder()
                .withItem(largeItem)
                .withPackaging(smallPackaging)
                .withFulfillmentCenter(existentFC)
                .build();
        shipmentOptionList.add(shipmentOption);
        when(packagingDAO.findShipmentOptions(largeItem, existentFC)).thenThrow(NoPackagingFitsItemException.class);
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, existentFC);

        // THEN
        assertNull(shipmentOption.getPackaging());
    }
}