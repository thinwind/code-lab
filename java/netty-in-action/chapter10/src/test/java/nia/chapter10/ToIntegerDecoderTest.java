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
package nia.chapter10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 *
 * TODO ToIntegerDecoderTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-04-09  14:17
 *
 */
public class ToIntegerDecoderTest {

    @Test
    public void testDecode() {
        ByteBuf buf = Unpooled.buffer();
        byte[] source = new byte[12];
        for (int i = 10; i < 22; i++) {
            buf.writeByte(i);
            source[i-10] = (byte) i;
        }

        EmbeddedChannel channel = new EmbeddedChannel(new ToIntegerDecoder());
        assertTrue(channel.writeInbound(buf));

        // read bytes
        byte[] first = new byte[4];
        System.arraycopy(source, 0, first, 0, 4);
        assertEquals(BitUtil.joinBytesToInt(first), channel.readInbound());

        byte[] second = new byte[4];
        System.arraycopy(source, 4, second, 0, 4);
        assertEquals(BitUtil.joinBytesToInt(second), channel.readInbound());
        
        byte[] third = new byte[4];
        System.arraycopy(source, 8, third, 0, 4);
        assertEquals(BitUtil.joinBytesToInt(third), channel.readInbound());
        
        assertNull(channel.readInbound());
    }
}
