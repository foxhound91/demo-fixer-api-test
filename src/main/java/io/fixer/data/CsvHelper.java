package io.fixer.data;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CsvHelper {

    private BufferedWriter bw;

    public CsvHelper() throws FileNotFoundException {
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("random_currency_rates.csv"), StandardCharsets.UTF_8));
    }

    public void writeToCSV(String currencySymbol, Float currencyRate) throws IOException {
        String oneLine = currencySymbol + "," + currencyRate;
        bw.write(oneLine);
        bw.newLine();
        bw.flush();
    }

}
