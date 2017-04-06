package com.slowpath.actionsheet;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.facebook.react.ReactFragmentActivity;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;
import java.util.List;

import com.baoyz.actionsheet.ActionSheet;

public class ActionSheetModule extends ReactContextBaseJavaModule implements ActionSheet.ActionSheetListener {

    private static final String MODULE_NAME = "ActionSheetAndroid";
    private ReactFragmentActivity activity;
    private Callback callback;
    private ReactApplicationContext context;

    public ActionSheetModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        this.activity = (ReactFragmentActivity) reactContext.getCurrentActivity();
        this.callback = null;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void showActionSheetWithOptions(ReadableMap params, Callback callback) {

        this.callback = callback;
        int cancelButtonIndex = params.getInt("cancelButtonIndex");
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
                  .setCancelableOnTouchOutside(false)
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
        callback.invoke(-1);
      }
    }

  }
