# Android数据存储
 step: 
    android 有三种基本的存储方式 
## 其一 使用 InputStream 和 outPutStream 来读取和写入文件 已达到保存效果
Q：如何在用户退出登陆后，重新打开app还是该数据？ <br/>
    1.修改布局文件，添加editText框 来进行测试 <br/>
    2.重写onDestroy方法，退出app时保存 当前的editText内容 <br/>
    3.使用outPutStream写入文件！(java流操作) <br/>
    4.使用FileInputStream 读取文件，一行一行读取 返回读取后的字符串 <br/>
    5.读取完毕在onCreate中添加判断条件 查看是否读取完毕->接着重启app显示读取内容 <br/>
## 其二：使用 SharePreferences 它是使用键值对存储数据的，以下统称为Sp
首先需要获取到Sp对象,Android主要提供了三种获取方式 <br/>
       -> one: 使用 Context类中的getSharePreferences() 方法 <br/>
       此方法接受两个参数，第一个参数 用于指定 SharePreferences 的文件名，如果不存在则重新创建一个 <br/>
       第二个参数 指定操作模式，现在只有MODE_PRIVATE 这一种模式，作用是只有当前的应用程序才可以对SharePreferences 进行读写 <br/>
       ->two : 使用Activity 的 getPreferences() 方法 <br/>
       它只接受一个操作模式参数，默认会自动把当前活动的类名作为SharePreferences 的参数 <br/>
       -> 使用PreferencesManager 的getDefaultSharePreferences 方法 <br/>
       得到了 Sp对象之后  就可以向sp文件中存储数据了，主要可以分为三步实现 <br/>
       1.调用Sp的edit() 方法 获取Sp.edit()对象 <br/>
       2.使用 putBoolean,putString 等方法传值 <br/>
       3.使用apply方法将添加的数据进行提交，从而完成数据存储操作！ <br/>
## 其三：使用 android 内置的sqlite 来执行CRUD，一般应对 复杂的数据 比如不用门户 的不同联系人 
1. 创建 Sqlite类，写建表语句 <br/>
        2. 创建点击按钮，点击 建表，并弹出 建表成功提示，后期可以维护更改 <br/>
        3. 使用adb shell 去查询表信息！cd到手机设备database目录->键入Sqlite3+表名即可查询出来表 <br/> 
            详细步骤： <br/>
            按钮点击之后 <br/>
            -> <br/>
            打开cmd 使用adb devices 查询设备信息 <br/>
            -> <br/>
            使用adb -s 指定设备地址+端口号 (PS:模拟器大多是127.0.0.1+固定端口) <br/>
            -> <br/>
            使用adb shell 进入linux 环境，接着 如果显示的是# 代表超级管理员，如果是$ 需要使用 su 切换 <br/>
            -> <br/>
            接着ls查看文件目录，进入data\data\包名\databases <br/>
            -> <br/>
            接着使用 sqlite3 + 库(.db文件) <br/>
            Q:有的设备没有内置的sqlite3 所以会出现找不到bin ---的错误,怎么办才好？ <br/>
            A:最简单的办法，换一台模拟器！ 最复杂的办法，根据报错信息去解决~~~            <br/> 
            Q:如果想再去创建一个分类，需要查库，需要新建表！但是我点击新建之后没有新建成功的提示，这是为什么？ <br/>
            A：因为onCreate方法只会执行一次，除非卸载app让他重新执行，这样才会把另外一个表也新建出来 <br/>
            Q：那除了卸载app外还有其他办法么? <br/>
            A:of Course 继承 SQLiteOpenHelper 会给我们提供一个onCreate 和 onUpgrade方法 <br/>
            接着-> 我们在 onUpgrade 方法中使用 drop 语句删除掉之前新建的表，并且执行建表语句 <br/>
            这样，每次版本发生改变的话，就会删除掉原来的Sqlite 表，重新新建！也就是现在 版本更新后更新 表的功能！ <br/>
            Q：add data 数据之后 cmd命令打开没有查出表信息,为什么 <br/>
            A：表名一定要匹配,传递的值得类型也需要匹配！不然 查表什么都查不到，就是空白的！ <br/>
       ----------------------------更新 2019年10月3日16点25分 <br/>
        Q：如何使用LietPal进行查库操作？ <br/>
        A:1.导入依赖 -> 添加application-> 添加 access文件 之后 CRUD实验就可以了 <br/>       
        Q：那它到底有什么用呢？他相对比 OutPutStream + SharePreferences + Sqlite 有哪些用处呢？ <br/>
        A：其实不难理解，从以前的 电报到现在的计算机，都在发展，所以，技术是不断地发展 突破的，这同时也是 Android数据库的优化 框架！ 就好比 莫尔斯码，到布莱叶盲文！再到c , c++ ,java ,python等等 <br/>
        Q:如何使用 LitePal 给数据库添加 数据？ <br/>
        A：实体类 继承 LitePalSupport -> 接着给对象赋值->使用 LitePalSupport 里面的 save()方法保存最后的数据-> 使用 select * from Book; 去查询 <br/>
        Q：如何使用LitePal 更新数据呢？ <br/>
        A：LitePal 提供了两种更新数据的方式，一种是对已存储的对象进行更新！一种是使用update去更新！ <br/>
       
      
 ## 更新 替换建表成功 为 --> ContentProvider 的跨进程共享数据 
     Q:这么做的好处是什么？<br/>
     A：可以让所有的App都共享该数据库表的数据，也就是说在其他app，查询指定URI就可以获取到这个库的所有数据！<br/>
     <br/>
     <b>step:</b><br/>
        1.去除MySqilte里面的Toast吐司，因为太low!<br/>
        2.创建一个 ContentProvider 使用android 提供的new -> other -> Content Provider <br/>
        3.首先 定义四个常量 分别是 BOOK_DIR,BOOK_ITEM,CATEGORY_DIR,CATEGORY_ITEM,用来判断表所匹配的uri!<br/>
        4.接着创建 static 代码块，用来添加 对应的内容提供者的uri;<br/>
        5.在OnCreate里面 使用 自定义的 MySqlite 创建和升级表，并且返回true 代表创建成功！<br/>
        6.接着 使用 我们定义的四个常量判断表的 uri，从而进行 CRUD 操作！<br/>
        7.接着使用getType()返回 MIME 类型的字符串 格式如下 ：如果通配符为/* -> vnd.android.cursor.dir/vnd.android.content.provider.table1<br/>
        8.在 AndroidManifest.xml 里面对ContentProvider进行注册，使用<privider></provider> 创建 ContentProvider<br/>
        9.创建新的项目，布局里面定义四个按钮,分别是 CRUD 可以用 Multiplexing litePal 的布局<br/>
       