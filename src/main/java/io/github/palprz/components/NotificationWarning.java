package io.github.palprz.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;

public class NotificationWarning {

    public static void show(String text) {
        Div content = new Div();
        content.setText(text);
        content.addClassName("red");
        Notification notification = new Notification(content);
        notification.setDuration(5000);
        notification.setPosition(Notification.Position.BOTTOM_END);
        notification.open();
    }
}
