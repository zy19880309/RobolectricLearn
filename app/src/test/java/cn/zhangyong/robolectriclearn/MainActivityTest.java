package cn.zhangyong.robolectriclearn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowToast;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * description:
 * autour: zhangyong
 * date: 2018/11/14 17:49
 * update: 2018/11/14
 * version:
 */
@RunWith(RobolectricTestRunner.class)
/*@Config(sdk = {JELLY_BEAN, JELLY_BEAN_MR1},
        application = CustomApplication.class,
        manifest = "--default",
        resourceDir = "some/build/path/res",
        qualifiers = "en-port"*//*限定用哪个资源values*//*)*/
public class MainActivityTest {
    @Test
    public void setup() {
        // setupActivity 会依次执行 Activity 的 onCreate, onStart, onResume
        Activity activity = Robolectric.setupActivity(MainActivity.class);
        assertNotNull(activity);
        TextView textview = activity.findViewById(R.id.tv);
        // 执行完 onResume 后文字变了
        assertEquals("onResume", textview.getText().toString());
    }

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        // setupActivity 会依次执行 Activity 的 onCreate, onStart, onResume
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        activity.findViewById(R.id.btn_login).performClick(); // 模拟执行一个点击

        Intent expectedIntent = new Intent(activity, LoginActivity.class); // 期望的 Intent
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity(); // 真实获取到的 Intent
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void clickingLogin_shouldStartLoginActivity2() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        activity.findViewById(R.id.btn_login).performClick(); // 模拟执行一个点击

        Intent expectedIntent = new Intent(activity, LoginActivity.class); // 期望的 Intent

        ShadowActivity activity2 = Shadows.shadowOf(activity);
        Intent actual2 = activity2.getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual2.getComponent());
    }

    @Test
    public void testLifecycle() {
        ActivityController<MainActivity> activityController = Robolectric.buildActivity(MainActivity.class).create().start();
        Activity activity = activityController.get(); // get 前已执行 create，start，那么这个 Activity 还没有执行过 resume
        TextView textview = (TextView) activity.findViewById(R.id.tv);
        assertEquals("onCreate", textview.getText().toString());
        activityController.resume(); // 之后执行 resume
        assertEquals("onResume", textview.getText().toString());
    }

    @Test
    @Config(qualifiers = "en-port") // 限定用哪个资源
    public void shouldUseEnglishAndPortraitResources() {
        final Context context = RuntimeEnvironment.application;
        // en-port 没有，en 也没有，用默认的
        assertThat(context.getString(R.string.not_overridden), equalTo("Not Overridden"));
        // en-port 没有，用 en 的
        assertThat(context.getString(R.string.overridden), equalTo("English qualified value"));
        assertThat(context.getString(R.string.overridden_twice), equalTo("English portrait qualified value"));
    }

    @Test
    public void clickBtn_shouldShowToast() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);

        Toast toast = ShadowToast.getLatestToast();
        assertNull(toast); // 判断 Toast 尚未弹出

        activity.findViewById(R.id.btn_toast).performClick();

        toast = ShadowToast.getLatestToast();
        assertNotNull(toast); // 判断Toast已经弹出

        ShadowToast shadowToast = Shadows.shadowOf(toast); // 获取 ShadowToast
        assertEquals(Toast.LENGTH_LONG, shadowToast.getDuration());
        assertEquals("Show Toast", ShadowToast.getTextOfLatestToast());
    }


}
