package com.mapbox.mapboxsdk.module.telemetry;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.Mapbox;
import java.util.Locale;


public class EventMetaData {

  private static String METADATA_KEY = "metadata";
  private static JsonObject eventMetaData = null;

  public static Bundle addMetaData(Bundle data) {
    if (data == null) {
      data = new Bundle();
    }
    data.putString(METADATA_KEY, getEventMetaData().toString());
    return data;
  }

  private static synchronized JsonObject getEventMetaData() {
    if (eventMetaData == null) {
      eventMetaData = new JsonObject();
      eventMetaData.addProperty("os", "android");
      eventMetaData.addProperty("manufacturer", Build.MANUFACTURER);
      eventMetaData.addProperty("brand", Build.BRAND);
      eventMetaData.addProperty("device", Build.MODEL);
      eventMetaData.addProperty("version", Build.VERSION.RELEASE);
      eventMetaData.addProperty("abi", Build.CPU_ABI);
      eventMetaData.addProperty("country", Locale.getDefault().getISO3Country());
      eventMetaData.addProperty("ram", getRam());
      eventMetaData.addProperty("screenSize", getWindowSize());
    }
    return eventMetaData;
  }

  private static String getRam() {
    ActivityManager actManager =
            (ActivityManager) Mapbox.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
    ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
    actManager.getMemoryInfo(memInfo);
    return String.valueOf(memInfo.totalMem);
  }

  private static String getWindowSize() {
    WindowManager windowManager =
            (WindowManager) Mapbox.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    Display display = windowManager.getDefaultDisplay();
    DisplayMetrics metrics = new DisplayMetrics();
    display.getMetrics(metrics);
    int width = metrics.widthPixels;
    int height = metrics.heightPixels;

    return "{" + width + "," + height + "}";
  }
}
