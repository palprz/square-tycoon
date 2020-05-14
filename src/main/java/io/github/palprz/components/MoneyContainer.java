package io.github.palprz.components;

import com.vaadin.flow.component.html.Label;

public class MoneyContainer {

    private Label current;
    // profit - expense = income
    private Label profit;
    private Label income;
    private Label expense;

    public MoneyContainer(Label current, Label profit, Label income, Label expense) {
        this.current = current;
        this.profit = profit;
        this.income = income;
        this.expense = expense;
    }

    public Label getIncome() {
        return income;
    }

    public Label getProfit() {
        return profit;
    }

    public Label getExpense() {
        return expense;
    }

    public Label getCurrent() {
        return current;
    }

}
