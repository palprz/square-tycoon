package io.github.palprz.builders;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import io.github.palprz.components.MoneyContainer;
import io.github.palprz.domain.FieldLogic;
import io.github.palprz.types.*;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DOMBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(DOMBuilder.class);

    @Inject
    private FieldLogic fieldLogic;

    private DialogBuilder dialogBuilder = new DialogBuilder();

    public Div createVisibleFieldElement(Map<Position, Field> board, Position position, TerrainEnum terrain) {
        Div div = new Div();
        div.addClassNames("general-border", "field", terrain.getName());
        div.getStyle().set("grid-column", String.valueOf(position.getColumn()));
        div.getStyle().set("grid-row", String.valueOf(position.getRow()));
        Div title = new Div();
        title.addClassNames("field-title");
        div.add(title);

        div.addClickListener(e -> dialogBuilder.createDialog(board, position));
        return div;
    }

    class DialogBuilder {

        private void createDialog(Map<Position, Field> board, Position position) {
            Field field = board.get(position);

            if (!field.getFieldMetadata().isVisible()) {
                // no action for not visible fields
                return;
            }

            if (field.getFieldMetadata().isDimmed()) {
                // only possible to buy field
                this.createBuyDialog(field);
                return;
            }

            // own field - can provide production
            this.createProductionDialog(field);
        }

        private void createBuyDialog(Field field) {
            Dialog dialog = new Dialog();
            Div dialogContainer = new Div();
            dialogContainer.addClassName("dialog-container");
            dialogContainer.add(new Label("Buy this field"));
            dialogContainer.add(new HtmlComponent("br"));

            NativeButton btn = new NativeButton("Accept", event -> {
                dialog.close();
                fieldLogic.buyField(field);
            });

            Div buttonsContainer = new Div();
            buttonsContainer.add(btn);
            buttonsContainer.addClassName("choose-container");

            buttonsContainer.add(new Label("Cost: $" + field.getFieldMetadata().getFieldPrice()));
            dialogContainer.add(buttonsContainer);

            dialog.add(dialogContainer);
            dialog.open();
        }

        private void createProductionDialog(Field field) {
            TerrainEnum terrain = field.getFieldMetadata().getType();

            List<ProductionEnum> productions = new ArrayList<>();
            switch (terrain) {
                case WATER:
                    productions.add(ProductionEnum.WATER);
                    break;
                case PLAIN:
                    productions.add(ProductionEnum.WHEAT);
                    productions.add(ProductionEnum.COW);
                    productions.add(ProductionEnum.BUTTER);
                    productions.add(ProductionEnum.IRON_BAR);
                    productions.add(ProductionEnum.FURNITURE);
                    productions.add(ProductionEnum.MARKET);
                    break;
                case FOREST:
                    productions.add(ProductionEnum.WOOD);
                    break;
                case MOUNTAIN:
                    productions.add(ProductionEnum.COAL);
                    productions.add(ProductionEnum.IRON);
                    break;
            }

            // Always possible to remove option from any type of the field
            productions.add(ProductionEnum.NONE);

            this.createProductionOptionButtons(field, productions);
        }

        private void createProductionOptionButtons(Field field, List<ProductionEnum> productions) {
            Dialog dialog = new Dialog();
            Div dialogContainer = new Div();
            dialogContainer.addClassName("dialog-container");
            dialogContainer.add(new Label("Choose production:"));
            dialogContainer.add(new HtmlComponent("br"));

            for (ProductionEnum production : productions) {
                NativeButton btn = new NativeButton(production.getDisplayName(), event -> {
                    dialog.close();
                    fieldLogic.provideProductionToField(field, production);
                });

                Div buttonsContainer = new Div();
                buttonsContainer.add(btn);
                buttonsContainer.add(new Label("Income: $" + production.getIncome()));
                buttonsContainer.add(new Label("Cost: $" + production.getCost()));
                buttonsContainer.addClassName("choose-container");

                dialogContainer.add(buttonsContainer);
            }

            dialog.add(dialogContainer);
            dialog.open();
        }
    }

    public MoneyContainer populateMoneyContainer() {
        Label current = new Label();
        current.addClassName("money-label-div");
        Label profit = new Label();
        profit.addClassNames("money-label-div", "green");
        Label income = new Label();
        income.addClassNames("money-label-div", "yellow");
        Label expense = new Label();
        expense.addClassNames("money-label-div", "red");

        return new MoneyContainer(current, profit, income, expense);
    }

    public Div populateCounterContainer(MoneyContainer moneyContainer) {
        Div container = new Div();
        container.addClassName("money-container");

        container.add(moneyContainer.getCurrent());
        container.add(moneyContainer.getProfit());
        container.add(moneyContainer.getIncome());
        container.add(moneyContainer.getExpense());

        return container;
    }
}
