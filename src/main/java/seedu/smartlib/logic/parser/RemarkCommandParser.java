package seedu.smartlib.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.smartlib.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.smartlib.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.smartlib.commons.core.index.Index;
import seedu.smartlib.commons.exceptions.IllegalValueException;
import seedu.smartlib.logic.commands.RemarkCommand;
import seedu.smartlib.logic.parser.exceptions.ParseException;
import seedu.smartlib.model.reader.Remark;
public class RemarkCommandParser implements Parser<RemarkCommand>{

    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE), ive);
        }

        String remark = argMultimap.getValue(PREFIX_REMARK).orElse("");

        return new RemarkCommand(index, new Remark(remark));
    }

}
