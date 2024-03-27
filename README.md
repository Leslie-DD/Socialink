# Socialink

Android 客户端项目，项目历史比较悠久，最早的代码始于 2016 年，所以编程语言 Java，未用到 Kotlin。
项目经过多次迭代，比较大的修改包括：
- 升级了 gradle、jdk 版本
- 简单优化了网络请求
- 并修复了各种 bug

### 整体来说项目架构不够健壮，比较古老
- 业务层没有架构可言，所有逻辑都写在 Activity 或者 Fragment
- 从而可能导致很多小问题，比如各种内存泄露

### 后续开发建议
- 引入 kotlin、compose，继续升级 gradle 版本到最新
- 若有新需求，业务层逻辑采用 MVI 或 MVVM 架构
- 模块化，抽出网络请求模块、登录模块等
- 主页 UI 逻辑优化，采用 navigation
- 升级各个页面，比如升级 ViewPager，弃用 xRecyclerView