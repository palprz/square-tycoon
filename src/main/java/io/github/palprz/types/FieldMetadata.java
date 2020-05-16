package io.github.palprz.types;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class FieldMetadata {

    /**
     * Row of the position of the field.
     */
    private int row;
    /**
     * Column of the position of the field.
     */
    private int column;
    /**
     * Terrain type to define background of the UI field and possible productions.
     */
    private TerrainEnum type = TerrainEnum.UNKNOWN;
    /**
     * Current set production of the field.
     */
    private ProductionEnum production = ProductionEnum.NONE;
    /**
     * Define if this field is visible for end-user. and not available to buy it
     */
    private boolean visible = false;
    /**
     * Define if this field is available to buy.
     */
    private boolean dimmed = true;
    /**
     * The cost of the field
     */
    private int fieldPrice;
    /**
     * Define if the field is connected with any market next to this field.
     */
    private boolean marketConnected;
    /**
     * Define if all require products have been provided by next fields' productions.
     */
    private boolean hasRequireProducts;
    /**
     * List with already provided products by next fields' productions.
     */
    private List<ProductEnum> providedProducts = new ArrayList<>();

    public FieldMetadata() {
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public TerrainEnum getType() {
        return type;
    }

    public void setType(TerrainEnum type) {
        this.type = type;
    }

    public ProductionEnum getProduction() {
        return production;
    }

    public void setProduction(ProductionEnum production) {
        this.production = production;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isDimmed() {
        return dimmed;
    }

    public void setDimmed(boolean dimmed) {
        this.dimmed = dimmed;
    }

    public boolean isMarketConnected() {
        return marketConnected;
    }

    public void setMarketConnected(boolean marketConnected) {
        this.marketConnected = marketConnected;
    }

    public boolean isHasRequireProducts() {
        return hasRequireProducts;
    }

    public void setHasRequireProducts(boolean hasRequireProducts) {
        this.hasRequireProducts = hasRequireProducts;
    }

    public int getFieldPrice() {
        return fieldPrice;
    }

    public void setFieldPrice(int fieldPrice) {
        this.fieldPrice = fieldPrice;
    }

    public List<ProductEnum> getProvidedProducts() {
        return providedProducts;
    }

    public void setProvidedProducts(List<ProductEnum> providedProducts) {
        this.providedProducts = providedProducts;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FieldMetadata.class.getSimpleName() + "[", "]")
                .add("row=" + row)
                .add("column=" + column)
                .add("type=" + type)
                .add("production=" + production)
                .add("visible=" + visible)
                .add("dimmed=" + dimmed)
                .add("fieldPrice=" + fieldPrice)
                .add("marketConnected=" + marketConnected)
                .add("hasRequireProducts=" + hasRequireProducts)
                .add("providedProducts=" + providedProducts)
                .toString();
    }
}
