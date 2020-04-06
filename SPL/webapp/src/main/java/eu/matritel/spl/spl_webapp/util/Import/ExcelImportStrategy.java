package eu.matritel.spl.spl_webapp.util.Import;

import eu.matritel.spl.spl_webapp.service.CheckListService;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class ExcelImportStrategy implements ImportStrategy {
    private static final int COLUMN_COUNT = 2;

    private static int headerCount = 1;

    private DataFormatter formatter = new DataFormatter();

    @Autowired
    CheckListService checkListService;

    public XSSFSheet createSheet(MultipartFile file) {
        XSSFSheet sheet = null;

        try {
            XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
            sheet = wb.getSheetAt(0);
            wb.close();
        } catch (FileNotFoundException fileNotFound) {
            fileNotFound.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return sheet;
    }

    public String[][] saveToArray(XSSFSheet sheet) {
        XSSFRow row;
        XSSFCell cell;
        int rows;

        rows = sheet.getLastRowNum() - headerCount;

        String[][] arrayOfSheet = new String[rows][COLUMN_COUNT];

        for (int r = headerCount; r < rows; r++) {
            for (int c = 0; c < COLUMN_COUNT; c++) {
                cell =sheet.getRow(r).getCell(c);
                arrayOfSheet[r][c] = formatter.formatCellValue(cell);
            }
        }

        return arrayOfSheet;
    }

    @Override
    public String[][] parseFile(MultipartFile file) {
        XSSFSheet sheet = createSheet(file);
        String[][] arrayOfSheet = saveToArray(sheet);
        return arrayOfSheet;
    }
}
