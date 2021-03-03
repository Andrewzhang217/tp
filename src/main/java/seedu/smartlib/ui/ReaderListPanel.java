package seedu.smartlib.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.smartlib.commons.core.LogsCenter;
import seedu.smartlib.model.reader.Reader;

/**
 * Panel containing the list of persons.
 */
public class ReaderListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ReaderListPanel.class);

    @FXML
    private ListView<Reader> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public ReaderListPanel(ObservableList<Reader> readerList) {
        super(FXML);
        personListView.setItems(readerList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Reader> {
        @Override
        protected void updateItem(Reader reader, boolean empty) {
            super.updateItem(reader, empty);

            if (empty || reader == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ReaderCard(reader, getIndex() + 1).getRoot());
            }
        }
    }

}
