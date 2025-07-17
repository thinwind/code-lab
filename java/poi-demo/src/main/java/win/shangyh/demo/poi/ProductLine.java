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
 * 产品线
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2025-07-15  15:31
 *
 */
public class ProductLine {
    
    private final String name;
    
    private final String code;
    
    private final List<ProductGroup> group = new ArrayList<>();
   
    public ProductLine(String productName, String productCode) {
        this.name = Objects.requireNonNull(productName, "Product name must not be null.");
        this.code = Objects.requireNonNull(productCode, "Product code must not be null.");
    }

    public static ProductLine of(String pdline) {
        if (pdline == null || pdline.isBlank()) {
           throw new IllegalArgumentException("Product line must not be blank.");
        }
        var parts = pdline.split("-");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Product line must contain at least product name and code: " + pdline);
        }
        return new ProductLine(parts[0].trim(),parts[1].trim());
    }
    
    public void addGroupItem(ProductGroup group) {
        if (group == null) {
            throw new IllegalArgumentException("Product group must not be null.");
        }
        this.group.add(group);
    }
    
    public void addGroupItems(ProductGroup... group) {
        if (group == null) 
            return;
        for (ProductGroup g : group) {
            this.addGroupItem(g);
        }
    }
    
    public ProductGroup getProductGroup(String gpline){
        var pg = ProductGroup.of(gpline);
        var idx = group.indexOf(pg);
        if (idx >= 0) {
            return group.get(idx);
        }
        group.add(pg);
        return pg;
    }
    
    public void addGroupList(List<ProductGroup> groups) {
        if (groups == null || groups.isEmpty()) {
            return;
        }
        for (ProductGroup g : groups) {
            this.addGroupItem(g);
        }
    }
    
    public void clearGroup() {
        this.group.clear();
    }
    
    public void removeGroupItem(ProductGroup group) {
        if (group == null) {
            return;
        }
        this.group.remove(group);
    }
    
    
    public List<ProductGroup> getGroup() {
        return new ArrayList<>(group);
    }

    public String toString(){
        var str="ProductLine: "+code+"-"+name;
        for (ProductGroup productGroup : group) {
            str += "\n\t--" + productGroup.toString("\t");
        }
        return str;
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
        ProductLine other = (ProductLine) obj;
        return Objects.equals(name, other.name) && Objects.equals(code, other.code);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
    
}
