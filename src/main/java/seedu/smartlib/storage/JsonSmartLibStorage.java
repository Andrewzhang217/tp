package seedu.smartlib.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.smartlib.commons.core.LogsCenter;
import seedu.smartlib.commons.exceptions.DataConversionException;
import seedu.smartlib.commons.exceptions.IllegalValueException;
import seedu.smartlib.commons.util.FileUtil;
import seedu.smartlib.commons.util.JsonUtil;
import seedu.smartlib.model.ReadOnlySmartLib;

/**
 * A class to access SmartLib data stored as a json file on the hard disk.
 */
public class JsonSmartLibStorage implements SmartLibStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonSmartLibStorage.class);

    private Path filePath;

    public JsonSmartLibStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getSmartLibFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlySmartLib> readSmartLib() throws DataConversionException {
        return readSmartLib(filePath);
    }

    /**
     * Similar to {@link #readSmartLib()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlySmartLib> readSmartLib(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableSmartLib> jsonSmartLib = JsonUtil.readJsonFile(
                filePath, JsonSerializableSmartLib.class);
        if (!jsonSmartLib.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonSmartLib.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveSmartLib(ReadOnlySmartLib smartLib) throws IOException {
        saveSmartLib(smartLib, filePath);
    }

    /**
     * Similar to {@link #saveSmartLib(ReadOnlySmartLib)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveSmartLib(ReadOnlySmartLib smartLib, Path filePath) throws IOException {
        requireNonNull(smartLib);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableSmartLib(smartLib), filePath);
    }

}
