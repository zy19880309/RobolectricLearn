package cn.zhangyong.robolectriclearn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.robolectric.shadow.api.Shadow.extract;

/**
 * description:
 * autour: zhangyong
 * date: 2018/11/15 15:24
 * update: 2018/11/15
 * version:
 */
// @Config 指定这个 Shadow 类
@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowPerson.class})
public class ShadowTest {

    @Test
    public void testShadowShadow() {
        Person person = new Person();
        //实际上调用的是ShadowPerson的方法
        System.out.println(person.getName());

        //获取 Person 对象对应的 Shadow 对象
        ShadowPerson shadowPerson = extract(person);
        assertEquals("AndroidUT", shadowPerson.getName());
    }
}
