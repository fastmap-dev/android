package com.telenav.osv.manager.network.encoder;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.telenav.osv.item.ScoreHistory;

/**
 * Created by kalmanb on 8/3/17.
 */
public class ScoreJsonEncoder {

    public static String encode(HashMap<Integer, ScoreHistory> histories) {

        JSONArray array = new JSONArray();
        for (ScoreHistory history : histories.values()) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("coverage", "" + history.coverage);
                obj.put("photo", "" + history.photoCount);
                obj.put("obdPhoto", "" + history.obdPhotoCount);
                obj.put("detectedSigns", "" + history.detectedSigns);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
        return array.toString();
    }
}
