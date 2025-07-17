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
import java.util.Objects;

/**
 *
 * 产品环节
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2025-07-16  14:00
 *
 */
public class ProductStage {

    private final String name;

    private final String code;

    private final List<ProblemDescription> descriptions = new ArrayList<>();

    public ProductStage(String name, String code) {
        this.name = Objects.requireNonNull(name, "Product stage name must not be null.");
        this.code = Objects.requireNonNull(code, "Product stage code must not be null.");
    }

    public static ProductStage of(String stageLine) {
        if (stageLine == null || stageLine.isBlank()) {
            throw new IllegalArgumentException("Product stage must not be blank.");
        }
        var parts = stageLine.split("-");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Product stage must contain at least name and code: " + stageLine);
        }
        return new ProductStage(parts[0].trim(), parts[1].trim());
    }

    public void addDescription(ProblemDescription description) {
        if (description == null) {
            throw new IllegalArgumentException("Problem description must not be null.");
        }
        if (this.descriptions.contains(description)) {
            return; // 如果已经存在，则不添加
        }
        this.descriptions.add(description);
    }

    /**
     * 添加一组问题描述
     */
    public void addDescriptions(ProblemDescription... descriptions) {
        if (descriptions == null) {
            return;
        }
        for (var description : descriptions) {
            this.addDescription(description);
        }
    }

    /**
     * 添加一组问题描述
     */
    public void addDescriptionList(List<ProblemDescription> descriptions) {
        if (descriptions == null || descriptions.isEmpty()) {
            return;
        }
        for (var description : descriptions) {
            this.addDescription(description);
        }
    }

    /**
     * 清除所有问题描述
     */
    public void clear() {
        this.descriptions.clear();
    }

    public void removeDescription(ProblemDescription description) {
        if (description == null) {
            return;
        }
        this.descriptions.remove(description);
    }

    public List<ProblemDescription> getDescriptions() {
        return new ArrayList<>(descriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProductStage)) {
            return false;
        }
        ProductStage other = (ProductStage) obj;
        return Objects.equals(name, other.name) && Objects.equals(code, other.code);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return toString("");
    }
    
    public String toString(String prefix) {
        var str ="ProductStage: " + code + "-" + name;
        for (ProblemDescription description : descriptions) {
            str += "\n" + prefix + "\t" + description.toString();
        }
        return str;
    }

}
