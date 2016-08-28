package com.example.cjj.slidingmenu;

import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.example.cjj.slidingmenu.bean.Info;
import com.example.cjj.slidingmenu.widget.LoadingView;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity {


    @Bind(R.id.mapView)
    MapView mapView;

    BaiduMap mBaiduMap;

    LoadingView mLocation;


    private LocationClient mLocationClient;
    /**
     * 定位的监听器
     */
    public MyLocationListener mMyLocationListener;
    /**
     * 当前定位的模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    /***
     * 是否是第一次定位
     */
    private volatile boolean isFristLocation = true;

    /**
     * 最新一次的经纬度
     */
    private double mCurrentLantitude;
    private double mCurrentLongitude;
    /**
     * 当前的精度
     */
    private float mCurrentAccracy;
    public static List<Info> infos = new ArrayList<Info>();

    static
    {
        infos.add(new Info(34.242652, 108.971171, R.drawable.a01, "英伦贵族小旅馆",
                "距离209米", 1456));
        infos.add(new Info(34.242952, 108.972171, R.drawable.a02, "沙井国际洗浴会所",
                "距离897米", 456));
        infos.add(new Info(34.242852, 108.973171, R.drawable.a03, "五环服装城",
                "距离249米", 1456));
        infos.add(new Info(34.242152, 108.971971, R.drawable.a04, "老米家泡馍小炒",
                "距离679米", 1456));
    }

    private BitmapDescriptor iconMarker;
    private InfoWindow mInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        mBaiduMap = mapView.getMap();
        iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
        addInfosOverlay(infos);
        initMarkerClickEvent();
        initMapClickEvent();
    }

    public void addInfosOverlay(List<Info> infos)
    {
        mBaiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (Info info : infos)
        {
            // 位置
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(iconMarker).zIndex(5);
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        // 将地图移到到最后一个经纬度位置
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(u);
    }

    private void initMapClickEvent()
    {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener()
        {

            @Override
            public boolean onMapPoiClick(MapPoi arg0)
            {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0)
            {
//                mMarkerInfoLy.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();

            }
        });
    }

    private void initMarkerClickEvent()
    {
        // 对Marker的点击
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(final Marker marker)
            {
                // 获得marker中的数据
                Info info = (Info) marker.getExtraInfo().get("info");

                // 生成一个TextView用户在地图中显示InfoWindow
//                LoadingView location = generateLoadingView();
                addWithGoi(marker.getPosition());
//                LoadingView mLocation= new LoadingView(getApplicationContext());
//                mLocation = generateLoadingView();
//                mLocation.setBackgroundResource(R.drawable.null_bg);
//                mLocation.setPadding(30, 20, 30, 50);
//                mLocation.setText(info.getName());
                // 将marker所在的经纬度的信息转化成屏幕上的坐标
//                final LatLng ll = marker.getPosition();
//                Point p = mBaiduMap.getProjection().toScreenLocation(ll);
//                //获取一个向上的偏移量，使得在marker之上，需要marker的高度
//                p.y -= 47;
//                LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
//                mInfoWindow = new InfoWindow(mLocation, llInfo, -47);
//                // 显示InfoWindow
//                mBaiduMap.showInfoWindow(mInfoWindow);
                // 设置详细信息布局为可见
                return true;
            }
        });
    }

    private void addWithGoi(LatLng latLng) {
        mLocation = (LoadingView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.loading_view,null);
        MapViewLayoutParams lp = new MapViewLayoutParams.Builder()
                .position(latLng)
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .yOffset(-50)
                .build();
        mapView.addView(mLocation,lp);
        mapView.requestLayout();
        mapView.invalidate();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mLocation.setText("城西银泰写白做一楼客服部");
            }
        },1500);
    }

    private LoadingView generateLoadingView() {
        LoadingView loadingView = new LoadingView(getApplicationContext());
        loadingView.setBallonPadding(3);
        loadingView.setTextSize(sp2px(18));
        loadingView.setMinRadius(2);
        loadingView.setMaxRadius(6);
//        loadingView.setText("Lorem Lisp super Length Text oops");
        return loadingView;
    }


    private class ViewHolder
    {
        ImageView infoImg;
        TextView infoName;
        TextView infoDistance;
        TextView infoZan;
    }

    public class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null)
                return;
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mCurrentAccracy = location.getRadius();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            mCurrentLantitude = location.getLatitude();
            mCurrentLongitude = location.getLongitude();
            // 设置自定义图标
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.navi_map_gps_locked);
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mCurrentMode, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);
            // 第一次定位时，将地图位置移动到当前位置
            if (isFristLocation)
            {
                isFristLocation = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

    }

    public int sp2px(int sp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }
}
