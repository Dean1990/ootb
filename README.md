#### 简介

这是一个工具集合

##### 文件目录

* data
	* db
		* DB.java（数据库管理）
	* io
		* DefaultLoadingDialog.java（提供的默认加载框）
		* DefaultResultCode.java（提供的默认返回值）
		* ILoadingDialog.java（加载框接口）
		* IRequestParam.java（请求参数接口）
		* IResultCode.java（返回值接口）
		* Request.java（网络请求的抽象类）
		* RequestCallback.java（网络请求的回调）
	* FileUtils.java（文件操作）
	* ImageUtils.java（图片操作）
	* MediaUtils.java（多媒体相关）
	* PersistenceUtils.java（持久化操作）
* entity
	* BaseEntiy.java（基础实体类）
* manager
	* NetworkManager.java（网络管理）
* utils
	* DeviceUtils.java（设备相关）
	* DLogUtils.java（日志操作）
	* FormatUtils.java（格式化操作）
	* PinyinUtils.java（拼音相关）
	* TextUtils.java（文本操作）
	* ValidateUtils.java（数据验证相关）
	* VersionUtils.java（应用版本相关）
* widget
	* glide
		* GlideCircleTransform.java（Glide加载图片圆形）
		* GlideRoundTransform.java（Glide加载图片圆角）
	* CirclePageRightIndicator.java（显示在右侧的小圆点指示器，存在BUG，无法用wrap_content控制住大小，需要设置固定值，或者代码计算设置）
	* GridViewForScrollView.java（支持嵌套在ScrollView中的GridView）
	* HorizontalListView.java（横向的ListView）
	* LazyViewPager.java（懒加载的ViewPager）
	* ListViewForScrollView.java（支持嵌套在ScrollView中的ListView）
	* LoopPagerAdapterWrapper.java
	* LoopViewPager.java（循环的ViewPager）
OotbConfig.java（配置文件，使用ootb需要先调用该类中的init函数）