# InfuseAndroid

这是一个电视播放器, 功能类似于IOS系统中的Infuse

这个应用的主要功能:

1. 使用Android平台, 适配TV
2. 支持SMB协议, 可以扫描和连接局域网中的SMB服务器
3. 支持扫描服务器中的视频文件, 主要分为电影(单文件), 电视剧(文件夹+季+单集)
4. 支持展示电影,电视剧的海报墙
5. 适配播放功能

## 项目技术栈

- 架构：MVVM (Model-View-ViewModel)
- UI：Jetpack Compose，TV Compose
- 网络：SMB协议 (jcifs-ng)
- 存储：Room数据库
- 异步：协程和Flow
- 依赖注入：Hilt
- 导航：Jetpack Navigation Compose
- 视频播放：Media3
- 图片加载：Coil

## 开发进度

- [x] 项目初始化和架构搭建
- [x] 基本导航框架实现
- [ ] SMB服务器连接和管理功能
- [ ] 视频文件扫描和分类
- [ ] 电影/电视剧列表界面
- [ ] 视频播放功能
- [ ] 收藏和最近播放功能

## 开发步骤

1. 创建SMB服务器管理界面和功能
2. 实现SMB文件浏览和扫描
3. 设计并实现电影和电视剧列表界面
4. 开发视频播放器功能
5. 添加收藏和最近播放功能
