package com.texttrans.translator.app_data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.texttrans.translator.R;
import com.texttrans.translator.app_data.dbhelper.DatabaseReferHelper;
import com.texttrans.translator.app_data.model.HistItem;


import java.util.List;

public class RecentSearchingAdapter extends RecyclerView.Adapter<RecentSearchingAdapter.MyViewHolder> {

    public List<HistItem> dataList;
    DatabaseReferHelper dbHelper;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView delete;
        public TextView lang1;
        public TextView lang2;
        public TextView str1;
        public TextView str2;

        public MyViewHolder(View view) {
            super(view);
            this.lang1 = (TextView) view.findViewById(R.id.lang1);
            this.str1 = (TextView) view.findViewById(R.id.str1);
            this.lang2 = (TextView) view.findViewById(R.id.lang2);
            this.str2 = (TextView) view.findViewById(R.id.str2);
            this.delete = (ImageView) view.findViewById(R.id.delete);
        }
    }

    public RecentSearchingAdapter(Context context, List<HistItem> list) {
        this.mContext = context;
        this.dataList = list;
        DatabaseReferHelper databaseReferHelper = new DatabaseReferHelper (context);
        this.dbHelper = databaseReferHelper;
        databaseReferHelper.openDataBase();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recentsearch_list_row, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        final HistItem histItem = this.dataList.get(i);
        myViewHolder.lang1.setText(histItem.getLan1());
        myViewHolder.str1.setText(histItem.getStr1());
        myViewHolder.lang2.setText(histItem.getLan2());
        myViewHolder.str2.setText(histItem.getStr2());
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (RecentSearchingAdapter.this.dbHelper.Delete_History_id(histItem.getId())) {
                    RecentSearchingAdapter.this.dataList.remove(i);
                    RecentSearchingAdapter.this.notifyItemRemoved(i);
                    RecentSearchingAdapter recentSearchingAdapter = RecentSearchingAdapter.this;
                    recentSearchingAdapter.notifyItemRangeChanged(i, recentSearchingAdapter.dataList.size());
                    return;
                }
                Toast.makeText(RecentSearchingAdapter.this.mContext, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return this.dataList.size();
    }
}
