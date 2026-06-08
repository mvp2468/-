### Wikipedia Android app

This repository contains the source code for the official [Wikipedia Android app](https://play.google.com/store/apps/details?id=org.wikipedia).

### Documentation

All documentation is kept on [our wiki](https://www.mediawiki.org/wiki/Wikimedia_Apps/Team/Android/App_hacking). Check it out!

### Issues

Kindly file issues in [our bug tracker][1]


[1]: https://phabricator.wikimedia.org/maniphest/task/edit/form/10/?title=&projects=wikipedia-android-app-backlog&points=1&description=%3D%3D%3D+Steps+to+reproduce%0A%23+%0A%23+%0A%23+%0A%0A%3D%3D%3D+Expected+results%0A%0A%3D%3D%3D+Actual+results%0A%0A%3D%3D%3D+Stack%20trace%0A%60%60%60lines%3D10%0A(Optional%20logcat%20output)%0A%60%60%60%0A%0A%3D%3D%3D+Environments+observed%0A**App+version%3A+**+%0A**Android+OS+versions%3A**+%0A**Device+model%3A**+%0A**Device+language%3A**

# 仓库文件总览
1. 项目源码目录（app、analytics、gradle等）
维基百科Android客户端完整源码，包含单元测试、构建脚本。

2. 维基百科应用安装包文件/
项目编译生成prodRelease正式交付文件：
- app-prod-release.aab：Google标准App Bundle包
- app-prod-release.apk：原生导出Release安装包
- wiki-release.apks：AAB经bundletool转换得到的通用APKS包

3. 静态安全分析报告/
维基百科APP静态逆向分析完整Word报告，基于Androguard工具完成权限、组件、网络接口扫描，附带Python分析源码与安全结论。

4. 随堂练习/
彭博的课程课堂练习、实验截图、阶段性小作业文档。
