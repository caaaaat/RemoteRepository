package com.test.firebasetast.areaTemp;



import androidx.appcompat.app.ActionBar;

import java.util.HashMap;

public class common {

    //回傳 地區報表代碼
    public HashMap areaNumber(){
        HashMap<String,String> result = new HashMap();
        result.put("臺北市","063");
        result.put("新北市","071");
        result.put("基隆市","051");
        result.put("桃園市","007");
        result.put("新竹縣","011");
        result.put("新竹市","055");
        result.put("苗栗縣","015");
        result.put("臺中市","075");
        result.put("南投縣","023");
        result.put("彰化縣","019");
        result.put("雲林縣","027");
        result.put("嘉義縣","031");
        result.put("嘉義市","059");
        result.put("臺南市","079");
        result.put("高雄市","067");
        result.put("屏東縣","035");
        result.put("宜蘭縣","003");
        result.put("花蓮縣","043");
        result.put("臺東縣","039");
        result.put("澎湖縣","047");
        result.put("金門縣","087");
        result.put("連江縣","083");

        return result;
    }

}
