package br.com.tecsinapse.dataio;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.poi.xssf.model.Styles;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.com.tecsinapse.dataio.style.Colors;
import br.com.tecsinapse.dataio.style.TableCellStyle;
import br.com.tecsinapse.dataio.util.ExporterUtil;

public class MemoryTest {


    private List<Table> tables;
    private Path tempDir;

    @BeforeClass
    public void setup() throws IOException {
        tables = dsData(3, 5000, 20);
        tempDir = Files.createTempDirectory("data-io-test");
        if (!Files.exists(tempDir)) {
            Files.createDirectory(tempDir);
        }
    }

    private List<Table> dsData(int sheets, int rows, int cols) {
        final String colSample = "Lorem Ipsum is simply dummy text of the printing and typesetting industry Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        List<Table> tables = new ArrayList<>(sheets);
        List<TableCellStyle> cellStyles = new ArrayList<>(5);
        cellStyles.add(new TableCellStyle(Colors.GREY_25_PERCENT));
        cellStyles.add(new TableCellStyle(Colors.BROWN));
        cellStyles.add(new TableCellStyle(Colors.DARK_YELLOW));
        cellStyles.add(new TableCellStyle(Colors.ORANGE));
        cellStyles.add(new TableCellStyle(Colors.SEA_GREEN));
        Random random = new Random();
        for (int s = 0; s < sheets; s++) {
            Table table = new Table(ExporterFormatter.BRAZILIAN);
            int styleIndex = 0;
            for (int r = 0; r < rows; r++) {
                table.addNewRow();
                TableCellStyle st = cellStyles.get(styleIndex);
                for (int c = 0; c < cols; c++) {
                    int max = c == 5 || c == 7 ? 25 : colSample.length() - 1;
                    int size = random.nextInt(max);
                    String txSample = colSample.substring(0, size < 3 ? 3 : size);
                    table.add(txSample, st);
                }
                if (r % 250 == 0) {
                    styleIndex++;
                }
                if (styleIndex >= cellStyles.size()) {
                    styleIndex = 0;
                }
            }
            tables.add(table);
        }
        return tables;
    }

    @Test(enabled = false)
    public void sxlsxMemoryTest() throws IOException {
        File xlsx = ExporterUtil.getMoreThanOneSheetSxlsxFile(tables, "test-memory-stream.xlsx");
        Path out = new File(tempDir.toFile(), xlsx.getName()).toPath();
        Files.move(xlsx.toPath(), out, StandardCopyOption.REPLACE_EXISTING);
        System.out.println(out.toFile().getAbsolutePath());
    }

    @Test(enabled = false)
    public void xlsxMemoryTest() throws IOException {
        File xls = ExporterUtil.getMoreThanOneSheetXlsxFile(tables, "test-memory.xlsx");
        Path out = new File(tempDir.toFile(), xls.getName()).toPath();
        Files.move(xls.toPath(),  out, StandardCopyOption.REPLACE_EXISTING);
        System.out.println(out.toFile().getAbsolutePath());
    }

}
