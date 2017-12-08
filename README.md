#### 简介

这是一个Android开发的工具集合

#### 配置

##### 1. build.gradle(Project)
	
	allprojects {
	    repositories {
	    	...
	    	maven { url "https://jitpack.io" }
	    	...
	    }
	}
	
##### 2. build.gradle(Module)

	dependencies {
		...
		compile 'com.github.Dean1990:ootb:-SNAPSHOT'
		...
	}
	
##### 3. application类
	
	@Override
    public void onCreate() {
        super.onCreate();
        ...
        //放在使用OOTB的最前面
        OotbConfig.init(this,true);
        //网络设置
        OotbConfig.setRequestServer(Constants.serviceUrl, null, new DefaultResultCode(),new DefaultLoadingDialog());
        ...
    }
    
##### 以上内容就可以正常使用OOTB，以下为扩展
##### 4. 假数据测试
	
###### 有时，接口还没有开发好，但需要使用假数据调试

	OotbConfig.setRequestFalseData(false, 1000);
	
######然后修改网络请求的request实现类的parse方法的参数值

	@Override
    public Object parse(String s) {

        s = "{\"msg\":\"Success\",\"result\":[{\"imagesId\":123,\"createTime\":{\"date\":17,\"hours\":15,\"seconds\":48,\"month\":6,\"timezoneOffset\":-480,\"year\":117,\"minutes\":21,\"time\":1500276108000,\"day\":1},\"resource\":\"我\",\"isRecommended\":12,\"id\":1,\"title\":\"小猪快跑\",\"content\":\"我\",\"createDate\":\"2017-07-17\"}],\"code\":\"200\"}";

        JSONObject object = JSON.parseObject(s);

        return object;
    }
    
##### 5. 定义网络请求参数

##### 6. 定义网络请求返回值

##### 7. 定义网络请求加载框
        
        

#### 文件目录

* data
	* db
		* DB.java（数据库管理）
	* io
		* DefaultLoadingDialog.java（提供的默认加载框）
		* DefaultResultCode.java（提供的默认返回值）
		* ILoadingDialog.java（加载框自定义接口）
		* IRequestParam.java（请求参数自定义接口）
		* Request.java（网络请求的抽象类）
		* Request.RequestCallback.java（网络请求的回调）
		* ResultCode.java（返回值自定义接口，耦合性太好，待改善）
	* FileUtils.java（文件操作）
	* ImageUtils.java（图片操作）
	* MediaUtils.java（多媒体相关）
	* PersistenceUtils.java（持久化操作,有时感觉这个类有点鸡肋）
* entity
	* BaseEntiy.java（基础实体类）
* manager
	* NetworkManager.java（网络管理）
	* PermissionManager.java（权限管理）
	* 
* utils
	* DeviceUtils.java（设备相关）
	* DLogUtils.java（日志操作）
	* FormatUtils.java（格式化操作）
	* PinyinUtils.java（拼音相关）
	* TextUtils.java（文本操作）
	* ValidateUtils.java（数据验证相关）
	* VersionUtils.java（应用版本相关）
	* WifiAutoConnectUtils（自动连接WIFI工具）
* widget
	* glide
		* GlideCircleTransform.java（Glide加载图片圆形）
		* GlideRoundTransform.java（Glide加载图片圆角）
	* CirclePageRightIndicator.java（显示在右侧的小圆点指示器，存在BUG，无法用wrap_content控制住大小，需要设置固定值，或者代码计算设置）
	* GridViewForScrollView.java（支持嵌套在ScrollView中的GridView）
	* HorizontalListView.java（横向的ListView）
	* LazyViewPager.java（懒加载的ViewPager）
	* ListViewForScrollView.java（支持嵌套在ScrollView中的ListView）
	* loopviewpager
		* LoopPagerAdapterWrapper.java（支持类）
		* LoopViewPager.java（循环的ViewPager）
OotbConfig.java（配置文件，使用ootb需要先调用该类中的init函数）

#### 引用

>compile 'org.xutils:xutils:3.5.0'
>>感谢xUtils，多年来一直在用这个框架，所以这个工具有很大一部分功能是对xUtils的再包装。

>compile 'com.alibaba:fastjson:1.2.41'
>>JSON操作

>compile 'com.github.bumptech.glide:glide:3.7.0'
>>图片加载

>compile 'com.jakewharton.rxbinding:rxbinding:0.4.0'

>compile 'com.tbruyelle.rxpermissions:rxpermissions:0.9.3@aar'
>>以上两个用来权限申请

>compile 'com.inkapplications.viewpageindicator:library:2.4.3'
>>CirclePageRightIndicator.java用到

>compile 'com.github.PhilJay:MPAndroidChart:v3.0.2'
>>图表绘制

>compile 'cn.bingoogolapple:bga-qrcodecore:1.1.7@aar'
>>扫码包装

>compile 'cn.bingoogolapple:bga-zbar:1.1.7@aar'
>>扫码zbar核心

>compile 'cn.bingoogolapple:bga-zxing:1.1.7@aar'
>>扫码zxing核心

>compile 'org.greenrobot:eventbus:3.0.0'
>>消息传递

>compile 'q.rorbin:VerticalTabLayout:1.2.5'
>>竖向的TabLayout

>compile 'com.zhy:flowlayout-lib:1.0.3'
>>横向流式布局

>compile 'se.emilsjolander:stickylistheaders:2.7.0'
>>带标题(header)悬停的ListView

>compile 'com.github.Dean1990:BothwayListview:-SNAPSHOT'
>>双向滑动的ListView

>compile 'com.belerweb:pinyin4j:2.5.1'
>>拼音相关