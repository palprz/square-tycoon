package io.github.palprz.types;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class FieldMetadata {

    private int row;
    private int column;
    private TerrainEnum type = TerrainEnum.UNKNOWN;
    private ProductionEnum production = ProductionEnum.NONE;
    private boolean visible = false;
    private boolean dimmed = true;
    private int fieldPrice;
    private boolean marketConnected;
    private boolean hasRequireProducts;
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
