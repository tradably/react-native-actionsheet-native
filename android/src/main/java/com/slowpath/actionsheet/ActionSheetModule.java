package com.slowpath.actionsheet;

import android.app.Activity;

import com.facebook.react.ReactFragmentActivity;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import com.baoyz.actionsheet.ActionSheet;

public class ActionSheetModule extends ReactContextBaseJavaModule implements ActionSheet.ActionSheetListener {

    private static final int RESULT_CANCEL = -1;
    private static final int RESULT_ERROR = -2;

    private FragmentActivity activity;
    private Callback callback;
    private ReactApplicationContext context;

    public ActionSheetModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        this.callback = null;
    }

    @Override
    public String getName() {
        return "ActionSheetAndroid";
    }

    public @Nullable Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();

        constants.put("RESULT_CANCEL", RESULT_CANCEL);
        constants.put("RESULT_ERROR", RESULT_ERROR);

        return constants;
    }

    @ReactMethod
    public void showActionSheetWithOptions(ReadableMap params, Callback callback) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null || !(currentActivity instanceof FragmentActivity)) {
            this.callback.invoke(RESULT_ERROR);
            return;
        }
        this.activity = (FragmentActivity) currentActivity;
        this.callback = callback;
        int cancelButtonIndex = params.getInt("cancelButtonIndex");
        boolean cancelOnTouchOutside = params.getBoolean("cancelOnTouchOutside");
        
        ReadableArray options = params.getArray("options");
        List<String> list = new ArrayList<>(options.size());
        String cancelButtonName = options.getString(cancelButtonIndex);

        for (int i = 0; i < options.size(); ++i) {
          String value = options.getString(i);
          if (!value.equals(cancelButtonName)) {
            list.add(value);
          }
        }

        String[] args = new String[list.size()];

        ActionSheet.createBuilder(context, activity.getSupportFragmentManager())
                  .setCancelButtonTitle(cancelButtonName)
                  .setOtherButtonTitles(list.toArray(args))
                  .setCancelableOnTouchOutside(cancelOnTouchOutside)
                  .setListener(this).show();
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
      if (callback != null) {
        callback.invoke(index);
      }
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
      if (callback != null && isCancle) {
        callback.invoke(RESULT_CANCEL);
      }
    }

  }
