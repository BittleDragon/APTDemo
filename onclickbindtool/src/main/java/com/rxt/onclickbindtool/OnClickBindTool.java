package com.rxt.onclickbindtool;

import android.app.Activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Desc:绑定工具类
 *
 * @author raoxuting
 * since 2019/3/11
 */
public class OnClickBindTool {

    public static void bind(Activity activity) {
        Class clazz = activity.getClass();
        try {
            Class onClickGeneratingClass = Class.forName(clazz.getName() + "_OnClick");
            Method method = onClickGeneratingClass.getDeclaredMethod("setOnClickListenerForView", clazz);
            method.setAccessible(true);
            method.invoke(onClickGeneratingClass.newInstance(), activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
