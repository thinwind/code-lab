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

import java.util.List;

/**
 *
 * TODO 说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2025-07-15  15:06
 *
 */
public class XlsxDemo {
    public static void main(String[] args) {
        ProductFactory factory = new ProductFactory();
        var demoFile="/Users/shangyh/Playground/code-lab/java/poi-demo/src/main/resources/问题初始化.xlsx";
        try {
            List<ProductLine> productLines = factory.createProductLines(demoFile);
            for (ProductLine productLine : productLines) {
                System.out.println(productLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
