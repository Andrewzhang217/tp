package seedu.smartlib.model.record;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Objects.requireNonNull;
import static seedu.smartlib.commons.util.AppUtil.checkArgument;
import static seedu.smartlib.model.SmartLib.DURATION;

import java.time.LocalDateTime;

/**
 * The DateBorrowed class takes note of the date which a book is borrowed from SmartLib.
 */
public class DateBorrowed {

    public static final String MESSAGE_CONSTRAINTS = "Date should be of the format yyyy-mm-dd ";
    public static final String VALIDATION_REGEX = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";

    private final String value;

    /**
     * Constructs an {@code DateBorrowed}.
     *
     * @param date A valid date.
     */
    public DateBorrowed(LocalDateTime date) {
        requireNonNull(date);
        //checkArgument(isValidDate(date.toString()), MESSAGE_CONSTRAINTS);
        value = date.toString();
    }

    /**
     * Constructs an {@code DateBorrowed}.
     *
     * @param date A valid date.
     */
    public DateBorrowed(String date) {
        requireNonNull(date);
        //checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        value = date;
    }

    /**
     * Indicates whether the book associated with a record is overdue.
     *
     * @return true if the book is overdue, and false otherwise.
     */
    public boolean isOverdue() {
        return DAYS.between(LocalDateTime.parse(this.value), LocalDateTime.now()) > DURATION;
    }

    /**
     * Indicates whether a given string is a valid date.
     *
     * @param test string to be tested.
     * @return true if a given string is a valid date, and false otherwise.
     */
    public static boolean isValidDate(String test) {
        return test.toString().matches(VALIDATION_REGEX);
    }

    /**
     * Returns this borrow date in String format.
     *
     * @return this borrow date in String format.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Checks if this borrow date is equal to another borrow date.
     *
     * @param other the other borrow date to be compared.
     * @return true if this borrow date is equal to the other borrow date, and false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateBorrowed // instanceof handles nulls
                && value.equals(((DateBorrowed) other).value)); // state check
    }

    /**
     * Generates a hashcode for this borrow date.
     *
     * @return the hashcode for this borrow date.
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
