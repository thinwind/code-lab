/*
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.deergate.httpclient5;

import java.io.File;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.junit.Test;

/**
 *
 * TODO HttpEntityTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-06-23  19:39
 *
 */
public class HttpEntityTest {

    @Test
    public void test1() {
        File file = new File("somefile.txt");
        FileEntity entity = new FileEntity(file, ContentType.create("text/plain", "UTF-8"));

        HttpPost httppost = new HttpPost("http://localhost/action.do");
        httppost.setEntity(entity);
    }
}
