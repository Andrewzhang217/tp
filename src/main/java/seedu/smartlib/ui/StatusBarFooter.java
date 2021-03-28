package seedu.smartlib.ui;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * A UI for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {

    private static final String FXML = "StatusBarFooter.fxml";

    @FXML
    private Label saveLocationStatus;

    /**
     * Creates a {@code StatusBarFooter} with the given {@code Path}.
     *
     * @param saveLocation the path of the StatusBarFooter.
     */
    public StatusBarFooter(Path saveLocation) {
        super(FXML);
        saveLocationStatus.setText(Paths.get(".").resolve(saveLocation).toString());
    }

}
