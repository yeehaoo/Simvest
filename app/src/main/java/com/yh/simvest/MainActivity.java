package com.yh.simvest;

/**
 * Simvest by yeehaoo
 * 21/11/2020
 * Simple investment simulator application.
 *
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_buy, btn_sell;
    EditText et_lots, et_code, et_price;
    TextView tv_status, tv_cash, tv_share1, tv_share2, tv_share3, tv_share4, tv_share5;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    double cashBalance;
    MyAdapter myAdapter;

    //room
    AppDatabase db;
    ShareDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cashBalance = 0;

        //initialising views
        btn_buy = findViewById(R.id.btn_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = et_code.getText().toString();
                double price = Double.parseDouble(et_price.getText().toString());
                double lots = Double.parseDouble(et_lots.getText().toString());
                trade(code, price, lots, true);
                myAdapter.notifyDataSetChanged();
            }
        });
        btn_sell = findViewById(R.id.btn_sell);
        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = et_code.getText().toString();
                double price = Double.parseDouble(et_price.getText().toString());
                double lots = Double.parseDouble(et_lots.getText().toString());
                trade(code, price, lots, false);
            }
        });
        et_lots = findViewById(R.id.et_lots);
        et_code = findViewById(R.id.et_code);
        et_price = findViewById(R.id.et_price);
        tv_status = findViewById(R.id.tv_status);
        tv_cash = findViewById(R.id.tv_cash);
        linearLayout = findViewById(R.id.linearLayout);

        //room
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "share").allowMainThreadQueries().build();
        dao = db.shareDao();

        //recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter(this, dao.getAll());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initialise cash. check if cash exists in table, if not, insert 5000 starting cash. display amount in tv_cash
        if(dao.getCash() == null) {
            Share temp = new Share("cash", 5000);
            dao.insertAll(temp);
            cashBalance = temp.lots;
        } else {
            cashBalance = dao.getCash().lots;
        }
        tv_cash.setText(String.valueOf(cashBalance));
    }

    //buy x lots of shares of code y at z price
    //very similar to sell(), probably can look into combining the code
    //can also look into fetching price of share online
    private void trade(String code, double price, double lots, boolean bool_buying) {
        double totalPrice = price * lots;
        if (cashBalance - totalPrice < 0 & bool_buying == true) {
            tv_status.setText("ERROR: Insufficient balance.");
        }
        else if (lots < 100) {
            tv_status.setText("ERROR: Lots < 100");
        }
        else {
            //1. look for share in db
            //2. if present: get share and update
            //3. if not: create new share
            Share temp_share = dao.findByCode(code);
            if (temp_share == null) {
                if(bool_buying == false) {
                    //user is selling a share that he does not own
                    tv_status.setText("ERROR: You do not currently own this share.");
                }
                else {
                    //user is buying a new share
                    //create new share
                    temp_share = new Share();
                    temp_share.lots = lots;
                    temp_share.code = code;
                    dao.insertAll(temp_share);
                    updateCash(-totalPrice);
                    tv_status.setText("Successfully bought " + lots + " x " + code + " for " + price +" each.");
                }
            }
            else {
                //update existing share
                if(bool_buying == true) {
                    //user acquires new lots of x share
                    temp_share.lots += lots;
                    dao.updateShares(temp_share);
                    updateCash(-totalPrice);
                    tv_status.setText("Successfully bought " + lots + " x " + code + " for " + price +" each.");
                }
                else {
                    //user sells x lots of y share, check if he is trying to sell more than he owns
                    if (temp_share.lots - lots < 0){
                        tv_status.setText("ERROR: You are trying to sell more lots than you own.");
                    }
                    else {
                        temp_share.lots -= lots;
                        //if there are no remaining lots, delete the row. else, update the row
                        if (temp_share.lots == 0) {
                            dao.delete(temp_share);
                        } else {
                            dao.updateShares(temp_share);
                        }
                        updateCash(totalPrice);
                        tv_status.setText("Successfully sold " + lots + " x " + code + " for " + price +" each.");
                    }
                }
            }
        }
    }

    //get cash, change value, update to db
    private void updateCash (double amount) {
        Share temp = dao.getCash();

        temp.lots += amount;
        cashBalance += amount;

        dao.updateShares(temp);
        tv_cash.setText(String.valueOf(cashBalance));
        myAdapter.listShare = dao.getAll();

        myAdapter.notifyDataSetChanged();
    }

}