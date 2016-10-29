package com.blazers.jandan.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by Blazers on 2015/11/17.
 */
public class Unique {

    static Character[] tags = {
        'a', 'b', 'c', 'd', 'e', 'f', 'g',
        'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u',
        'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', '-', '+',
        'A', 'B', 'C', 'D', 'E', 'F', 'G',
        'H', 'I', 'J' ,'K', 'L', 'M', 'N',
        'O', 'P', 'Q', 'R', 'S', 'T', 'U',
        'V', 'W', 'X', 'Y', 'Z'
    };

    public static String generateName(Context context) {
        if (null == SPHelper.getStringSP(context, SPHelper.USER_ID, null)) {
            String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            if (TextUtils.isEmpty(imei)) {
                imei = "1234567890";
            }
            String binary = Long.toBinaryString(Long.parseLong(imei));
            while (binary.length() < 54)
                binary = "0" + binary;
            StringBuffer buffer = new StringBuffer();
            for(int i = 0 ; i < 9 ; i ++) {
                String bla64 = binary.substring(6*i, 6*(i+1));
                int index = Integer.valueOf(bla64, 2);
                buffer.append(tags[index]);
            }
            String name =  buffer.toString();
            SPHelper.putStringSP(context, SPHelper.USER_ID, name);
            return name;
        }
        return SPHelper.getStringSP(context, SPHelper.USER_ID, "MrError");
    }

    public static String generateGavatar(Context context, @Nullable String gender) {
        return "http://eightbitavatar.herokuapp.com/?id="+ Unique.generateName(context)+"&s=male&size=320";
    }

    //111000 110101 111110 101001 001100 011001 111111 111111 11/0000  IMIE最大值 50位 补4位至前面
}
