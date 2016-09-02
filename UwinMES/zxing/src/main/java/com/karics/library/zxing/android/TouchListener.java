package com.karics.library.zxing.android;

import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/8/12.
 */
public class TouchListener implements View.OnTouchListener {

    /** 记录是拖拉照片模式还是放大缩小照片模式 */
    private static final int MODE_INIT = 0;
    /** 放大缩小照片模式 */
    private static final int MODE_ZOOM = 1;
    private int mode = MODE_INIT;// 初始状态

    /** 用于记录拖拉图片移动的坐标位置 */
    private float startDis;
    private Camera.Parameters mParameters;

    public TouchListener(Camera.Parameters parameters){
        mParameters = parameters;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 手指压下屏幕
            case MotionEvent.ACTION_DOWN:
                mode = MODE_INIT;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                mode = MODE_ZOOM;
                /** 计算两个手指间的距离 */
                startDis = distance(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_ZOOM && mParameters != null) {
                    //只有同时触屏两个点的时候才执行
                    if(event.getPointerCount()<2) return true;
                    float endDis = distance(event);// 结束距离
                    //每变化10f zoom变1
                    int scale=(int) ((endDis-startDis)/10f);
                    if(scale>=1||scale<=-1){
                        int zoom=mParameters.getZoom()+scale;
                        //zoom不能超出范围
                        if(zoom>mParameters.getMaxZoom()) zoom=mParameters.getMaxZoom();
                        if(zoom<0) zoom=0;
//                        CameraConfigurationUtils.setZoom(mParameters, 2.5);
                        mParameters.setZoom(zoom);
                        //将最后一次的距离设为当前距离
                        startDis=endDis;
                    }
                }
                break;
        }
        return true;
    }
    /** 计算两个手指间的距离 */
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        /** 使用勾股定理返回两点之间的距离 */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
