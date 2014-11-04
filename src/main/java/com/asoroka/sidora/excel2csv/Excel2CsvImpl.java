
package com.asoroka.sidora.excel2csv;

import static com.google.common.base.Throwables.propagate;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

/**
 * @author ajs6f
 */
public class Excel2CsvImpl implements Excel2Csv {

    private final int minColumns;

    final private static Logger log = getLogger(Excel2CsvImpl.class);

    /**
     * Default constructor.
     */
    public Excel2CsvImpl() {
        this.minColumns = -1;
    }

    /**
     * @param minColumns
     */
    public Excel2CsvImpl(final int minColumns) {
        this.minColumns = minColumns;
    }

    @Override
    public List<File> apply(final URL inputUrl) {

        final POIFSFileSystem poiFileSystem;
        try (final InputStream inputStream = inputUrl.openStream()) {
            poiFileSystem = new POIFSFileSystem(inputStream);

            final XLS2CSVmra poiTransformer =
                    new XLS2CSVmra(poiFileSystem, minColumns);
            final MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(
                    poiTransformer);
            final FormatTrackingHSSFListener ftListener = new FormatTrackingHSSFListener(
                    listener);

            poiTransformer.setFormatListener(ftListener);

            final HSSFRequest request = new HSSFRequest();
            request.addListenerForAllRecords(ftListener);
            final HSSFEventFactory factory = new HSSFEventFactory();
            factory.processWorkbookEvents(request, poiFileSystem);
            return poiTransformer.getOutputs();

        } catch (final IOException e) {
            log.error("Could not process input URL: {}!", inputUrl);
            throw propagate(e);
        }
    }
}
