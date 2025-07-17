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
 * 产品组
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2025-07-15  17:04
 *
 */
public class ProductGroup {
    
    private final String name;
    
    private final String code;
    
    private final List<ProductStage> stages = new ArrayList<>();

    public ProductGroup(String name, String code) {
        this.name = Objects.requireNonNull(name, "Product group name must not be null.");
        this.code = Objects.requireNonNull(code, "Product group code must not be null.");
    }

    public static ProductGroup of(String group) {
         if (group == null || group.isBlank()) {
           throw new IllegalArgumentException("Product line must not be blank.");
        }
        var parts = group.split("-");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Product line must contain at least product name and code: " + group);
        }
        return new ProductGroup(parts[0].trim(),parts[1].trim());
    }
    
    public void clear(){
        this.stages.clear();
    }
    
    public void removeStage(ProductStage stage) {
        if (stage == null) {
            return;
        }
        this.stages.remove(stage);
    }
    
    public void addStage(ProductStage stage) {
        if (stage == null) {
            return;
        }
        if (this.stages.contains(stage)) {
            return; // 如果已经存在，则不添加
        }
        this.stages.add(stage);
    }
    
    public void addStages(ProductStage... stageArray) {
        if (stageArray == null || stageArray.length == 0) {
            return;
        }
        for (ProductStage stage : stageArray) {
           addStage(stage);
        }
    }
    
    public void addStageList(List<ProductStage> stageList) {
        if (stageList == null || stageList.isEmpty()) {
            return;
        }
        for (ProductStage stage : stageList) {
            addStage(stage);
        }
    }
    
    public ProductStage getStage(String stageLine) {
        var stage = ProductStage.of(stageLine);
        var idx = stages.indexOf(stage);
        if (idx >= 0) {
            return stages.get(idx);
        }
        stages.add(stage);
        return stage;
    }
    
    public List<ProductStage> getStages() {
        return new ArrayList<>(stages);
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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ProductGroup other = (ProductGroup) obj;
        return Objects.equals(name, other.name) && Objects.equals(code, other.code);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
    
    public String toString() {
        return toString("");
    }
    
    public String toString(String prefix){
        var str ="ProductGroup: "+code + "-" + name;
        for (ProductStage stage : stages) {
            str += "\n"+prefix+"\t--" + stage.toString(prefix+"\t");
        }
        return str;
    }
    
}
