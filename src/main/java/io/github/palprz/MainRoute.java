package io.github.palprz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.communication.PushMode;
import io.github.palprz.storage.config.Config;
import io.github.palprz.storage.Storage;
import io.github.palprz.views.BoardView;
import io.github.palprz.views.MoneyView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

@Push(PushMode.MANUAL)
@Route("")
@CssImport("./styles/style.css")
public class MainRoute extends FlexLayout implements HasDynamicTitle{

    private final Logger LOG = LoggerFactory.getLogger(MainRoute.class);

    static {
        SLF4JBridgeHandler.install();
    }

    // Just for debugging to see when the page has been generated
    private String title = "";

    @Override
    public String getPageTitle() {
        return title;
    }

    @Inject
    private BoardView boardView;

    @Inject
    private MoneyView moneyView;

    private Timer timer = null;

    @PostConstruct
    public void init() throws IOException {
        Storage storage = Storage.get();
        if (storage.getConfig() == null) {
            storage.setConfig(this.initConfig());
        }
    }

    private Config initConfig() throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream("config/config.yaml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(input, Config.class);
        } catch (IOException e) {
            LOG.warn("Problem to map config - return empty object: {}", e);
            return new Config();
        } finally {
            input.close();
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Just for debugging to see when the page has been generated
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        title = formatter.format(new Date());

        Div boardContainer = boardView.init();
        add(boardContainer);

        timer = new Timer();
        Div moneyContainer = moneyView.init(attachEvent.getUI(), timer);
        add(moneyContainer);


        Button regenerateBoardBtn = new Button("Generate new board");
        regenerateBoardBtn.addClassName("additional-btn");
        regenerateBoardBtn.addClickListener(e -> generateNewBoard());

        Div buttonsContainer = new Div();
        buttonsContainer.add(regenerateBoardBtn);

        add(buttonsContainer);
    }

    protected void generateNewBoard() {
        // Cleanup timer
        LOG.debug("Kill timer");
        timer.cancel();
        timer = null;
        LOG.info("Reload page");
        Storage.get().setBoard(null);
        UI.getCurrent().getPage().reload();
    }

}
