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
 * 描    述：SpEasy超级简单的存储工具类
 * ================================================
 */
public class SpEasy {
    private Context context;//上下文对象
    private SpException dispose;//全局异常处理
    private static SpEasy single;//单例对象
    private SpEasy(Context context, SpException dispose){
        initFor(context, dispose);
    }//单例构造
    private SpEasy(Context context){
        initFor(context, null);
    }//单例构造

    /**
     * 获取本类单例对象
     * @return 注意,调用本方法之前请先调用静态的init()方法
     */
    public static SpEasy getSingle() {
        return single;
    }

    /**
     * 构造初始化方法
     * @param context 上下文对象
     * @param dispose 全局异常处理对象
     */
    private void initFor(Context context,SpException dispose){
        if (dispose == null){
            this.dispose = new SpEpsImpl();
        }else{
            this.dispose = dispose;
        }
        this.context = context;
    }

    /**
     * 本类的全局异常处理方法
     */
    public interface SpException{
        /**
         * 处理安全权限异常
         * @param e 异常对象
         */
        void disposeIllegalAccess(IllegalAccessException e);

        /**
         * 处理实例化异常
         * @param e 异常对象
         */
        void disposeInstantiation(InstantiationException e);

        /**
         * 处理反射异常
         * @param e 异常对象
         */
        void disposeInvocationTarget(InvocationTargetException e);
    }

    /**
     * 初始化方法
     * @param context 上下文对象
     * @param dispose 异常处理对象
     */
    public static void init(Context context,SpException dispose){
        if (single==null) single = new SpEasy(context,dispose);
    }

    /**
     * 初始化方法
     * @param context 上下文对象
     */
    public static void init(Context context){
        if (single==null) single = new SpEasy(context);
    }

    /**
     * 获取一个SharedPreferences对象
     * @param tClass T的类型
     * @param <T> 继承自SharedPreferencesTrait
     * @return SharedPreferences对象
     */
    public <T extends SharedPreferencesTrait> SharedPreferences getInfo(Class<T> tClass){
        T obj = null;
        try {
            obj = tClass.newInstance();
        } catch (InstantiationException e) {
            dispose.disposeInstantiation(e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            dispose.disposeIllegalAccess(e);
            e.printStackTrace();
        }
        assert obj != null;
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
    public boolean putInt(SharedPreferences sharedPreferences,String key,int value){
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
    public boolean putString(SharedPreferences sharedPreferences,String key,String value){
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
    public boolean putFloat(SharedPreferences sharedPreferences,String key,float value){
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
    public boolean putLong(SharedPreferences sharedPreferences,String key,long value){
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
    public boolean putBoolean(SharedPreferences sharedPreferences,String key,boolean value){
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
    public boolean putStringSet(SharedPreferences sharedPreferences,String key,Set<String> value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key,value);
        return editor.commit();//保存
    }

    /**
     * 根据一个模型类型 生成一个对象
     * @param tClass 类对象
     * @param <T> 继承自SharedPreferencesTrait
     * @return T对象
     * @注意 尚未支持Set<String>如果获取到的值中包含Set集合,可以使用上述
     * @See SpEasy.putStringSet()方法
     */
    public <T extends SharedPreferencesTrait> T obtainShared(Class<T> tClass){
        T obj = null;
        try {
            obj = tClass.newInstance();
        } catch (InstantiationException e) {
            dispose.disposeInstantiation(e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            dispose.disposeIllegalAccess(e);
            e.printStackTrace();
        }
        assert obj != null;
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
                    try {
                        method.invoke(obj,sp.getString(fieldName,""));
                    } catch (IllegalAccessException e) {
                        dispose.disposeIllegalAccess(e);
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        dispose.disposeInvocationTarget(e);
                        e.printStackTrace();
                    }
                }
                if (parmts[0]== int.class) {
                    try {
                        method.invoke(obj,Integer.valueOf(sp.getString(fieldName,"0")));
                    } catch (IllegalAccessException e) {
                        dispose.disposeIllegalAccess(e);
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        dispose.disposeInvocationTarget(e);
                        e.printStackTrace();
                    }
                }
                if (parmts[0]== long.class) {
                    try {
                        method.invoke(obj,Long.valueOf(sp.getString(fieldName,"0")));
                    } catch (IllegalAccessException e) {
                        dispose.disposeIllegalAccess(e);
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        dispose.disposeInvocationTarget(e);
                        e.printStackTrace();
                    }
                }
                if (parmts[0]== boolean.class) {
                    try {
                        method.invoke(obj,Boolean.valueOf(sp.getString(fieldName,"false")));
                    } catch (IllegalAccessException e) {
                        dispose.disposeIllegalAccess(e);
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        dispose.disposeInvocationTarget(e);
                        e.printStackTrace();
                    }
                }
                if (parmts[0]== float.class) {
                    try {
                        method.invoke(obj,Float.valueOf(sp.getString(fieldName,"0")));
                    } catch (IllegalAccessException e) {
                        dispose.disposeIllegalAccess(e);
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        dispose.disposeInvocationTarget(e);
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }

    /**
     * 将一个模型类对象存储到SharedPreferences中
     * @param t 模型类对象
     * @param <T> 继承自SharedPreferencesTrait
     */
    public  <T extends SharedPreferencesTrait> void saveShared(T t){
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
                Object value= null;
                try {
                    value = method.invoke(t,null);
                } catch (IllegalAccessException e) {
                    dispose.disposeIllegalAccess(e);
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    dispose.disposeInvocationTarget(e);
                    e.printStackTrace();
                }
                editor.putString(fieldName,String.valueOf(value));
            }else if (mName.startsWith("is")){
                String fieldName = mName.substring(2, mName.length());//截取出变量名称
                Object value= null;
                try {
                    value = method.invoke(t,null);
                } catch (IllegalAccessException e) {
                    dispose.disposeIllegalAccess(e);
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    dispose.disposeInvocationTarget(e);
                    e.printStackTrace();
                }
                editor.putString(fieldName,String.valueOf(value));
            }
            editor.apply();//提交
        }
    }

    /**
     * 空的异常处理办法
     */
    private class SpEpsImpl implements SpException{
        @Override
        public void disposeIllegalAccess(IllegalAccessException e) {}
        @Override
        public void disposeInstantiation(InstantiationException e) {}
        @Override
        public void disposeInvocationTarget(InvocationTargetException e) {}
    }
}
