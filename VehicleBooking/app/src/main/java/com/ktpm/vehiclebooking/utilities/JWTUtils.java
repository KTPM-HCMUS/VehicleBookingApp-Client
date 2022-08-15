package com.ktpm.vehiclebooking.utilities;

import android.util.Base64;

import com.ktpm.vehiclebooking.model.UserPayload;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JWTUtils {

    public static JSONObject decoded(String JWTEncoded) throws Exception {
        JSONObject s1 = null;
        try {
            String[] split = JWTEncoded.split("\\.");
            s1 = new JSONObject(getJson(split[1]));
        } catch (UnsupportedEncodingException e) {
            //Error
            e.printStackTrace();
        }
        return s1;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static UserPayload parseTokenToGetDriver(String token){
        UserPayload userPayload = null;
        try {
            JSONObject s = JWTUtils.decoded(token);
            String userID = s.getString("userId");
            String userName = s.getString("userName");
            String dob = s.getString("dob");
            int role = s.getInt("role");
            Date birthDate;
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            birthDate = df.parse(dob);
            String email = s.getString("email");
            int transportationtype = s.getInt("type");
            String vehicleplatenumber = s.getString("vehicle_plate");
            userPayload = new UserPayload(userID, userName, birthDate, email, role, transportationtype, vehicleplatenumber );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPayload;
    }
}