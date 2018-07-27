package cn.secret.base.created.shared;

import android.content.Context;

/**
 * ================================================
 * 作    者：贾恒飞
 * 项    目：Ax
 * 日    期：2018/7/26 0026
 * 包    名：cn.secret.base.bean
 * 描    述：SharedPreferences对象基类
 * ================================================
 */
public abstract class SharedPreferencesTrait {

    /**
     * 必须存在空的构造函数
     */
    public SharedPreferencesTrait() {}

    /**
     * 获取当前类中的文件名称,重写时如果为""或者null,则默认使用类名
     * @return 要保存的文件名称
     */
    public abstract String getSharedXmlName();

    /**
     * 设置存储模式,可重写,默认使用私有模式
     * @return Context.MODE_PRIVATE
     */
    public int getSharedType(){
//       -私有模式
//        Context.MODE_PRIVATE 的值是 0;
//        ①只能被创建这个文件的当前应用访问
//        ②若文件不存在会创建文件；若创建的文件已存在则会覆盖掉原来的文件
//       -追加模式
//        Context.MODE_APPEND 的值是 32768;
//        ①只能被创建这个文件的当前应用访问
//        ②若文件不存在会创建文件；若文件存在则在文件的末尾进行追加内容
//       -可读模式
//        Context.MODE_WORLD_READABLE的值是1;
//        ①创建出来的文件可以被其他应用所读取
//       -可写模式
//        Context.MODE_WORLD_WRITEABLE的值是2
//        ①允许其他应用对其进行写入。
        return Context.MODE_PRIVATE;
    }

}
