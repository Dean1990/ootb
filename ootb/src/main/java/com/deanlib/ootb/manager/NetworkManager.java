package com.deanlib.ootb.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.deanlib.ootb.utils.DeviceUtils;
import com.deanlib.ootb.utils.UtilsConfig;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


public class NetworkManager {
	
	private static final String TAG = NetworkManager.class.getSimpleName();


	
	public interface NetworkListener{
		
		public void onNetworkDisconnect();
		public void onNetworkConnected(int type);
		
	}
	
	List<NetworkListener> listeners = new ArrayList<NetworkListener>();
	public void addOnNetworkListener(NetworkListener listener){
		if(!listeners.contains(listener))
			listeners.add(listener);
	}

	/**
	 * 移除网络监听
	 * @param listener
	 */
	public void removeNetworkListener(NetworkListener listener){
		if(listeners.contains(listener))
			listeners.remove(listener);
	}
	
	private static NetworkManager instance  = null;
	
	public static boolean enable = false;

	public static NetworkManager getInstance(){
		if(instance == null){
			instance = new NetworkManager();
		}
		return instance;
	}

	/**
	 * 初始化
	 */
	public void init(){
		if(DeviceUtils.isNetworkConnected()&& DeviceUtils.getNetworkType()!=0){
			enable = true;
		}
	}
	
	NetworkChangedReceiver ncr = new NetworkChangedReceiver();
	
	public void registerNetworkReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		UtilsConfig.mContext.registerReceiver(ncr, filter);
	}
	
	public void unregisterNetworkReceiver(){
		UtilsConfig.mContext.unregisterReceiver(ncr);
	}
	
	
	/**
	 * 网络状态接收器
	 * @author louis.lv
	 *
	 */
	class NetworkChangedReceiver extends BroadcastReceiver {
		
		 @Override
		 public void onReceive(Context context, Intent intent) {
			 	String action = intent.getAction();
				LogUtil.d(action);
				if(DeviceUtils.getNetworkType()==DeviceUtils.TYPE_NO_CONNECTION){
					NetworkManager.enable = false;
					LogUtil.d("没有网络");
					for(NetworkListener lis:listeners){
						lis.onNetworkDisconnect();
					}
					return;
				}
				
				if(DeviceUtils.getNetworkType()==DeviceUtils.TYPE_WIFI){
					wifiAction(intent, action);
				}else {
					wapAction(intent, action);
				}
				
		 }

		public void wapAction(Intent intent, String action) {
			if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
			    ConnectivityManager cm = (ConnectivityManager) UtilsConfig.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			    //如果是在开启wifi连接和有网络状态下
			    if(NetworkInfo.State.CONNECTED==info.getState()){
			        //连接状态
					LogUtil.e("有网络连接");
			        NetworkManager.enable = true;
			        for(NetworkListener lis:listeners){
						lis.onNetworkConnected(DeviceUtils.getNetworkType());
					}
			    }else{
					LogUtil.e("无网络连接");
			        NetworkManager.enable = false;
			        
					for(NetworkListener lis:listeners){
						lis.onNetworkDisconnect();
					}
			    }
			}
		}

		public void wifiAction(Intent intent, String action) {
			if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
				NetworkInfo info =intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if(info!=null){
					LogUtil.v(info.getDetailedState().toString());
					if(info.getDetailedState()== DetailedState.CONNECTED){
						NetworkManager.enable = true;
						LogUtil.d("网络已连接");
						
						//获取更新--网络断开后没有到达的消息通过这个方法获取
						
						//开启推送服务
						
						for(NetworkListener lis:listeners){
							lis.onNetworkConnected(DeviceUtils.getNetworkType());
						}
					}
				}
			}else if(action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
				DetailedState state = WifiInfo.getDetailedStateOf((SupplicantState)intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE));
				LogUtil.i(state.toString());
				if(state== DetailedState.DISCONNECTED){//切换网络
					NetworkManager.enable = false;
					LogUtil.d("网络断开");
					for(NetworkListener lis:listeners){
						lis.onNetworkDisconnect();
					}
				}
			}else if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){//开关wifi
				int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
				if(wifiState== WifiManager.WIFI_STATE_DISABLED){
					NetworkManager.enable = false;
					LogUtil.d("关闭网络");
					//发送通知给activity 显示toast提示
					for(NetworkListener lis:listeners){
						lis.onNetworkDisconnect();
					}
				}
			}
		}
		 
	}
}
