### 附加功能

1. 添加权限:\
   `<uses-permission android:name="com.termux.permission.RUN_COMMAND"/>`到 AndroidManifest.xml 中。
2. 在关于界面添加介绍按钮：附加功能。
3. 在设置界面添加功能按钮：Termux权限申请。
4. 执行Javascript代码中，sendIntent 函数支持数组参数放入到intent的extra中。修改部分如下：\
   <pre>
   // /ch/rmy/android/http_shortcuts/scripting/actions/types/SendIntentAction.kt
   
   // ...
   private const val EXTRA_TYPE_STRINGARR = "string[]"
   // ...
   
   when (extra.optString(KEY_EXTRA_TYPE)) {
       // ... 
       EXTRA_TYPE_STRINGARR -> {
          putExtra(name, extra.optJSONArray(KEY_EXTRA_VALUE)?.toListOfStrings()?.toTypedArray())
       }
       // ...
   }
   
   // ...

   </pre>

这样一来，就可以这样使用sendIntent：
 
<pre>
sendIntent({
    type: "service",
    action: "com.termux.RUN_COMMAND",
    packageName: "com.termux",
    className: "com.termux.app.RunCommandService",
    extras: [
        {
            name: "com.termux.RUN_COMMAND_PATH",
            type: "string",
            value: "/data/data/com.termux/files/usr/bin/ping",
        },{
            name: "com.termux.RUN_COMMAND_ARGUMENTS",
            type: "string[]",
            value: ["192.168.1.1"],
    }]
})
</pre>