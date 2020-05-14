package io.github.palprz.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;

public class NotificationInfo {

    public static void show(String text) {
        Div content = new Div();
        content.setText(text);
        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.BOTTOM_END);
        notification.open();
    }
}
