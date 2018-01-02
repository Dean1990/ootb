#### 简介

这是一个Android开发的工具集合,或者叫CodeBase

#### 配置

##### 1. build.gradle(Project)

```groovy
allprojects {
	repositories {
	    ...
	    maven { url "https://jitpack.io" }
	    ...
	}
}
```

##### 2. build.gradle(Module)

```groovy
dependencies {
	...
	compile 'com.github.Dean1990:ootb:2.0.1'
	...
}
```

##### 3. application类

```java
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
```

##### 4. 权限

并不要求所有权限都声明，按需添加

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CAMERA" />
```

以上内容就可以正常使用OOTB，以下为扩展

##### 5. 假数据测试

###### 有时，接口还没有开发好，但需要使用假数据调试

```java
OotbConfig.setRequestFalseData(false, 1000);
```
###### 然后修改网络请求的request实现类的parse方法的参数值

```java
@Override
public Object parse(String s) {

    s = "{\"msg\":\"Success\",\"result\":[{\"imagesId\":123,\"createTime\":{\"date\":17,\"hours\":15,\"seconds\":48,\"month\":6,\"timezoneOffset\":-480,\"year\":117,\"minutes\":21,\"time\":1500276108000,\"day\":1},\"resource\":\"我\",\"isRecommended\":12,\"id\":1,\"title\":\"小猪快跑\",\"content\":\"我\",\"createDate\":\"2017-07-17\"}],\"code\":\"200\"}";

    JSONObject object = JSON.parseObject(s);

    return object;
}
```

##### 6. 定义网络请求参数

```java
public class UserReqParam implements IRequestParam {
		@Override
	    public RequestParams disposeParam(RequestParams requestParams) {
	    
	        //可以在这个方法里设置一些全局网络请求参数
	        if (requestParams!=null){
	            if (UserManager.isUserLogin()){
	                DLogUtils.d("User Header>>>token:"+UserManager.user.token);
	                requestParams.addHeader("token",UserManager.user.token);
	                requestParams.setReadTimeout(30000);
	            }
	
	        }
	
	        return requestParams;
	    }
	}
```

##### 7. 定义网络请求返回值

```java
public class UserResultCode extends ResultCode {
  public UserResultCode(String successCode,HashMap<String, String> resultCodeMap) {
    super(successCode,resultCodeMap);
  }

  @Override
  public boolean onResultParse(String code) {
    //可以在这里设置一些全局的网络请求结果的相应操作
    //例如接口规定 code返回123时登录过期,即可如下操作
    if ("123".equals(code)){

      UserManager.deleteUser();

    }

    return super.onResultParse(code);
  }
}
```
##### 8. 定义网络请求加载框

```java
public class UserLoadingDialog extends ILoadingDialog {

  @Override
  public Dialog showLoadingDialog(Activity activity) {

    //自定义加载框
    View view = LayoutInflater.from(activity).inflate(R.layout.layout_loading, null);
    ImageView imgLoading = view.findViewById(R.id.imgLoading);

    Animation animation = AnimationUtils.loadAnimation(activity, R.anim.round_rotate);
    animation.setInterpolator(new LinearInterpolator());
    imgLoading.setImageResource(R.mipmap.loading);
    imgLoading.startAnimation(animation);

    return new AlertDialog.Builder(activity, R.style.TransparentBGDialog).setCancelable(false).setView(view).show();
  }

  @Override
  public void dismissLoadingDialog() {
    //网络请求结束，关闭加载框时会调用该方法
  }

}
```

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
  * ​
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

>'org.xutils:xutils:3.5.0'
>>感谢xUtils，多年来一直在用这个框架，所以这个工具有很大一部分功能是对xUtils的再包装。

>'com.alibaba:fastjson:1.2.41'
>>JSON操作

>'com.github.bumptech.glide:glide:3.7.0'
>>图片加载

>'com.jakewharton.rxbinding:rxbinding:0.4.0'

>'com.tbruyelle.rxpermissions:rxpermissions:0.9.3@aar'
>>以上两个用来权限申请

>'com.inkapplications.viewpageindicator:library:2.4.3'
>>CirclePageRightIndicator.java用到

>'com.github.PhilJay:MPAndroidChart:v3.0.2'
>>图表绘制

>'cn.bingoogolapple:bga-qrcodecore:1.1.7@aar'
>>扫码包装

>'cn.bingoogolapple:bga-zbar:1.1.7@aar'
>>扫码zbar核心

>'cn.bingoogolapple:bga-zxing:1.1.7@aar'
>>扫码zxing核心

>'org.greenrobot:eventbus:3.0.0'
>>消息传递

>'q.rorbin:VerticalTabLayout:1.2.5'
>>竖向的TabLayout

>'com.zhy:flowlayout-lib:1.0.3'
>>横向流式布局

>'se.emilsjolander:stickylistheaders:2.7.0'
>>带标题(header)悬停的ListView

>'com.github.Dean1990:BothwayListview:-SNAPSHOT'
>>双向滑动的ListView

>'com.belerweb:pinyin4j:2.5.1'
>>拼音相关

>'com.scwang.smartrefresh:SmartRefreshLayout:1.0.3'
>>上下拉加载