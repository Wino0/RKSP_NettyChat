package Client.netty.cha;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable { //
    private  Network network;

    @FXML
    TextField messageField;
    @FXML
    TextArea MainArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            network = new Network((args -> {
                MainArea.appendText((String)args[0]);
            }));
    }

    public void sendMassegeAction(ActionEvent actionEvent) { //
        network.sendAmassage(messageField.getText()); // в сеть отпраляем сообщение из поля сообщений
        messageField.clear(); // после отправки очищаем поле сообщений
        messageField.requestFocus(); // уст фокус на поле сообщений
    }
}
