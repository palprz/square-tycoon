package io.github.palprz.types;

import java.util.Arrays;
import java.util.List;

public enum ProductionEnum {

    WATER(
            "Water",
            5,
            0,
            Arrays.asList(),
            Arrays.asList(ProductEnum.WATER)),
    WHEAT(
            "Wheat",
            10,
            50,
            Arrays.asList(ProductEnum.WATER),
            Arrays.asList(ProductEnum.HAY)),
    COW(
            "Cow",
            15,
            100,
            Arrays.asList(ProductEnum.WATER, ProductEnum.HAY),
            Arrays.asList(ProductEnum.MEAT, ProductEnum.MILK, ProductEnum.SKIN)),
    BUTTER(
            "Butter",
            10,
            150,
            Arrays.asList(ProductEnum.MILK),
            Arrays.asList(ProductEnum.BUTTER)),
    COAL(
            "Coal",
            20,
            0,
            Arrays.asList(ProductEnum.MEAT),
            Arrays.asList(ProductEnum.COAL)),
    IRON(
            "Iron",
            20,
            0,
            Arrays.asList(ProductEnum.MEAT),
            Arrays.asList(ProductEnum.IRON)),
    IRON_BAR(
            "Iron bar",
            50,
            500,
            Arrays.asList(ProductEnum.COAL, ProductEnum.IRON),
            Arrays.asList(ProductEnum.IRON_BAR)),
    WOOD(
            "Wood",
            15,
            0,
            Arrays.asList(),
            Arrays.asList(ProductEnum.WOOD)),
    FURNITURE(
            "Furniture",
            30,
            250,
            Arrays.asList(ProductEnum.SKIN, ProductEnum.WOOD),
            Arrays.asList(ProductEnum.FURNITURE)),
    MARKET(
            "Market",
            10,
            0,
            Arrays.asList(),
            Arrays.asList()),
    NONE(
            "None",
            0,
            0,
            Arrays.asList(),
            Arrays.asList());

    private String displayName;
    private int cost;
    private int income;
    private List<ProductEnum> requireProducts;
    private List<ProductEnum> provideProducts;

    ProductionEnum(String displayName, int cost, int income, List<ProductEnum> requireProducts, List<ProductEnum> provideProducts) {
        this.displayName = displayName;
        this.cost = cost;
        this.income = income;
        this.requireProducts = requireProducts;
        this.provideProducts = provideProducts;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.displayName.toLowerCase();
    }

    public int getCost() {
        return cost;
    }

    public int getIncome() {
        return income;
    }

    public List<ProductEnum> getRequireProducts() {
        return requireProducts;
    }

    public void setRequireProducts(List<ProductEnum> requireProducts) {
        this.requireProducts = requireProducts;
    }

    public List<ProductEnum> getProvideProducts() {
        return provideProducts;
    }

    public void setProvideProducts(List<ProductEnum> provideProducts) {
        this.provideProducts = provideProducts;
    }
}
