package com.stupidcoderx.modding.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;

public class JsonUtil {
    public static JsonArray serializeFloatArray(float[] arr, int begin, int end) {
        Preconditions.checkElementIndex(begin, arr.length);
        Preconditions.checkElementIndex(end - 1, arr.length);
        Preconditions.checkArgument(begin <= end, "invalid range");
        JsonArray res = new JsonArray();
        for(int i = begin ; i < end ; i ++) {
            res.add(arr[i]);
        }
        return res;
    }
}
