package com.yh.simvest;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ShareDao {
    @Query("SELECT * FROM share WHERE NOT share_code = 'cash' ")
    List<Share> getAll();

    @Query("SELECT * FROM share WHERE share_code = 'cash' ")
    Share getCash();

    @Query("SELECT COUNT(*) FROM share WHERE NOT share_code = 'cash'")
    int getCount();

    @Query("SELECT * FROM share WHERE sid IN (:shareIds)")
    List<Share> loadAllByIds(int[] shareIds);

    @Query("SELECT * FROM share WHERE share_code LIKE :code LIMIT 1")
    Share findByCode(String code);

    @Insert
    void insertAll(Share... shares);

    @Update
    public void updateShares(Share... shares);

    @Delete
    void delete(Share share);
}
