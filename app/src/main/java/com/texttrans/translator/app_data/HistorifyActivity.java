package com.texttrans.translator.app_data;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.texttrans.translator.R;
import com.texttrans.translator.app_data.model.HistItem;

import com.texttrans.translator.app_data.adapter.RecentSearchingAdapter;
import com.texttrans.translator.app_data.dbhelper.DatabaseReferHelper;

import java.util.ArrayList;
import java.util.List;

public class HistorifyActivity extends AppCompatActivity {
    ImageView back1;
    DatabaseReferHelper databaseHelper;
    RecyclerView historify;
    public List<HistItem> historylisting;
    public RecentSearchingAdapter textAdapter;
    ProgressBar progressing;
    TextView textempty;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_historify);
        ImageView imageView = (ImageView) findViewById(R.id.back1);
        this.back1 = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HistorifyActivity.this.onBackPressed();
            }
        });
        DatabaseReferHelper databaseReferHelper = new DatabaseReferHelper (this);

        this.databaseHelper = databaseReferHelper;
        databaseReferHelper.openDataBase();
        this.historify = (RecyclerView) findViewById(R.id.recycler_view);
        this.progressing = (ProgressBar) findViewById(R.id.progress);
        this.textempty = (TextView) findViewById(R.id.tvempty);
        this.historify.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.historify.setItemAnimator(new DefaultItemAnimator());
        new getSearchHistory().execute(new Void[0]);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private class getSearchHistory extends AsyncTask<Void, Void, Void> {


        public void onPreExecute() {
            List unused = HistorifyActivity.this.historylisting = new ArrayList();
            HistorifyActivity.this.historylisting.clear();
            HistorifyActivity.this.progressing.setVisibility(View.VISIBLE);
        }


        public Void doInBackground(Void... voidArr) {
            try {
                HistorifyActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        List unused = HistorifyActivity.this.historylisting = HistorifyActivity.this.databaseHelper.getSearchHistory();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                HistorifyActivity.this.progressing.setVisibility(View.GONE);
            }
            try {
                Thread.sleep(1000);
                return null;
            } catch (InterruptedException e2) {
                e2.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            HistorifyActivity.this.progressing.setVisibility(View.GONE);
            if (HistorifyActivity.this.historylisting.size() == 0) {
                HistorifyActivity.this.textempty.setVisibility(View.VISIBLE);
                return;
            }
            HistorifyActivity.this.textempty.setVisibility(View.GONE);
            HistorifyActivity historifyActivity = HistorifyActivity.this;
            HistorifyActivity historifyActivity2 = HistorifyActivity.this;
            RecentSearchingAdapter unused = historifyActivity.textAdapter = new RecentSearchingAdapter (historifyActivity2, historifyActivity2.historylisting);
            HistorifyActivity.this.historify.setAdapter(HistorifyActivity.this.textAdapter);
        }

    }
}
