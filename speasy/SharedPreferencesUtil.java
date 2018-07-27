package cn.secret.base.created.shared;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * ================================================
 * 作    者：贾恒飞
 * 项    目：base
 * 日    期：2018/7/26 0026
 * 包    名：cn.secret.base.util
 * 描    述：SharedPreferences存储工具类
 * ================================================
 */
public class SharedPreferencesUtil {

    /**
     * 获取一个SharedPreferences对象
     * @param context 上下文关系
     * @param tClass T的类型
     * @param <T> 继承自SharedPreferencesTrait
     * @return SharedPreferences对象
     * @throws IllegalAccessException 安全权限异常
     * @throws InstantiationException 实例化异常
     */
    public static <T extends SharedPreferencesTrait> SharedPreferences getInfo(Context context,Class<T> tClass)
            throws IllegalAccessException, InstantiationException {
        T obj = tClass.newInstance();
        String xmlName = obj.getSharedXmlName();
        if (xmlName == null || xmlName.length()==0){
            xmlName = tClass.getSimpleName();
        }
        return context.getSharedPreferences(xmlName,obj.getSharedType());
    }

    /**
     * 存储int类型值
     * @param sharedPreferences 存储对象
     * @param key key值
     * @param value value值
     * @return 是否存储成功
     */
    public static boolean putInt(SharedPreferences sharedPreferences,String key,int value){
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putInt(key,value);
       return editor.commit();//保存
    }

    /**
     * 存储String类型值
     * @param sharedPreferences 存储对象
     * @param key key值
     * @param value value值
     * @return 是否存储成功
     */
    public static boolean putString(SharedPreferences sharedPreferences,String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        return editor.commit();//保存
    }

    /**
     * 存储float类型的值
     * @param sharedPreferences 存储对象
     * @param key key值
     * @param value value值
     * @return 是否存储成功
     */
    public static boolean putFloat(SharedPreferences sharedPreferences,String key,float value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key,value);
        return editor.commit();//保存
    }

    /**
     * 存储Long类型的值
     * @param sharedPreferences 存储对象
     * @param key key值
     * @param value value值
     * @return 是否存储成功
     */
    public static boolean putLong(SharedPreferences sharedPreferences,String key,long value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key,value);
        return editor.commit();//保存
    }

    /**
     * 存储布尔类型的值
     * @param sharedPreferences 存储对象
     * @param key key值
     * @param value value值
     * @return 是否存储成功
     */
    public static boolean putBoolean(SharedPreferences sharedPreferences,String key,boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        return editor.commit();//保存
    }

    /**
     * 存储Set<String>类型的值
     * @param sharedPreferences 存储对象
     * @param key key值
     * @param value value值
     * @return 是否存储成功
     */
    public static boolean putStringSet(SharedPreferences sharedPreferences,String key,Set<String> value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key,value);
        return editor.commit();//保存
    }

    /**
     * 根据一个模型类型 生成一个对象
     * @param context 上下文关系
     * @param tClass 类对象
     * @param <T> 继承自SharedPreferencesTrait
     * @return T对象
     * @throws IllegalAccessException 安全权限异常
     * @throws InstantiationException 实例化异常
     * @throws InvocationTargetException 反射异常
     * @注意 尚未支持Set<String>如果获取到的值中包含Set集合,可以使用上述
     * @See SpEasy.putStringSet()方法
     */
    public static <T extends SharedPreferencesTrait> T obtainShared(Context context,Class<T> tClass)
            throws IllegalAccessException, InstantiationException, InvocationTargetException{
        T obj = tClass.newInstance();
        String xmlName = obj.getSharedXmlName();
        if (xmlName == null || xmlName.length()==0){
            xmlName = tClass.getSimpleName();
        }
        SharedPreferences sp = context.getSharedPreferences(xmlName,obj.getSharedType());
        // 得到对象中所有的方法
        Method[] methods = tClass.getMethods();
        for (Method method:methods) {
            //拿到除所有set方法名
            String mName = method.getName();
            if (mName.startsWith("set")) {
                String fieldName = mName.substring(3, mName.length());//截取出变量名称
                Class[] parmts = method.getParameterTypes();
                // 根据参数类型，从sp中按照列名取得对应的值，并且执行改set方法
                if (parmts[0] == String.class) {
                    method.invoke(obj,sp.getString(fieldName,""));
                }
                if (parmts[0]== int.class) {
                    method.invoke(obj,Integer.valueOf(sp.getString(fieldName,"0")));
                }
                if (parmts[0]== long.class) {
                    method.invoke(obj,Long.valueOf(sp.getString(fieldName,"0")));
                }
                if (parmts[0]== boolean.class) {
                    method.invoke(obj,Boolean.valueOf(sp.getString(fieldName,"false")));
                }
                if (parmts[0]== float.class) {
                    method.invoke(obj,Float.valueOf(sp.getString(fieldName,"0")));
                }
            }
        }
        return obj;
    }

    /**
     * 将一个模型类对象存储到SharedPreferences中
     * @param context 上下文对象
     * @param t 模型类对象
     * @param <T> 继承自SharedPreferencesTrait
     * @throws InvocationTargetException 反射异常
     * @throws IllegalAccessException 安全权限异常
     */
    public static <T extends SharedPreferencesTrait> void saveShared(Context context,T t)
            throws InvocationTargetException, IllegalAccessException {
        String xmlName = t.getSharedXmlName();
        if (xmlName == null || xmlName.length()==0){
            xmlName = t.getClass().getSimpleName();
        }
        SharedPreferences sp = context.getSharedPreferences(xmlName,t.getSharedType());
        SharedPreferences.Editor editor = sp.edit();
        Map<String,?> map = sp.getAll();
        // 得到对象中所有的方法
        Method[] methods = t.getClass().getMethods();
        for (Method method:methods) {
            //拿到除所有get方法名
            String mName = method.getName();
            if (mName.startsWith("get") && !mName.startsWith("getClass")) {
                String fieldName = mName.substring(3, mName.length());//截取出变量名称
                Object value= method.invoke(t,null);
                editor.putString(fieldName,String.valueOf(value));
            }else if (mName.startsWith("is")){
                String fieldName = mName.substring(2, mName.length());//截取出变量名称
                Object value= method.invoke(t,null);
                editor.putString(fieldName,String.valueOf(value));
            }
            editor.apply();//提交
        }
    }
}
