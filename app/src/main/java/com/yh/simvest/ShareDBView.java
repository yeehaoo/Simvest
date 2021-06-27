package com.yh.simvest;

import androidx.room.DatabaseView;

@DatabaseView("SELECT * FROM share")
public class ShareDBView {
    public int sid;
    public String share_code;
    public int share_lots;
}
