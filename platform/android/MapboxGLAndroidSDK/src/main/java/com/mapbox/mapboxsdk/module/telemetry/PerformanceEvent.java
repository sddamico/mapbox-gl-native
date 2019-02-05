package com.mapbox.mapboxsdk.module.telemetry;

import com.google.gson.Gson;

import com.google.gson.JsonObject;

import com.google.gson.reflect.TypeToken;
import com.mapbox.android.telemetry.Event;

import android.os.Bundle;
import android.os.Parcel;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Generic Performance Event that can be used for performance measurements.
 * Customer measurements can be added to the bundle.
 */
public class PerformanceEvent extends Event {

  private static final String PERFORMANCE_TRACE = "mobile.performance_trace";

  private final String event;

  private final String created;

  private final String sessionId;

  private final List<Attribute<String>> attributes;

  private final List<Attribute<Double>> counters;

  private final JsonObject metadata;


  private static final SimpleDateFormat DATE_FORMAT =
          new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

  PerformanceEvent(String sessionId, Bundle bundle) {

    this.event = PERFORMANCE_TRACE;
    this.created = DATE_FORMAT.format(new Date());
    this.sessionId = sessionId;

    Gson gson = new Gson();
    String strInBundle = bundle.getString("attributes");
    if (strInBundle == null) {
      this.attributes = null;
    } else {
      this.attributes = gson.fromJson(strInBundle,
              new TypeToken<ArrayList<Attribute<String>>>() {}.getType());
    }

    strInBundle = bundle.getString("counters");
    if (strInBundle == null) {
      this.counters = null;
    } else {
      this.counters = gson.fromJson(strInBundle,
              new TypeToken<ArrayList<Attribute<? extends Double>>>() {}.getType());
    }

    strInBundle = bundle.getString("metadata");
    if (strInBundle == null) {
      this.metadata = null;
    } else {
      this.metadata = gson.fromJson(strInBundle, JsonObject.class);
    }
  }

  private PerformanceEvent(Parcel in) {
    this.event = in.readString();
    this.created = in.readString();
    this.sessionId = in.readString();

    Gson gson = new Gson();

    String nextString = in.readString();
    if (nextString == null) {
      this.attributes = null;
    } else {
      this.attributes = gson.fromJson(nextString,
              new TypeToken<ArrayList<Attribute<String>>>() {}.getType());
    }

    nextString = in.readString();
    if (nextString == null) {
      this.counters = null;
    } else {
      this.counters = gson.fromJson(nextString,
              new TypeToken<ArrayList<Attribute<? extends Double>>>() {}.getType());
    }

    String metaDataStr = in.readString();
    if (metaDataStr == null) {
      this.metadata = null;
    } else {
      this.metadata = gson.fromJson(metaDataStr, JsonObject.class);
    }
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(event);
    parcel.writeString(created);
    parcel.writeString(sessionId);

    Gson gson = new Gson();

    parcel.writeString(gson.toJson(attributes));
    parcel.writeString(gson.toJson(counters));

    if (metadata != null) {
      parcel.writeString(metadata.toString());
    }
  }

  public static final Creator<PerformanceEvent> CREATOR = new Creator<PerformanceEvent>() {
    @Override
    public PerformanceEvent createFromParcel(Parcel in) {
      return new PerformanceEvent(in);
    }

    @Override
    public PerformanceEvent[] newArray(int size) {
      return new PerformanceEvent[size];
    }
  };


  private class Attribute<T> {
    private final String name;
    private final T value;

    Attribute(String name, T value) {
      this.name = name;
      this.value = value;
    }
  }
}
