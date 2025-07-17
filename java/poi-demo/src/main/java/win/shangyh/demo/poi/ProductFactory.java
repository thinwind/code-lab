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

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * 工厂类，用于创建产品线
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2025-07-16  14:27
 *
 */
public class ProductFactory {

    public List<ProductLine> createProductLines(InputStream fileInputStream)
            throws InvalidFormatException, IOException {
        final List<ProductLine> result = new ArrayList<>();

        XlsxReader.readXlsx(fileInputStream, row -> {
            if (row == null || row.length == 0) {
                return;
            }
            var startCol = 1;
            String productLineStr = row[startCol++];
            ProductLine productLine = ProductLine.of(productLineStr);
            var pdlineIdx = result.indexOf(productLine);

            // 如果产品线已经存在，则获取现有的产品线，否则添加新的产品线
            if (pdlineIdx >= 0) {
                productLine = result.get(pdlineIdx);
            } else {
                result.add(productLine);
            }

            ProductGroup group = productLine.getProductGroup(row[startCol++]);
            ProductStage stage = group.getStage(row[startCol++]);
            ProblemDescription description = new ProblemDescription(
                    row[startCol++],
                    row[startCol++],
                    row[startCol++]);
            stage.addDescription(description);
        });

        return result;
    }
    
     public List<ProductLine> createProductLines(String filePath) throws InvalidFormatException, MalformedURLException, IOException{
        return createProductLines(Path.of(filePath).toUri().toURL().openStream());
     }
}
