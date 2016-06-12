# 自定义 AndroidActivity 转场动画
在 Android 5.0上 google 官方给我提供了不少好看方便使用的专场动画

原生提供的普通转场动画
- fade 渐隐渐现
- slid 各元素先后滑动进入
- Explode 分裂成连个部分以前进入

分享元素的转场动画
- changeBound 这个是最长使用的 改变View 大小和位置
- changeClipBounds 改变 Clip 边界的大小
- changeImageTransform 改变ImageView 的大小 和 martix
- ChangeTransform 改变普通的 View 一些Scalex 值
- ChangeScroll 改变滑动位置

以上都是原生的. 但是面对一些复制的转场动画,google 提供的这几个还是不够, 很多时候都需要自己定义转场动画.
例如下转场动画, 使用原生的这些动画很难实现:


![](image/1.jpg)







