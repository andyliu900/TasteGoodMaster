package com.tastegood.master.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by surandy on 16/5/24.
 */
public class SPUtils {

    private static Context mContext;

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void put(String key, Object object) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {  // 默认以序列化的对象保存
            saveObject(key, object);
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(String key, Object defaultObject) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return readObject(key);
        }

    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     */
    public static void clear() {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
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
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
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
        public static void apply(SharedPreferences.Editor editor) {
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

    /**
     * desc:保存对象
     * @param key
     * @param obj 要保存的对象，只能保存实现了serializable的对象
     */
    private static void saveObject(String key ,Object obj) {
        try {
            // 保存对象
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            ObjectOutputStream os=new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            put(key, bytesToHexString);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("", "保存obj失败");
        }
    }

    /**
     * 将数组转为16进制
     * @param bArray
     * @return
     */
    private static String bytesToHexString(byte[] bArray) {
        if(bArray == null){
            return null;
        }
        if(bArray.length == 0){
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 获取保存的Object对象
     * @param key
     * @return
     */
    private static Object readObject(String key) {
        try {
            if (contains(key)) {
                String string = (String)get(key, "");
                if(TextUtils.isEmpty(string)){
                    return null;
                }else{
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = stringToBytes(string);
                    ByteArrayInputStream bis=new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is=new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    Object readObject = is.readObject();
                    return readObject;
                }
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //所有异常返回null
        return null;
    }

    /**
     * 将16进制的数据转为数组
     * @param data
     * @return
     */
    private static byte[] stringToBytes(String data) {
        String hexString=data.toUpperCase().trim();
        if (hexString.length()%2!=0) {
            return null;
        }
        byte[] retData=new byte[hexString.length()/2];
        for(int i=0;i<hexString.length();i++)
        {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch1;
            if(hex_char1 >= '0' && hex_char1 <='9')
                int_ch1 = (hex_char1-48)*16;   //// 0 的Ascll - 48
            else if(hex_char1 >= 'A' && hex_char1 <='F')
                int_ch1 = (hex_char1-55)*16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch2;
            if(hex_char2 >= '0' && hex_char2 <='9')
                int_ch2 = (hex_char2-48); //// 0 的Ascll - 48
            else if(hex_char2 >= 'A' && hex_char2 <='F')
                int_ch2 = hex_char2-55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1+int_ch2;
            retData[i/2]=(byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }

}
