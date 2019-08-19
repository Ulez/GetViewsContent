package com.ulez.getviewscontent;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "accessibilityservice";
    private int mDebugDepth;
    private AccessibilityNodeInfo mNodeInfo;
    private long lastTime = 0;
    private static final String pattern = "\"(.*)\" # ";
    private int index = 0;
    private int lastIndex = 0;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        Log.e(TAG, "onAccessibilityEvent==" + event.getClassName());
        mDebugDepth = 0;
        mNodeInfo = event.getSource();
        index = 0;
        printAllViews(mNodeInfo);
//        Log.e(TAG, String.format(
//                "onAccessibilityEvent: type = [ %s ], class = [ %s ], package = [ %s ], time = [ %s ], text = [ %s ]",
//                getEventType(event), event.getClassName(), event.getPackageName(),
//                event.getEventTime(), getEventText(event)));
    }

    private CharSequence getEventText(AccessibilityEvent event) {
        CharSequence xx = "null event";
        try {
            xx = event.getText().get(0);
        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
        } finally {
            return xx;
        }
    }

    @Override
    protected void onServiceConnected() {
//        Log.e(TAG,"onServiceConnected");
        super.onServiceConnected();
    }

    private int getEventType(AccessibilityEvent event) {
        return event.getEventType();
    }

    @Override
    public void onInterrupt() {
//        Log.e(TAG, "onInterrupt==");
    }

    private void printAllViews(AccessibilityNodeInfo mNodeInfo) {
        if (mNodeInfo == null) return;
        String log = "";
        for (int i = 0; i < mDebugDepth; i++) {
            log += ".";
        }
        String text = "";
        try {
            text = mNodeInfo.getText().toString();
        } catch (Exception e) {
        }
        if (mDebugDepth == 9 && !TextUtils.isEmpty(text) && !"  ".equals(text)) {
            Rect outBounds = new Rect();
            mNodeInfo.getBoundsInScreen(outBounds);
            if (outBounds.right==840){
                Log.i(TAG, index + ":text=" + text);
                if (index>lastIndex){
                    lastIndex = Math.max(lastIndex,index);
                    Log.i(TAG, index + ":最后一条文本为:" + text);
                }
            }
            index++;
        }
        if (mNodeInfo.getChildCount() < 1) {
            return;
        }
        mDebugDepth++;
        for (int i = 0; i < mNodeInfo.getChildCount(); i++) {
            printAllViews(mNodeInfo.getChild(i));
        }
        mDebugDepth--;
    }
}
