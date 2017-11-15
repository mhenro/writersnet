package org.booklink.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by mhenr on 16.11.2017.
 */
public class ObjectHelperTest {
    private TestObject src;

    @Before
    public void init() {
        src = new TestObject(null, null);
    }

    @Test
    public void allNulls() throws Exception {
        String[] result = ObjectHelper.getNullPropertyNames(src);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals("a", result[0]);
        Assert.assertEquals("b", result[1]);
    }

    @Test
    public void firstNull() throws Exception {
        src.a = 10;
        String[] result = ObjectHelper.getNullPropertyNames(src);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals("b", result[0]);
    }

    @Test
    public void secondNull() throws Exception {
        src.b = 10;
        String[] result = ObjectHelper.getNullPropertyNames(src);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals("a", result[0]);
    }

    @Test
    public void withoutNulls() throws Exception {
        src.a = 10;
        src.b = 20;
        String[] result = ObjectHelper.getNullPropertyNames(src);
        Assert.assertEquals(0, result.length);
    }


    /* ---------------------------------------------------------------------- */



    public class TestObject {
        private Integer a;
        private Integer b;

        public TestObject(final Integer a, final Integer b) {
            this.a = a;
            this.b = b;
        }

        public Integer getA() {
            return a;
        }

        public void setA(Integer a) {
            this.a = a;
        }

        public Integer getB() {
            return b;
        }

        public void setB(Integer b) {
            this.b = b;
        }
    }
}
