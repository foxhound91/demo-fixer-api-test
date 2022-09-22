package io.fixer.data;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class CsvWriter implements Closeable {

    private final BufferedWriter bw;

    public CsvWriter() throws FileNotFoundException {
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("random_currency_rates.csv"), StandardCharsets.UTF_8));
    }

    public void writeToCSV(String currencySymbol, Float currencyRate) throws IOException {
        String oneLine = currencySymbol + "," + currencyRate;
        bw.write(oneLine);
        bw.newLine();
        bw.flush();
    }

    @Override
    public void close() throws IOException {
        bw.close();
    }
}
