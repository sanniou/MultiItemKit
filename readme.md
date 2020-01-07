[TOC]
# MultiItem
MultiItem 诞生在一个深度使用 [`databinding`][1] 的开发环境中，结合数据驱动的开发思想，抽象出一个数据层的接口 `DataItem` ，并开发与其配套使用的一系列工具，核心思想是拆解 UI ，组合并复用 `DataItem` 来完成界面
## 开始
```
    implementation 'com.sanniou:multiitem:0.0.1'
    // 可选
    implementation 'com.sanniou:multiitemkit:0.0.1'
```
## 用法
### 实现 DataItem
```
    data class GithubRepo(val name: String) : DataItem {
        override fun getItemType() = R.layout.main_item_github_repo
    }
```
### 绑定 layout
```
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>

        <variable
            name="item"
            type="com.sanniou.multiitemsample.model.GithubRepo" />
    </data>

    <FramLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{item.name}"/>
    </FramLayout>
</layout>

```

### 数据集
```
    val items = MultiItemArrayList<DataItem>()
```
### 使用 MultiItemAdapter
```
    recyclerView.adapter = MultiItemAdapter(items)
```
### 操作数据
```
    items.add(GithubRepo())
    items.remove(GithubRepo())
    items.swap(1,9)
    items.move(2,5)
```
`RecyclerView` 已经绑定了 `List` ,对 `List` 的任何操作都将实时显示在界面上
## 高级
`multiitemkit` 提供了以下特性
### Adapter

 - MultiItemAdapter
 - ItemClickAdapter
 - MultiClickAdapter

### Databinding
#### Divider
提供 `bindingRecyclerAdapter` 方法，可以在 `layout` 中快速设置 `ItemDecoration Divider`,有以下属性
```
dividerColor   颜色
dividerHeight  高度
divider        是否处理分割线，一般不需设置，最少只设置这一个值即可
noDivider      不处理分割线
horizontal     方向，默认竖向
drawOnTop      绘制在 View 上边
drawFirstStart 最上开始绘制
drawLastEnd    最后是否绘制
types          指定需要绘制的 ViewType 的集合，传空绘制全部类型
```
这只是快速设置默认 `Divider` ，可以使用 `VerticalItemDecoration`、 `HorizontalItemDecoration` 来设置详细参数
#### Data
在 `layout` 文件中绑定 `RecyclerView` 的 `data` 属性即可快速实现 `Adapter` 部分的操作
```
<layout>

    <data>

        <variable
            name="itemClickListener"
            type="com.sanniou.multiitem.OnItemClickListener" />
        <variable
            name="data"
            type="com.sanniou.multiitem.MultiItemArrayList&lt;DataItem&gt;" />
        <import type="com.sanniou.multiitem.DataItem" />
    </data>

        <androidx.recyclerview.widget.RecyclerView
            xmlns:android="http://schemas.android.com/apk/res/android"
            data="@{data}"
            itemClickListener="@{itemClickListener}"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
</layout>
```
其他属性还有
```
data 数据集
itemClickListener item 点击监听
longPressListener item 长按监听
viewListener item 指定 id 的 view 点击监听
layoutManager LayoutManager 字符串
orientation orientation 默认是0 ，为了使默认使用 VERTICAL ，这里规定 0 = VERTICAL0 ，1 = HORIZONTAL，那么需要横向时设置 1 即可
span span 值
prefetchCount 预取
isItemPrefetchEnabled 启用预取
customerLayoutManager 表示使用自定义 LayoutManager ，如果 true 则 LayoutManager 相关属性不生效
```
### 在 Layout 中使用 DataItem
`DataItem` 也可以直接在 `Layout` 中使用 `item` 属性绑定  
在 `Framlayout` 中作为 `childView` 使用
```
    <FrameLayout
    item="@{item}"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
在 `View` 中使用时替换原 `View`
```
   <Space
    item="@{item}"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

### 使用 WrapperDataItem
继承 `WrapperDataItem` ,需要实现一个 `WrapperHandler` ，可以扩展 DataItem 的更多特性；  
`WrapperHandler` 的意义在于，封装了一些通用的 `DataItem` 处理方法，可以快速集成在新的 `DataItem` 中。
在 `multiitemkit` 中提供了以下几种 WrapperHandler

 - ClickWrapperHandler
 - MarginWarpperHandler
 - PaddingWarpperHandler
 - RoundWrapperHandler
 - SizeWarpperHandler

  [1]: https://developer.android.com/topic/libraries/data-binding
