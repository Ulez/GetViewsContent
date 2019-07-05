package com.ulez.getviewscontent;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";
    private int mDebugDepth;
    private AccessibilityNodeInfo mNodeInfo;
    private long lastTime = 0;
    private static final String pattern = "\"(.*)\" # ";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        Log.e(TAG, "onAccessibilityEvent==" + event.getClassName());
        mDebugDepth = 0;
        mNodeInfo = event.getSource();
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
        if (mDebugDepth >= 6 && Config.packageName.equals(mNodeInfo.getPackageName().toString()) && Config.TextName.equals(mNodeInfo.getClassName().toString())) {
            CharSequence text = mNodeInfo.getText();
            if (System.currentTimeMillis() - lastTime > 800) {
//                log += "(" + text + " <-- " + mNodeInfo.getViewIdResourceName() + ")";
                String result = text.toString();
                // 创建 Pattern 对象
                Pattern r = Pattern.compile(pattern);
                // 现在创建 matcher 对象
                Matcher m = r.matcher(result);
                if (m.find()) {
                    System.out.println("Found value: " + m.group(1));
                    result = m.group(1);
                } else {
                    result = "";
                }
                Toast.makeText(MyAccessibilityService.this, result, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "asr=" + result);
                lastTime = System.currentTimeMillis();
            }
        }
        if (mNodeInfo.getChildCount() < 1) return;
        mDebugDepth++;
        for (int i = 0; i < mNodeInfo.getChildCount(); i++) {
            printAllViews(mNodeInfo.getChild(i));
        }
        mDebugDepth--;
    }
}
