package seedu.smartlib.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.smartlib.model.Model.PREDICATE_SHOW_ALL_READERS;
import static seedu.smartlib.testutil.Assert.assertThrows;
import static seedu.smartlib.testutil.TypicalModels.ALICE;
import static seedu.smartlib.testutil.TypicalModels.AMY;
import static seedu.smartlib.testutil.TypicalModels.BENSON;
import static seedu.smartlib.testutil.TypicalModels.ELLE;
import static seedu.smartlib.testutil.TypicalModels.FIONA;
import static seedu.smartlib.testutil.TypicalModels.HABIT;
import static seedu.smartlib.testutil.TypicalModels.HARRY;
import static seedu.smartlib.testutil.TypicalModels.IDA;
import static seedu.smartlib.testutil.TypicalModels.LIFE;
import static seedu.smartlib.testutil.TypicalModels.RECORD_A;
import static seedu.smartlib.testutil.TypicalModels.RECORD_B;
import static seedu.smartlib.testutil.TypicalModels.RECORD_C;
import static seedu.smartlib.testutil.TypicalModels.SECRET;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.smartlib.commons.core.GuiSettings;
import seedu.smartlib.commons.core.name.Name;
import seedu.smartlib.model.book.Barcode;
import seedu.smartlib.model.book.Book;
import seedu.smartlib.model.book.exceptions.BookNotFoundException;
import seedu.smartlib.model.book.exceptions.DuplicateBookException;
import seedu.smartlib.model.reader.NameContainsKeywordsPredicate;
import seedu.smartlib.model.reader.Reader;
import seedu.smartlib.model.reader.exceptions.DuplicateReaderException;
import seedu.smartlib.model.reader.exceptions.ReaderNotFoundException;
import seedu.smartlib.model.record.exceptions.DuplicateRecordException;
import seedu.smartlib.testutil.BookBuilder;
import seedu.smartlib.testutil.SmartLibBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();
    private SmartLib smartLib = new SmartLibBuilder()
            .withReader(ALICE).withReader(BENSON)
            .withBook(HARRY).withBook(SECRET)
            .withRecord(RECORD_C)
            .build();
    private final UserPrefs userPrefs = new UserPrefs();
    private final GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
    private final Path filePath = Paths.get("smartLib/file/path");

    @Test
    public void constructorWithoutParams() {
        // empty constructor
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new SmartLib(), new SmartLib(modelManager.getSmartLib()));
    }

    @Test
    public void constructorWithParams() {
        // constructor with params
        ModelManager mm = new ModelManager(smartLib, userPrefs);
        assertEquals(userPrefs, mm.getUserPrefs());
        assertEquals(new GuiSettings(), mm.getGuiSettings());
        assertEquals(smartLib, new SmartLib(mm.getSmartLib()));
    }

    @Test
    public void getUserPrefs() {
        userPrefs.setSmartLibFilePath(filePath);
        userPrefs.setGuiSettings(guiSettings);
        modelManager = new ModelManager(smartLib, userPrefs);

        ModelManager modelManagerCopy = new ModelManager(smartLib, userPrefs);
        ModelManager modelManager2 = new ModelManager();

        // EP: same UserPrefs
        assertEquals(modelManager.getUserPrefs(), modelManagerCopy.getUserPrefs());

        // EP: second ModelManager missing UserPrefs
        assertNotEquals(modelManager.getUserPrefs(), modelManager2.getUserPrefs());

        // EP: different UserPrefs
        modelManager2 = new ModelManager(smartLib, new UserPrefs());
        assertNotEquals(modelManager.getUserPrefs(), modelManager2.getUserPrefs());
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        userPrefs.setSmartLibFilePath(filePath);
        userPrefs.setGuiSettings(guiSettings);
        assertDoesNotThrow(() -> modelManager.setUserPrefs(userPrefs));

        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setSmartLibFilePath(Paths.get("new/smartLib/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void getGuiSettings() {
        userPrefs.setSmartLibFilePath(filePath);
        userPrefs.setGuiSettings(guiSettings);
        modelManager = new ModelManager(smartLib, userPrefs);

        ModelManager modelManagerCopy = new ModelManager(smartLib, userPrefs);
        ModelManager modelManager2 = new ModelManager();

        // EP: same UserPrefs (and thus GuiSettings)
        assertEquals(modelManager.getGuiSettings(), modelManagerCopy.getGuiSettings());

        // EP: second ModelManager missing UserPrefs (and thus GuiSettings)
        assertNotEquals(modelManager.getGuiSettings(), modelManager2.getGuiSettings());

        // EP: different UserPrefs (and thus GuiSettings)
        modelManager2 = new ModelManager(smartLib, new UserPrefs());
        assertNotEquals(modelManager.getGuiSettings(), modelManager2.getGuiSettings());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        assertDoesNotThrow(() -> modelManager.setGuiSettings(guiSettings));

        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void getSmartLibFilePath() {
        userPrefs.setSmartLibFilePath(filePath);
        userPrefs.setGuiSettings(guiSettings);
        modelManager = new ModelManager(smartLib, userPrefs);

        ModelManager modelManagerCopy = new ModelManager(smartLib, userPrefs);
        ModelManager modelManager2 = new ModelManager();

        // EP: same UserPrefs (and thus filepath)
        assertEquals(modelManager.getSmartLibFilePath(), modelManagerCopy.getSmartLibFilePath());

        // EP: second ModelManager missing UserPrefs (and thus filepath)
        assertNotEquals(modelManager.getSmartLibFilePath(), modelManager2.getSmartLibFilePath());

        // EP: different UserPrefs (and thus filepath)
        modelManager2 = new ModelManager(smartLib, new UserPrefs());
        assertNotEquals(modelManager.getSmartLibFilePath(), modelManager2.getSmartLibFilePath());
    }

    @Test
    public void setSmartLibFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setSmartLibFilePath(null));
    }

    @Test
    public void setSmartLibFilePath_validPath_setsSmartLibFilePath() {
        assertDoesNotThrow(() -> modelManager.setSmartLibFilePath(filePath));

        modelManager.setSmartLibFilePath(filePath);
        assertEquals(filePath, modelManager.getSmartLibFilePath());
    }

    @Test
    public void getSmartLib() {
        userPrefs.setSmartLibFilePath(filePath);
        userPrefs.setGuiSettings(guiSettings);
        modelManager = new ModelManager(smartLib, userPrefs);

        ModelManager modelManagerCopy = new ModelManager(smartLib, userPrefs);
        ModelManager modelManager2 = new ModelManager();

        // EP: same SmartLib
        assertEquals(modelManager.getSmartLib(), modelManagerCopy.getSmartLib());

        // EP: second ModelManager missing SmartLib
        assertNotEquals(modelManager.getSmartLib(), modelManager2.getSmartLib());

        // EP: different SmartLib
        modelManager2 = new ModelManager(new SmartLib(), userPrefs);
        assertNotEquals(modelManager.getSmartLib(), modelManager2.getSmartLib());
    }

    @Test
    public void setSmartLib_nullSmartLib_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setSmartLib(null));
    }

    @Test
    public void setSmartLib_validSmartLib_setsSmartLib() {
        assertDoesNotThrow(() -> modelManager.setSmartLib(smartLib));

        modelManager.setSmartLib(smartLib);
        assertEquals(smartLib, modelManager.getSmartLib());
    }

    @Test
    public void hasBook_nullBook_throwsNullPointerException() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertThrows(NullPointerException.class, () -> modelManager.hasBook((Book) null));
    }

    @Test
    public void hasBook_bookNotInSmartLib_returnsFalse() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertFalse(modelManager.hasBook(HABIT));
        assertFalse(modelManager.hasBook(LIFE));
    }

    @Test
    public void hasBook_bookInSmartLib_returnsTrue() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertTrue(modelManager.hasBook(HARRY));
        assertTrue(modelManager.hasBook(SECRET));
    }

    @Test
    public void hasBookWithName_nullBookName_throwsNullPointerException() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertThrows(NullPointerException.class, () -> modelManager.hasBook((Name) null));
    }

    @Test
    public void hasBookWithName_bookNotInSmartLib_returnsFalse() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertFalse(modelManager.hasBook(HABIT.getName()));
        assertFalse(modelManager.hasBook(LIFE.getName()));
    }

    @Test
    public void hasBookWithName_bookInSmartLib_returnsTrue() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertTrue(modelManager.hasBook(HARRY.getName()));
        assertTrue(modelManager.hasBook(SECRET.getName()));
    }

    @Test
    public void hasBookWithBarcode_nullBarcode_throwsNullPointerException() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertThrows(NullPointerException.class, () -> modelManager.hasBookWithBarcode(null));
    }

    @Test
    public void hasBookWithBarcode_bookNotInSmartLib_returnsFalse() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertFalse(modelManager.hasBookWithBarcode(HABIT.getBarcode()));
        assertFalse(modelManager.hasBookWithBarcode(LIFE.getBarcode()));
    }

    @Test
    public void hasBookWithBarcode_bookInSmartLib_returnsTrue() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertTrue(modelManager.hasBookWithBarcode(HARRY.getBarcode()));
        assertTrue(modelManager.hasBookWithBarcode(SECRET.getBarcode()));
    }

    @Test
    public void isBookWithBarcodeBorrowed() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertFalse(modelManager.isBookWithBarcodeBorrowed(HARRY.getBarcode()));
        assertTrue(modelManager.isBookWithBarcodeBorrowed(SECRET.getBarcode()));
    }

    @Test
    public void hasReader_nullReader_throwsNullPointerException() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertThrows(NullPointerException.class, () -> modelManager.hasReader((Reader) null));
    }

    @Test
    public void hasReader_readerNotInSmartLib_returnsFalse() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertFalse(modelManager.hasReader(AMY));
        assertFalse(modelManager.hasReader(IDA));
    }

    @Test
    public void hasReader_readerInSmartLib_returnsTrue() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertTrue(modelManager.hasReader(ALICE));
        assertTrue(modelManager.hasReader(BENSON));
    }

    @Test
    public void hasReaderWithName_nullReaderName_throwsNullPointerException() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertThrows(NullPointerException.class, () -> modelManager.hasReader((Name) null));
    }

    @Test
    public void hasReaderWithName_readerNotInSmartLib_returnsFalse() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertFalse(modelManager.hasReader(AMY.getName()));
        assertFalse(modelManager.hasReader(IDA.getName()));
    }

    @Test
    public void hasReaderWithName_readerInSmartLib_returnsTrue() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertTrue(modelManager.hasReader(ALICE.getName()));
        assertTrue(modelManager.hasReader(BENSON.getName()));
    }

    @Test
    public void canReaderBorrow() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: null reader
        assertThrows(NullPointerException.class, () -> modelManager.canReaderBorrow(null));

        // EP: readers not in SmartLib
        assertFalse(modelManager.canReaderBorrow(AMY.getName()));
        assertFalse(modelManager.canReaderBorrow(IDA.getName()));

        // EP: readers in SmartLib, can borrow (no overdue books)
        assertTrue(modelManager.canReaderBorrow(ALICE.getName()));

        // EP: readers in SmartLib, cannot borrow (with overdue books)
        assertFalse(modelManager.canReaderBorrow(BENSON.getName()));
    }

    @Test
    public void hasRecord_nullRecord_throwsNullPointerException() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertThrows(NullPointerException.class, () -> modelManager.hasRecord(null));
    }

    @Test
    public void hasRecord_recordNotInSmartLib_returnsFalse() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertFalse(modelManager.hasRecord(RECORD_A));
        assertFalse(modelManager.hasRecord(RECORD_B));
    }

    @Test
    public void hasRecord_recordInSmartLib_returnsTrue() {
        modelManager = new ModelManager(smartLib, userPrefs);
        assertTrue(modelManager.hasRecord(RECORD_C));
    }

    @Test
    public void borrowBook_hasNullParam_throwsNullPointerException() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: valid reader name and barcode
        assertDoesNotThrow(() -> modelManager.borrowBook(ALICE.getName(), HARRY.getBarcode()));

        // EP: null reader name and barcode
        assertThrows(NullPointerException.class, () -> modelManager.borrowBook(null, null));

        // EP: null reader name, valid barcode
        assertThrows(NullPointerException.class, () -> modelManager.borrowBook(null, HARRY.getBarcode()));

        // EP: null barcode, valid reader name
        assertThrows(NullPointerException.class, () -> modelManager.borrowBook(ALICE.getName(), null));
    }

    @Test
    public void borrowBook_noNullParam() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: valid reader name and barcode -> can borrow
        assertTrue(modelManager.borrowBook(ALICE.getName(), HARRY.getBarcode()));

        // EP: invalid reader name, valid barcode -> cannot borrow
        modelManager = new ModelManager(smartLib, userPrefs);
        assertFalse(modelManager.borrowBook(AMY.getName(), HARRY.getBarcode()));
        assertFalse(modelManager.borrowBook(IDA.getName(), HARRY.getBarcode()));

        // EP: valid reader name, invalid barcode -> cannot borrow
        assertFalse(modelManager.borrowBook(ALICE.getName(), HABIT.getBarcode()));
        assertFalse(modelManager.borrowBook(ALICE.getName(), LIFE.getBarcode()));

        // EP: valid reader name, barcode of borrowed book -> cannot borrow
        assertFalse(modelManager.borrowBook(ALICE.getName(), SECRET.getBarcode()));

        // EP: name of reader with overdue book, valid barcode -> cannot borrow
        assertFalse(modelManager.borrowBook(BENSON.getName(), HARRY.getBarcode()));

        // EP: reader with a borrowed book, the book he/she borrowed -> cannot borrow
        modelManager.borrowBook(ALICE.getName(), HARRY.getBarcode());
        assertFalse(modelManager.borrowBook(ALICE.getName(), HARRY.getBarcode()));
        assertFalse(modelManager.borrowBook(BENSON.getName(), SECRET.getBarcode()));
    }

    @Test
    public void returnBook_hasNullParam_throwsNullPointerException() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: valid reader name and barcode
        assertDoesNotThrow(() -> modelManager.returnBook(BENSON.getName(), SECRET.getBarcode()));

        // EP: null reader name and barcode
        modelManager = new ModelManager(smartLib, userPrefs);
        assertThrows(NullPointerException.class, () -> modelManager.returnBook(null, null));

        // EP: null reader name, valid barcode
        assertThrows(NullPointerException.class, () -> modelManager.returnBook(null, SECRET.getBarcode()));

        // EP: null barcode, valid reader name
        assertThrows(NullPointerException.class, () -> modelManager.returnBook(BENSON.getName(), null));
    }

    @Test
    public void returnBook_noNullParam() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: valid reader name and barcode -> can return
        assertTrue(modelManager.returnBook(BENSON.getName(), SECRET.getBarcode()));

        // EP: invalid reader name, valid barcode -> cannot return
        modelManager = new ModelManager(smartLib, userPrefs);
        assertFalse(modelManager.returnBook(AMY.getName(), SECRET.getBarcode()));
        assertFalse(modelManager.returnBook(IDA.getName(), SECRET.getBarcode()));

        // EP: valid reader name, invalid barcode -> cannot return
        assertFalse(modelManager.returnBook(BENSON.getName(), HABIT.getBarcode()));
        assertFalse(modelManager.returnBook(BENSON.getName(), LIFE.getBarcode()));

        // EP: valid reader name, barcode of book not borrowed by him -> cannot return
        assertFalse(modelManager.returnBook(BENSON.getName(), HARRY.getBarcode()));

        // EP: name of reader without book, valid barcode -> cannot return
        assertFalse(modelManager.returnBook(ALICE.getName(), SECRET.getBarcode()));

        // EP: reader with without a borrowed book, an un-borrowed book -> cannot return
        modelManager.returnBook(BENSON.getName(), SECRET.getBarcode());
        assertFalse(modelManager.returnBook(ALICE.getName(), HARRY.getBarcode()));
        assertFalse(modelManager.returnBook(ALICE.getName(), SECRET.getBarcode()));
        assertFalse(modelManager.returnBook(BENSON.getName(), HARRY.getBarcode()));
        assertFalse(modelManager.returnBook(BENSON.getName(), SECRET.getBarcode()));
    }

    @Test
    public void deleteBook() {
        modelManager = new ModelManager(smartLib, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(smartLib, userPrefs);

        // EP: null book
        assertThrows(NullPointerException.class, () -> modelManager.deleteBook(null));

        // EP: book not in SmartLib -> throws exception
        assertThrows(BookNotFoundException.class, () -> modelManager.deleteBook(HABIT));
        assertThrows(BookNotFoundException.class, () -> modelManager.deleteBook(LIFE));

        // EP: book in SmartLib -> book deleted
        modelManager.deleteBook(SECRET);
        assertNotEquals(modelManagerCopy, modelManager);
        assertFalse(modelManager.hasBook(SECRET));

        modelManager = new ModelManager(smartLib, userPrefs);
        modelManager.deleteBook(HARRY);
        assertNotEquals(modelManagerCopy, modelManager);
        assertFalse(modelManager.hasBook(HARRY));
    }

    @Test
    public void deleteReader() {
        modelManager = new ModelManager(smartLib, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(smartLib, userPrefs);

        // EP: null reader
        assertThrows(NullPointerException.class, () -> modelManager.deleteReader(null));

        // EP: reader not in SmartLib -> throws exception
        assertThrows(ReaderNotFoundException.class, () -> modelManager.deleteReader(AMY));
        assertThrows(ReaderNotFoundException.class, () -> modelManager.deleteReader(IDA));

        // EP: reader in SmartLib -> reader deleted
        modelManager.deleteReader(ALICE);
        assertNotEquals(modelManagerCopy, modelManager);
        assertFalse(modelManager.hasReader(ALICE));

        modelManager = new ModelManager(smartLib, userPrefs);
        modelManager.deleteReader(BENSON);
        assertNotEquals(modelManagerCopy, modelManager);
        assertFalse(modelManager.hasReader(BENSON));
    }

    @Test
    public void addBook() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: null book
        assertThrows(NullPointerException.class, () -> modelManager.addBook(null));

        // EP: book already in SmartLib -> throws exception
        assertThrows(DuplicateBookException.class, () -> modelManager.addBook(SECRET));
        assertThrows(DuplicateBookException.class, () -> modelManager.addBook(HARRY));

        // EP: book not in SmartLib -> book added
        modelManager.addBook(HABIT);
        assertTrue(modelManager.hasBook(HABIT));
        modelManager.addBook(LIFE);
        assertTrue(modelManager.hasBook(LIFE));
    }

    @Test
    public void addReader() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: null reader
        assertThrows(NullPointerException.class, () -> modelManager.addReader(null));

        // EP: reader already in SmartLib -> throws exception
        assertThrows(DuplicateReaderException.class, () -> modelManager.addReader(ALICE));
        assertThrows(DuplicateReaderException.class, () -> modelManager.addReader(BENSON));

        // EP: reader not in SmartLib -> reader added
        modelManager.addReader(AMY);
        assertTrue(modelManager.hasReader(AMY));
        modelManager.addReader(IDA);
        assertTrue(modelManager.hasReader(IDA));
    }

    @Test
    public void addRecord() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: null record
        assertThrows(NullPointerException.class, () -> modelManager.addRecord(null));

        // EP: record already in SmartLib -> throws exception
        assertThrows(DuplicateRecordException.class, () -> modelManager.addRecord(RECORD_C));

        // EP: record not in SmartLib -> record added
        modelManager.addRecord(RECORD_A);
        assertTrue(modelManager.hasRecord(RECORD_A));
        modelManager.addRecord(RECORD_B);
        assertTrue(modelManager.hasRecord(RECORD_B));
    }

    @Test
    public void markRecordAsReturned() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: null record
        assertThrows(AssertionError.class, () -> modelManager.markRecordAsReturned(null));

        // EP: invalid record -> throws exception
        assertThrows(AssertionError.class, () -> modelManager.markRecordAsReturned(RECORD_A));

        // EP: valid record -> return successful
        assertDoesNotThrow(() -> modelManager.markRecordAsReturned(RECORD_C));
    }

    @Test
    public void getBookBarcode() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: null book name
        assertThrows(NullPointerException.class, () -> modelManager.getBookBarcode(null));

        // EP: invalid book name -> returns null
        assertNull(modelManager.getBookBarcode(HABIT.getName()));
        assertNull(modelManager.getBookBarcode(LIFE.getName()));

        // EP: valid book name -> returns barcode of book
        assertEquals(SECRET.getBarcode(), modelManager.getBookBarcode(SECRET.getName()));
        assertEquals(HARRY.getBarcode(), modelManager.getBookBarcode(HARRY.getName()));
    }

    @Test
    public void getFirstAvailableBookBarcode() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: null book name
        assertThrows(NullPointerException.class, () -> modelManager.getFirstAvailableBookBarcode(null));

        // EP: invalid book name -> returns first null
        assertNull(modelManager.getFirstAvailableBookBarcode(HABIT.getName()));
        assertNull(modelManager.getFirstAvailableBookBarcode(LIFE.getName()));

        // EP: valid book name -> returns first available barcode of book
        modelManager.addBook(new BookBuilder()
                .withName(SECRET.getName().toString())
                .withAuthor(SECRET.getAuthor().toString())
                .withPublisher(SECRET.getPublisher().toString())
                .withIsbn(SECRET.getIsbn().toString())
                .withBarcode(SECRET.getBarcode().getValue() + 1 + "")
                .withGenre(SECRET.getGenre().toString())
                .build()
        );
        modelManager.addBook(new BookBuilder()
                .withName(SECRET.getName().toString())
                .withAuthor(SECRET.getAuthor().toString())
                .withPublisher(SECRET.getPublisher().toString())
                .withIsbn(SECRET.getIsbn().toString())
                .withBarcode(SECRET.getBarcode().getValue() + 2 + "")
                .withGenre(SECRET.getGenre().toString())
                .build()
        );
        assertEquals(new Barcode(SECRET.getBarcode().getValue() + 1),
                modelManager.getFirstAvailableBookBarcode(SECRET.getName()));
        assertNotEquals(new Barcode(SECRET.getBarcode().getValue() + 2),
                modelManager.getFirstAvailableBookBarcode(SECRET.getName()));
    }

    @Test
    public void getBookNameForReturn() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: null barcode
        assertThrows(NullPointerException.class, () -> modelManager.getBookNameForReturn(null));

        // EP: invalid barcode -> returns null
        assertNull(modelManager.getBookNameForReturn(HABIT.getBarcode()));
        assertNull(modelManager.getBookNameForReturn(LIFE.getBarcode()));

        // EP: valid barcode -> returns name of book
        assertEquals(SECRET.getName(), modelManager.getBookNameForReturn(SECRET.getBarcode()));
        assertEquals(HARRY.getName(), modelManager.getBookNameForReturn(HARRY.getBarcode()));
    }

    @Test
    public void getReaderNameForReturn() {
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: null barcode
        assertThrows(NullPointerException.class, () -> modelManager.getReaderNameForReturn(null));

        // EP: invalid barcode -> returns null
        assertNull(modelManager.getReaderNameForReturn(HABIT.getBarcode()));
        assertNull(modelManager.getReaderNameForReturn(LIFE.getBarcode()));

        // EP: valid barcode -> returns name of borrower, if any
    }

    @Test
    public void setReader() {
        modelManager = new ModelManager(smartLib, userPrefs);
        modelManager.addReader(ELLE);

        // EP: valid target and editedReader
        assertDoesNotThrow(() -> modelManager.setReader(ELLE, FIONA));

        // EP: null target, valid editedReader
        assertThrows(NullPointerException.class, () -> modelManager.setReader(null, ELLE));

        // EP: valid target, null editedReader
        assertThrows(NullPointerException.class, () -> modelManager.setReader(ELLE, null));

        // EP: null target and editedReader
        assertThrows(NullPointerException.class, () -> modelManager.setReader(null, null));

        // EP: valid target but duplicated editedReader
        assertThrows(DuplicateReaderException.class, () -> modelManager.setReader(FIONA, ALICE));
        assertThrows(DuplicateReaderException.class, () -> modelManager.setReader(FIONA, BENSON));

        // EP: invalid target but duplicated editedReader
        assertThrows(ReaderNotFoundException.class, () -> modelManager.setReader(AMY, ALICE));
        assertThrows(ReaderNotFoundException.class, () -> modelManager.setReader(IDA, ALICE));
    }

    @Test
    public void getFilteredReaderList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredReaderList().remove(0));
    }

    @Test
    public void getFilteredBookList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredBookList().remove(0));
    }

    @Test
    public void getFilteredRecordList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredRecordList().remove(0));
    }

    @Test
    public void equals() {
        SmartLib differentSmartLib = new SmartLib();
        modelManager = new ModelManager(smartLib, userPrefs);

        // EP: same object
        assertTrue(modelManager.equals(modelManager));

        // EP: null
        assertFalse(modelManager.equals(null));

        // EP: different data types
        assertFalse(modelManager.equals(5));
        assertFalse(modelManager.equals(" "));

        // EP: same values
        ModelManager modelManagerCopy = new ModelManager(smartLib, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // EP: different smartLib
        assertFalse(modelManager.equals(new ModelManager(differentSmartLib, userPrefs)));

        // EP: different filteredList
        String[] keywords = ALICE.getName().toString().split("\\s+");
        modelManager.updateFilteredReaderList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(smartLib, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredReaderList(PREDICATE_SHOW_ALL_READERS);

        // EP: different userPrefs
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setSmartLibFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(smartLib, differentUserPrefs)));
    }

}
