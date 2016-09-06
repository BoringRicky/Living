package com.liteng.living;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.liteng.living.adapter.LivesAdapter;
import com.liteng.living.entity.Lives;
import com.liteng.living.http.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String url = "http://120.55.238.158/api/live/near_recommend?lc=3000000000011509&cv=IK3.1.10_Android&cc=TG36008&ua=XiaomiMI4LTE&uid=190761403&sid=20Tr1VWDFRc5wxUub4BC6rl55284NDi0VsCuvi2aZi2h59JJRfVDI&devi=867323029795190&imsi=460002330273772&imei=867323029795190&icc=898600520115f0989782&conn=WIFI&vv=1.0.3-2016060211417.android&aid=6524c2b6ae0bb697&osversion=android_23&mtid=4c8b78842db191e46d8639b709d1fa38&mtxid=fcd7333d06da&proto=4&smid=DujlPyXDfceh+88GbEzm+rhiRWdHAXcqw3ASJWkadmdHVmg5HpwVO2vPVauokUmZh2DI3gKxpE7rh4aEPx32U3LA&longitude=116.32758&latitude=39.75431";


    private GridView mGvLivingList;
    private LivesAdapter mAdapter;
    private List<Lives> mLivesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGvLivingList = (GridView)findViewById(R.id.gvLivingList);

        mLivesList = new ArrayList<>();
        mAdapter = new LivesAdapter(this,mLivesList);
        mGvLivingList.setAdapter(mAdapter);


        mGvLivingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,VideoViewActivity.class);
                intent.putExtra("stream_addr",mLivesList.get(position).getStream_addr());
                startActivity(intent);
            }
        });

        new RequestLivingTask().execute(url);

    }

    private class RequestLivingTask extends AsyncTask<String,Void,List<Lives>>{

        ProgressDialog mProgressDialog ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(MainActivity.this,"加载中……",null,false);
        }

        @Override
        protected List<Lives> doInBackground(String... params) {
            String url = params[0];
            List<Lives> liveList = new ArrayList<>();
            try {
                String livingList = HttpUtils.getInstance().get(url);
                JSONObject livingObj = new JSONObject(livingList);
                JSONArray livesArr = livingObj.optJSONArray("lives");
                for (int i = 0; i < livesArr.length(); i++) {
                    JSONObject live = livesArr.optJSONObject(i);
                    Lives lives = new Lives(live);
                    liveList.add(lives);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return liveList;
        }

        @Override
        protected void onPostExecute(List<Lives> lives) {
            super.onPostExecute(lives);
            mLivesList.addAll(lives);
            mAdapter.notifyDataSetChanged();

            mProgressDialog.dismiss();
        }
    }



}
