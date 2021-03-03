package seedu.smartlib.logic.commands;

import static seedu.smartlib.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.smartlib.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.smartlib.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.smartlib.model.Model;
import seedu.smartlib.model.ModelManager;
import seedu.smartlib.model.UserPrefs;
import seedu.smartlib.model.reader.Reader;
import seedu.smartlib.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Reader validReader = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getSmartLib(), new UserPrefs());
        expectedModel.addPerson(validReader);

        assertCommandSuccess(new AddCommand(validReader), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validReader), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Reader readerInList = model.getSmartLib().getPersonList().get(0);
        assertCommandFailure(new AddCommand(readerInList), model, AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
