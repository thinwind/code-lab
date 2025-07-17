/* 
 * Copyright 2025 Shang Yehua <niceshang@outlook.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package win.shangyh.demo.poi;

import java.util.function.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * XLSX 文件读取器
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2025-07-15  11:20
 *
 */
public class XlsxReader {

    /**
     * 读取 XLSX 文件
     *
     * @param filePath XLSX 文件路径
     * @throws IOException 读取文件或者关闭文件失败时抛出此异常
     * @throws InvalidFormatException excel 文件格式不正确时抛出此异常
     * @throws IllegalArgumentException 如果文件不存在或者不是 XLSX 文件
     * 
     */
    public static void readXlsx(String filePath,Consumer<String[]> rowConsumer) throws InvalidFormatException, IOException {
        //读取 XLSX
        //使用 Apache POI
        var file = Path.of(filePath).toFile();
        if(!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + filePath);
        } 
        if (!(filePath.endsWith(".xlsx") || filePath.endsWith(".xlsm"))) {
            throw new IllegalArgumentException("File is not an XLSX file: " + filePath);
        }
        
        try (var inputStream = file.toPath().toUri().toURL().openStream()) {
            readXlsx(inputStream, rowConsumer);
        } catch (IOException e) {
            throw new IOException("Failed to read XLSX file: " + filePath, e);
        }
    }
    
    public static void readXlsx(InputStream inputStream, Consumer<String[]> rowConsumer) throws InvalidFormatException, IOException {
        var workbook = new XSSFWorkbook(inputStream);
        var sheet = workbook.getSheetAt(0);
        sheet.forEach(row -> {
            if(row.getRowNum()==0) {
                //跳过第一行
                return;
            }
            var cells = new String[row.getLastCellNum()];
            for (int i = 0; i < row.getLastCellNum(); i++) {
                var cell = row.getCell(i);
                 switch (cell.getCellType()){
                    case STRING:
                        cells[i] = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        double numericValue = cell.getNumericCellValue();
                        //检查是否是整数
                        long longValue = (long) numericValue;
                        if(longValue == numericValue) {
                            //如果是整数，转换为字符串
                            cells[i] = String.valueOf(longValue);
                        }else{
                            //如果是小数，小数
                            cells[i] = String.valueOf(numericValue);
                        }
                        break;
                    case BOOLEAN:
                        cells[i] = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        cells[i] = cell.getCellFormula();
                        break;
                    case BLANK:
                        cells[i] = "";
                        break;
                    default:
                        cells[i] = cell.toString();
                 }
                
            }
            rowConsumer.accept(cells);
        });
        try {
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close workbook", e);
        }
    }
}
