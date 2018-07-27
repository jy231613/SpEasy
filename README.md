# SpEasy

#### 项目介绍
安卓存储工具SharedPreferences的orm封装,通过对对象的操作,完成数据的读取和存储,简单好用,存储和读取只需要一句话调用!

#### 软件架构
SharedPreferencesUtil:简单封装的工具类,可以直接使用,但是每次操作都需要自己处理异常;
SharedPreferencesTrait:封装对象,所有模型类文件都要继承自这个类才有效;
SpEasy:最终封装结果,在Application中初始化之后,可以做到一行代码存储,一行代码读取数据,通过操作对象,完成数据的操作;


#### 使用教程

1. 下载speasy文件夹中的三个文件,copy到你的项目中;
2. 在Application中进行初始化,在初始化时如果不指定全局的异常处理,就会默认调用一个空的异常处理;
    `SpEasy.init(this,this);或者SpEasy.init(this);`
![初始化示例图](https://images.gitee.com/uploads/images/2018/0727/111413_232596e5_1883971.png "效果图1.png")
3. 书写一个模型类,并且继承自SharedPreferencesTrait类,赋予其参数的get和set方法;
![模型类](https://images.gitee.com/uploads/images/2018/0727/111703_73b5fd36_1883971.png "效果图2.png")
4. 通过操作类来存储和删除数据
![存储数据](https://images.gitee.com/uploads/images/2018/0727/111809_d9b430dc_1883971.png "效果图3.png")
![查询数据](https://images.gitee.com/uploads/images/2018/0727/111859_d06f0eca_1883971.png "效果图4.png")

#### 注意事项

1. 
2. xxxx
3. xxxx

#### 联系我

1. 码云:@zFox 
2. GitHub:jy231613
3. CSDN:[https://blog.csdn.net/qq_36676433](https://blog.csdn.net/qq_36676433)
4. wechat:jy271613
5. QQ:2757525207
    
