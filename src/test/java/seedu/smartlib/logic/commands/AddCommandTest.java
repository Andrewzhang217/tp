package seedu.smartlib.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.smartlib.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.smartlib.commons.core.GuiSettings;
import seedu.smartlib.logic.commands.exceptions.CommandException;
import seedu.smartlib.model.Model;
import seedu.smartlib.model.ReadOnlySmartLib;
import seedu.smartlib.model.ReadOnlyUserPrefs;
import seedu.smartlib.model.SmartLib;
import seedu.smartlib.model.reader.Reader;
import seedu.smartlib.testutil.PersonBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Reader validReader = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validReader).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validReader), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validReader), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Reader validReader = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validReader);
        ModelStub modelStub = new ModelStubWithPerson(validReader);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Reader alice = new PersonBuilder().withName("Alice").build();
        Reader bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Reader reader) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSmartLib(ReadOnlySmartLib newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlySmartLib getSmartLib() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Reader reader) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Reader target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Reader target, Reader editedReader) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Reader> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Reader> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Reader reader;

        ModelStubWithPerson(Reader reader) {
            requireNonNull(reader);
            this.reader = reader;
        }

        @Override
        public boolean hasPerson(Reader reader) {
            requireNonNull(reader);
            return this.reader.isSamePerson(reader);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Reader> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Reader reader) {
            requireNonNull(reader);
            return personsAdded.stream().anyMatch(reader::isSamePerson);
        }

        @Override
        public void addPerson(Reader reader) {
            requireNonNull(reader);
            personsAdded.add(reader);
        }

        @Override
        public ReadOnlySmartLib getSmartLib() {
            return new SmartLib();
        }
    }

}
