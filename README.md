# RecTest

启动主activity

> adb shell am start ai.t.rec/.Main


开始录音
> adb shell am broadcast -a ai.rec.start

停止录音

> adb shell am broadcast -a ai.rec.stop

录音文件保存在这个路径

> /sdcard/rec