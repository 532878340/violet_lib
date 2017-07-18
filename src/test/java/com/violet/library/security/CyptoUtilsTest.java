package com.violet.library.security;

import org.junit.Before;
import org.junit.Test;

/**
 * description：
 * author：JimG on 17/4/7 09:14
 * e-mail：info@zijinqianbao@qq.com
 */

public class CyptoUtilsTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void encode() throws Exception {
        String encode = CyptoUtils.encode("123456");
        System.out.print(encode);
    }

}