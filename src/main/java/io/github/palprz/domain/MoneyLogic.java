package io.github.palprz.domain;

import com.vaadin.flow.component.UI;
import io.github.palprz.components.MoneyContainer;
import io.github.palprz.storage.Storage;
import io.github.palprz.types.Field;
import io.github.palprz.types.FieldMetadata;
import io.github.palprz.types.Position;
import io.github.palprz.types.ProductionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TimerTask;

public class MoneyLogic extends TimerTask {

    private final Logger LOG = LoggerFactory.getLogger(MoneyLogic.class);

    private UI ui;
    private MoneyContainer uiContainer;

    private int availableMoney;
    private int profit;
    private int income;
    private int expense;

    public MoneyLogic(UI ui, MoneyContainer uiContainer) {
        this.ui = ui;
        this.uiContainer = uiContainer;
    }

    @Override
    public void run() {
        LOG.debug("Counting... availableMoney before counting: {}", availableMoney);
        Map<Position, Field> board = Storage.get().getBoard();
        availableMoney = Storage.get().getAvailableMoney();
        profit = 0;
        income = 0;
        expense = 0;
        for (Map.Entry<Position, Field> entry : board.entrySet()) {
            FieldMetadata fieldMeta = entry.getValue().getFieldMetadata();
            ProductionEnum production = fieldMeta.getProduction();
            // If it's set
            if (production != ProductionEnum.NONE) {
                // field can produce products when has got all necessary products and is connected to the market
                if (fieldMeta.isHasRequireProducts()) {
//                    if (fieldMeta.isHasRequireProducts() && fieldMeta.isMarketConnected()) {
                    income = income + production.getIncome();
                }
                expense = expense + production.getCost();
                availableMoney = availableMoney + production.getIncome() - production.getCost();
            }
        }

        // Quick math
        profit = income - expense;

        ui.access(() -> {
            uiContainer.getCurrent().setText("Money: $" + availableMoney);
            uiContainer.getProfit().setText("Profit: $" + profit);
            uiContainer.getIncome().setText("Income: $" + income);
            uiContainer.getExpense().setText("Expense: $" + expense);
            ui.push();
        });

        Storage.get().setAvailableMoney(availableMoney);

        LOG.debug("Counted: availableMoney after counting: {}", availableMoney);
    }
}
