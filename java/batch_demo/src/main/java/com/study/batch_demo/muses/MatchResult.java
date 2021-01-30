/* 
 * Copyright 2021 Shang Yehua
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
package com.study.batch_demo.muses;

/**
 *
 * 匹配结果
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-00-05  07:21
 *
 */
public class MatchResult {
    
    private String joinedLine;
    
    //匹配结果，依次为1和2是否匹配，1/2和3是否匹配,....
    //第一行在所有行不匹配时才应该返回false
    //最终结果会比参与匹配的行数少1
    private boolean[] matchedDetails;

    public String getJoinedLine() {
        return joinedLine;
    }

    public void setJoinedLine(String joinedLine) {
        this.joinedLine = joinedLine;
    }

    public boolean[] getMatchedDetails() {
        return matchedDetails;
    }

    public void setMatchedDetails(boolean[] matchedDetails) {
        this.matchedDetails = matchedDetails;
    }
    
    
}
