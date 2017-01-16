package com.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 参数读写类
 *
 * @author xmq
 */
public final class XPreferencesService {

    private static SharedPreferences sharedPre;

    public final static SharedPreferences getInstance(Context context) {
        synchronized (XPreferencesService.class) {
            if (sharedPre == null) {
                synchronized (XPreferencesService.class) {
                    sharedPre = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
                }
            }
        }
        return sharedPre;
    }

    /**
     * 获取editor 编辑器
     *
     * @param context
     */
    public Editor getPreferEditor(Context context) {
        return getInstance(context).edit();
    }

    /**
     * 获取指定名称参数
     *
     * @param ctn
     * @param getName     获取保存的参数名称
     * @param defaultInfo 如果无法得到，则得到
     * @return 保存的参数
     */
    public String getInfo(Context ctn, String getName, String defaultInfo) {
        if(ctn==null){
            return defaultInfo;
        }
        SharedPreferences preferences = getInstance(ctn);
        return preferences.getString(getName, defaultInfo);
    }

    /**
     * 获取指定名称参数
     *
     * @param ctn
     * @param getName     获取保存的参数名称
     * @param defaultInfo 如果无法得到，则得到
     * @return 保存的参数
     */
    public int getInfo(Context ctn, String getName, int defaultInfo) {
        if (ctn == null)
            return defaultInfo;
        SharedPreferences preferences = getInstance(ctn);
        return preferences.getInt(getName, defaultInfo);
    }

    /**
     * 获取指定名称参数
     *
     * @param ctn
     * @param getName     获取保存的参数名称
     * @param defaultInfo 如果无法得到，则得到
     * @return 保存的参数
     */
    public boolean getInfo(Context ctn, String getName, boolean defaultInfo) {
        if (ctn == null)
            return defaultInfo;
        SharedPreferences preferences = getInstance(ctn);
        return preferences.getBoolean(getName, defaultInfo);
    }

    /**
     * 获取指定名称参数
     *
     * @param ctn
     * @param getName     获取保存的参数名称
     * @param defaultInfo 如果无法得到，则得到
     * @return 保存的参数
     */
    public float getInfo(Context ctn, String getName, float defaultInfo) {
        if (ctn == null)
            return defaultInfo;
        SharedPreferences preferences = getInstance(ctn);
        return preferences.getFloat(getName, defaultInfo);
    }

    /**
     * 获取参数
     *
     * @param ctn
     * @param name 保存的参数名称
     */
    public String[] getInfoArray(Context ctn, String name) {
        if (ctn == null)
            return null;
        SharedPreferences preferences = getInstance(ctn);
        int len = preferences.getInt(name + "len", 0);
        String[] result = new String[len];
        for (int i = 0; i < len; i++)
            result[i] = preferences.getString(name + i, "");
        return result;
    }

    /**
     * 查看sp中是否包含该值
     *
     * @param ctx
     * @param key 关键字
     */
    public boolean isContains(Context ctx, String key) {
        SharedPreferences preferences = getInstance(ctx);
        return preferences.contains(key);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public void clear(Context context) {
        Editor editor = getPreferEditor(context);
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 获取指定名称参数
     *
     * @param ctn
     * @param getName     获取保存的参数名称
     * @param defaultInfo 如果无法得到，则得到
     * @return 保存的参数
     */
    public float getInfo(Context ctn, String getName, long defaultInfo) {
        if (ctn == null)
            return defaultInfo;
        SharedPreferences preferences = getInstance(ctn);
        return preferences.getLong(getName, defaultInfo);
    }

    /**
     * 保存参数为指定名称
     *
     * @param ctn
     * @param name 保存的参数名称
     * @param info 保存的参数
     */
    public void saveInfo(Context ctn, String name, String info) {
        if (ctn == null)
            return;
        Editor editor = getPreferEditor(ctn);
        editor.putString(name, info);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 保存参数为指定名称
     *
     * @param ctn
     * @param name 保存的参数名称
     * @param info 保存的参数
     */
    public void saveInfoArray(Context ctn, String name, String[] info) {
        if (ctn == null)
            return;
        Editor editor = getPreferEditor(ctn);
        int len = info.length;
        editor.putInt(name + "len", len);
        for (int i = 0; i < len; i++) {
            editor.putString(name + i, info[i]);
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 保存参数为指定名称
     *
     * @param ctn
     * @param name 保存的参数名称
     * @param info 保存的参数
     */
    public void saveInfo(Context ctn, String name, int info) {
        if (ctn == null)
            return;
        Editor editor = getPreferEditor(ctn);
        editor.putInt(name, info);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 保存参数为指定名称
     *
     * @param ctn
     * @param name 保存的参数名称
     * @param info 保存的参数
     */
    public void saveInfo(Context ctn, String name, boolean info) {
        if (ctn == null)
            return;
        Editor editor = getPreferEditor(ctn);
        editor.putBoolean(name, info);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 获取指定名称参数
     *
     * @param ctn
     * @param name 获取保存的参数名称
     * @param info 如果无法得到，则默认值
     * @return 保存的参数
     */
    public void saveInfo(Context ctn, String name, float info) {
        if (ctn == null)
            return;
        Editor editor = getPreferEditor(ctn);
        editor.putFloat(name, info);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 获取指定名称参数
     *
     * @param ctn
     * @param name 获取保存的参数名称
     * @param info 如果无法得到，则默认值
     * @return 保存的参数
     */
    public void saveInfo(Context ctn, String name, long info) {
        if (ctn == null)
            return;
        Editor editor = getPreferEditor(ctn);
        editor.putLong(name, info);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 保存用户密码信息方法
     *
     * @param name
     * @param pwd
     * @param bool
     */
    public void save(Context ctn, String name, String pwd, boolean bool) {
        if (ctn == null)
            return;
        Editor editor = getPreferEditor(ctn);
        editor.putString("name", name);
        editor.putString("rempwd", bool + "");
        editor.putString("pwd", bool ? pwd : "");
        editor.commit();
    }

    /**
     * 读取本地参数用户信息方法
     * <p>一般用于登陆记住用户名密码</p>
     *
     * @return 数据集合
     */
    public HashMap<String, String> getPerMap(Context ctn) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (ctn != null) {
            SharedPreferences preferences = getInstance(ctn);
            map.put("name", preferences.getString("name", ""));
            map.put("pwd", preferences.getString("pwd", ""));
            map.put("rempwd", preferences.getString("rempwd", "false"));
        }
        return map;
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}
