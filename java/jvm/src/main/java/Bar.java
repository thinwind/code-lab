/* 
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
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
/**
 *
 * TODO Bar说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-03-31  08:13
 *
 */
// 编译并用javap -c查看编译后的字节码
public class Bar {
    private int tryBlock;
    private int catchBlock;
    private int finallyBlock;
    private int methodExit;
  
    public void test() {
      for (int i = 0; i < 100; i++) {
        try {
          tryBlock = 0;
          if (i < 50) {
            continue;
          } else if (i < 80) {
            break;
          } else {
            return;
          }
        } catch (Exception e) {
          catchBlock = 1;
        } finally {
          finallyBlock = 2;
        }
      }
      methodExit = 3;
    }
  }
