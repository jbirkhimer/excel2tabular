
package com.asoroka.sidora.excel2tabular;

import java.io.File;
import java.net.URL;
import java.util.List;

import com.google.common.base.Function;

/**
 * Represents any transformation from a {@link File} that contains an Excel spreadsheet to a File containing a CSV
 * tabular data file.
 * 
 * @author ajs6f
 */
public interface ExcelToTabular extends Function<URL, List<File>> {
    // EMPTY
}