package io.github.palprz.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.github.palprz.builders.DOMBuilder;
import io.github.palprz.components.MoneyContainer;
import io.github.palprz.domain.MoneyLogic;
import io.github.palprz.storage.Storage;

import javax.inject.Inject;
import java.util.Timer;

@Route("counter")
@CssImport("./styles/style.css")
public class MoneyView extends VerticalLayout {

    @Inject
    public DOMBuilder domBuilder;

    public Div init(UI ui, Timer timer) {
        MoneyContainer moneyContainer = domBuilder.populateMoneyContainer();
        Div counterContainer = domBuilder.populateCounterContainer(moneyContainer);

        // Init money and run task to count them
        Storage storage = Storage.get();
        int initialMoney = storage.getConfig().getInitialAvailableMoney();
        storage.setAvailableMoney(initialMoney);

        timer.schedule(new MoneyLogic(ui, moneyContainer), 0, 1000);

        return counterContainer;
    }


}
