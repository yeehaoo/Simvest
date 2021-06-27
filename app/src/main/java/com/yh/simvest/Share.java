package com.yh.simvest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Share {
    @PrimaryKey(autoGenerate = true)
    public int sid;

    @ColumnInfo(name="share_code")
    public String code;

    @ColumnInfo(name="share_lots")
    public double lots;

    public Share (String code, int lots) {
        this.code = code;
        this.lots = lots;
    }

    public Share () {
        this.code = "";
        this.lots = 0;
    }
}
