package seedu.smartlib.model;

import static java.util.Objects.requireNonNull;
import static seedu.smartlib.commons.util.CollectionUtil.requireAllNonNull;

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.smartlib.commons.core.GuiSettings;
import seedu.smartlib.commons.core.LogsCenter;
import seedu.smartlib.commons.core.name.Name;
import seedu.smartlib.model.book.Barcode;
import seedu.smartlib.model.book.Book;
import seedu.smartlib.model.reader.Reader;
import seedu.smartlib.model.record.Record;

/**
 * Represents the in-memory model of SmartLib's data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final SmartLib smartLib;
    private final UserPrefs userPrefs;
    private final FilteredList<Book> filteredBooks;
    private final FilteredList<Reader> filteredReaders;
    private final FilteredList<Record> filteredRecords;

    /**
     * Initializes a ModelManager with the given SmartLib and userPrefs.
     */
    public ModelManager(ReadOnlySmartLib smartLib, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(smartLib, userPrefs);

        logger.fine("Initializing with SmartLib: " + smartLib + " and user prefs " + userPrefs);

        this.smartLib = new SmartLib(smartLib);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredBooks = new FilteredList<>(this.smartLib.getBookList());
        filteredReaders = new FilteredList<>(this.smartLib.getReaderList());
        filteredRecords = new FilteredList<>(this.smartLib.getRecordList());
    }

    public ModelManager() {
        this(new SmartLib(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getSmartLibFilePath() {
        return userPrefs.getSmartLibFilePath();
    }

    @Override
    public void setSmartLibFilePath(Path smartLibFilePath) {
        requireNonNull(smartLibFilePath);
        userPrefs.setSmartLibFilePath(smartLibFilePath);
    }

    //=========== SmartLib ================================================================================

    @Override
    public void setSmartLib(ReadOnlySmartLib smartLib) {
        this.smartLib.resetData(smartLib);
    }

    @Override
    public ReadOnlySmartLib getSmartLib() {
        return smartLib;
    }

    @Override
    public boolean hasBook(Book book) {
        requireNonNull(book);
        return smartLib.hasBook(book);
    }

    @Override
    public boolean hasBook(Name bookName) {
        requireAllNonNull(bookName);
        return smartLib.hasBook(bookName);
    }

    @Override
    public boolean hasBookWithBarcode(Barcode barcode) {
        requireNonNull(barcode);
        assert(Barcode.isValidBarcode(barcode.getValue()));
        return smartLib.hasBookWithBarcode(barcode);
    }

    @Override
    public boolean isBookWithBarcodeBorrowed(Barcode barcode) {
        return smartLib.isBookWithBarcodeBorrowed(barcode);
    }

    @Override
    public boolean hasReader(Reader reader) {
        requireNonNull(reader);
        return smartLib.hasReader(reader);
    }

    @Override
    public boolean hasReader(Name readerName) {
        requireAllNonNull(readerName);
        return smartLib.hasReader(readerName);
    }

    @Override
    public boolean canReaderBorrow(Name readerName) {
        requireAllNonNull(readerName);
        return smartLib.canReaderBorrow(readerName);
    }

    @Override
    public boolean hasRecord(Record record) {
        requireNonNull(record);
        return smartLib.hasRecord(record);
    }

    @Override
    public boolean borrowBook(Name readerName, Barcode barcode) {
        requireAllNonNull(barcode, readerName);
        boolean status = smartLib.borrowBook(readerName, barcode);
        updateFilteredReaderList(PREDICATE_SHOW_ALL_READERS);
        updateFilteredBookList(PREDICATE_SHOW_ALL_BOOKS);
        return status;
    }

    @Override
    public boolean returnBook(Name readerName, Barcode barcode) {
        requireAllNonNull(barcode, readerName);
        boolean status = smartLib.returnBook(readerName, barcode);
        updateFilteredReaderList(PREDICATE_SHOW_ALL_READERS);
        updateFilteredBookList(PREDICATE_SHOW_ALL_BOOKS);
        return status;
    }

    @Override
    public void deleteBook(Book target) {
        smartLib.removeBook(target);
    }

    @Override
    public void deleteReader(Reader target) {
        smartLib.removeReader(target);
    }

    @Override
    public void addBook(Book book) {
        smartLib.addBook(book);
        updateFilteredBookList(PREDICATE_SHOW_ALL_BOOKS);
    }

    @Override
    public void addReader(Reader reader) {
        smartLib.addReader(reader);
        updateFilteredReaderList(PREDICATE_SHOW_ALL_READERS);
    }

    @Override
    public void addRecord(Record record) {
        System.out.println(smartLib);
        smartLib.addRecord(record);
        updateFilteredRecordList(PREDICATE_SHOW_ALL_RECORDS);
    }

    @Override
    public void markRecordAsReturned(Record record) {
        Record foundRecord = null;
        for (Record r : smartLib.getRecordList()) {
            if (r.equals(record)) {
                foundRecord = r;
            }
        }
        if (foundRecord != null) {
            foundRecord.setDateReturned(record.getDateReturned());
        }
        updateFilteredRecordList(PREDICATE_SHOW_ALL_RECORDS);
    }

    /**
     * Returns the barcode of the first available (i.e. not borrowed) copy of the book in SmartLib.
     *
     * @param bookName name of the book to be borrowed
     * @return the barcode of the first available copy of the book in SmartLib
     */
    @Override
    public Barcode getBookBarcode(Name bookName) {
        ArrayList<Book> books = smartLib.getBooksByName(bookName);
        requireNonNull(books);

        for (Book b : books) {
            if (!b.isBorrowed()) {
                return b.getBarcode();
            }
        }

        return null;
    }

    /**
     * Returns the barcode of the first copy of the specified book borrowed by the reader in SmartLib.
     *
     * @param bookName   name of the book to be borrowed
     * @param readerName name of the reader who borrowed the book
     * @return the barcode of the first such book in SmartLib
     */
    @Override
    public Barcode getBookBarcodeForReturn(Name bookName, Name readerName) {
        ArrayList<Book> books = smartLib.getBooksByName(bookName);
        requireNonNull(books);

        for (Book b : books) {
            if (b.getBorrowerName() != null && b.getBorrowerName().equals(readerName)) {
                return b.getBarcode();
            }
        }

        return null;
    }

    @Override
    public void setReader(Reader target, Reader editedReader) {
        requireAllNonNull(target, editedReader);

        smartLib.setReader(target, editedReader);
    }

    //=========== Filtered Person List Accessors =============================================================

    @Override
    public ObservableList<Book> getFilteredBookList() {
        return filteredBooks;
    }

    @Override
    public ObservableList<Reader> getFilteredReaderList() {
        return filteredReaders;
    }

    @Override
    public void updateFilteredBookList(Predicate<Book> predicate) {
        requireNonNull(predicate);
        filteredBooks.setPredicate(predicate);
    }

    @Override
    public void updateFilteredReaderList(Predicate<Reader> predicate) {
        requireNonNull(predicate);
        filteredReaders.setPredicate(predicate);
    }

    @Override
    public void updateFilteredRecordList(Predicate<Record> predicate) {
        requireNonNull(predicate);
        filteredRecords.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return smartLib.equals(other.smartLib)
                && userPrefs.equals(other.userPrefs)
                && filteredReaders.equals(other.filteredReaders);
    }

}
