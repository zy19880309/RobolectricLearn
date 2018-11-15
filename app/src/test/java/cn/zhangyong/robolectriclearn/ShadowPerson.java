package cn.zhangyong.robolectriclearn;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * description:
 * autour: zhangyong
 * date: 2018/11/15 15:23
 * update: 2018/11/15
 * version:
 */
@Implements(Person.class)
public class ShadowPerson {
    // 重写其中一个方法
    @Implementation
    public String getName() {
        return "AndroidUT";
    }
}
