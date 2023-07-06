package com.amazon.ata.types;

import java.math.BigDecimal;

public class PolyBag extends Packaging {
    private BigDecimal volume;

    /**
     * Instantiates a new Packaging object.
     *
     * @param material - the Material of the package
     */
    public PolyBag(Material material, BigDecimal volume) {
        super(material);
        this.volume = volume;
    }

    /**
     * Returns whether the given item will fit in this packaging.
     *
     * @param item the item to test fit for
     * @return whether the item will fit in this packaging
     */
    public boolean canFitItem(Item item) {
        BigDecimal itemVolume = item.getLength().multiply(item.getWidth()).multiply(item.getHeight());
        return this.volume.compareTo(itemVolume) > 0;
    }

    /**
     * Returns the mass of the packaging in grams.
     * @return the mass of the packaging
     */
    public BigDecimal getMass() {
        double mass = Math.ceil(Math.sqrt(volume.doubleValue()) * 0.6);
        return BigDecimal.valueOf(mass);
    }
}
