# Android_example_View03
android数据存储
step: 
    android 有三种基本的存储方式 
        其一 使用 InputStream 和 outPutStream 来读取和写入文件 已达到保存效果
        Q：如何在用户退出登陆后，重新打开app还是该数据？
    1.修改布局文件，添加editText框 来进行测试
    2.重写onDestroy方法，退出app时保存 当前的editText内容
    3.使用outPutStream写入文件！(java流操作)
    4.使用FileInputStream 读取文件，一行一行读取 返回读取后的字符串
    5.读取完毕在onCreate中添加判断条件 查看是否读取完毕->接着重启app显示读取内容
    
    
          其二：使用SharePreferences 它是使用键值对存储数据的，以下统称为Sp
    1.首先需要获取到Sp对象,Android主要提供了三种获取方式
       -> one: 使用 Context类中的getSharePreferences() 方法
        此方法接受两个参数，第一个参数 用于指定 SharePreferences 的文件名，如果不存在则重新创建一个
        第二个参数 指定操作模式，现在只有MODE_PRIVATE 这一种模式，作用是只有当前的应用程序才可以对SharePreferences 进行读写
       ->two : 使用Activity 的 getPreferences() 方法
        它只接受一个操作模式参数，默认会自动把当前活动的类名作为SharePreferences 的参数
       -> 使用PreferencesManager 的getDefaultSharePreferences 方法
       
       
       得到了 Sp对象之后  就可以向sp文件中存储数据了，主要可以分为三步实现
       1.调用Sp的edit() 方法 获取Sp.edit()对象
       2.使用 putBoolean,putString 等方法传值
       3.使用apply方法将添加的数据进行提交，从而完成数据存储操作！
       
       其三：使用 android 内置的sqlite 来执行CRUD，一般应对 复杂的数据 比如不用门户 的不同联系人 
        1. 创建 Sqlite类，写建表语句
        2. 创建点击按钮，点击 建表，并弹出 建表成功提示，后期可以维护更改
        3. 使用adb shell 去查询表信息！cd到手机设备database目录->键入Sqlite3+表名即可查询出来表 
        
            详细步骤：
            按钮点击之后
            ->
            打开cmd 使用adb devices 查询设备信息
            ->
            使用adb -s 指定设备地址+端口号 (PS:模拟器大多是127.0.0.1+固定端口)
            ->
            使用adb shell 进入linux 环境，接着 如果显示的是# 代表超级管理员，如果是$ 需要使用 su 切换
            ->
            接着ls查看文件目录，进入data\data\包名\databases
            ->
            接着使用 sqlite3 + 库(.db文件)
            Q:有的设备没有内置的sqlite3 所以会出现找不到bin ---的错误,怎么办才好？
            A:最简单的办法，换一台模拟器！ 最复杂的办法，根据报错信息去解决~~~
            
            Q:如果想再去创建一个分类，需要查库，需要新建表！但是我点击新建之后没有新建成功的提示，这是为什么？
            A：因为onCreate方法只会执行一次，除非卸载app让他重新执行，这样才会把另外一个表也新建出来
            
            Q：那除了卸载app外还有其他办法么?
            A:of Course 继承 SQLiteOpenHelper 会给我们提供一个onCreate 和 onUpgrade方法
            接着-> 我们在 onUpgrade 方法中使用 drop 语句删除掉之前新建的表，并且执行建表语句
            这样，每次版本发生改变的话，就会删除掉原来的Sqlite 表，重新新建！也就是现在 版本更新后更新 表的功能！
       