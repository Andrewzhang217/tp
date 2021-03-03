package seedu.smartlib.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.smartlib.model.Model;
import seedu.smartlib.model.SmartLib;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setSmartLib(new SmartLib());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
