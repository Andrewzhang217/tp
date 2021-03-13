package seedu.smartlib.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.smartlib.model.reader.Reader;
import seedu.smartlib.model.reader.UniqueReaderList;

/**
 * Wraps all data at the SmartLib level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class SmartLib implements ReadOnlySmartLib {

    private final UniqueReaderList readers;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        readers = new UniqueReaderList();
    }

    public SmartLib() {}

    /**
     * Creates a SmartLib using the Readers in the {@code toBeCopied}
     */
    public SmartLib(ReadOnlySmartLib toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the reader list with {@code readers}.
     * {@code readers} must not contain duplicate readers.
     */
    public void setReaders(List<Reader> readers) {
        this.readers.setReaders(readers);
    }

    /**
     * Resets the existing data of this {@code SmartLib} with {@code newData}.
     */
    public void resetData(ReadOnlySmartLib newData) {
        requireNonNull(newData);

        setReaders(newData.getReaderList());
    }

    //// reader-level operations

    /**
     * Returns true if a reader with the same identity as {@code reader} exists in the registered reader base.
     */
    public boolean hasReader(Reader reader) {
        requireNonNull(reader);
        return readers.contains(reader);
    }

    /**
     * Adds a reader to the registered reader base.
     * The reader must not already exist in the registered reader base.
     */
    public void addReader(Reader p) {
        readers.addReader(p);
    }

    /**
     * Replaces the given reader {@code target} in the list with {@code editedReader}.
     * {@code target} must exist in SmartLib.
     * The reader identity of {@code editedReader} must not be the same as another existing reader in SmartLib.
     */
    public void setReader(Reader target, Reader editedReader) {
        requireNonNull(editedReader);

        readers.setReader(target, editedReader);
    }

    /**
     * Removes {@code key} from this {@code SmartLib}.
     * {@code key} must exist in the SmartLib registered reader base.
     */
    public void removeReader(Reader key) {
        readers.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return readers.asUnmodifiableObservableList().size() + " readers";
        // TODO: refine later
    }

    @Override
    public ObservableList<Reader> getReaderList() {
        return readers.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SmartLib // instanceof handles nulls
                && readers.equals(((SmartLib) other).readers));
    }

    @Override
    public int hashCode() {
        return readers.hashCode();
    }
}
