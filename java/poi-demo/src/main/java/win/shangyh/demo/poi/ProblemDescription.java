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

import java.util.Objects;

/**
 *
 * 问题描述
 * 
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2025-07-16  14:01
 *
 */
public class ProblemDescription {
    
    /**
     * 问题描述
     */
    private final String description;
    
    /**
     * 问题分类
     */
    private final String category;
    
    /**
     * 问题种类
     */
    private final String type;

    public ProblemDescription(String description, String category, String type) {
        this.description = Objects.requireNonNull(description, "Problem description must not be null.");
        this.category =  Objects.requireNonNull(category, "[问题分类]不能为空.");
        this.type = Objects.requireNonNull(type, "[问题种类]不能为空.");
    }

    @Override
    public String toString() {
        return description + "@[分类:" + category + "]-[种类:" + type + "]";
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(description);
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
        ProblemDescription other = (ProblemDescription) obj;
        return Objects.equals(description, other.description);
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }
    
}
