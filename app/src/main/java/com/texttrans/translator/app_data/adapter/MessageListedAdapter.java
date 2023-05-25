package com.texttrans.translator.app_data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.texttrans.translator.R;
import com.texttrans.translator.app_data.model.VoiceChatingItem;


import java.util.List;

public class MessageListedAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<VoiceChatingItem> mMessageList;

    public int getItemViewType(int i) {
        return i;
    }

    public MessageListedAdapter(Context context, List<VoiceChatingItem> list) {
        this.mContext = context;
        this.mMessageList = list;
    }

    public int getItemCount() {
        return this.mMessageList.size();
    }

    public void add(VoiceChatingItem voiceChatingItem) {
        this.mMessageList.add(voiceChatingItem);
        notifyDataSetChanged();
    }

    public long getItemId(int i) {
        return super.getItemId(i);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ReceivedMessageHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_received, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((ReceivedMessageHolder) viewHolder).bind(this.mMessageList.get(i));
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        LinearLayout leyout_left;
        LinearLayout leyout_right;
        TextView messageText;
        TextView messageText1;
        TextView timeText;
        TextView timeText1;

        ReceivedMessageHolder(View view) {
            super(view);
            this.timeText = (TextView) view.findViewById(R.id.text_message_name);
            this.messageText = (TextView) view.findViewById(R.id.text_message_body);
            this.timeText1 = (TextView) view.findViewById(R.id.text_message_name1);
            this.messageText1 = (TextView) view.findViewById(R.id.text_message_body1);
            this.leyout_left = (LinearLayout) view.findViewById(R.id.leyout_left);
            this.leyout_right = (LinearLayout) view.findViewById(R.id.leyout_right);
        }


        public void bind(VoiceChatingItem voiceChatingItem) {
            if (voiceChatingItem.getType().equals("sender")) {
                this.leyout_right.setVisibility(View.VISIBLE);
                this.leyout_left.setVisibility(View.INVISIBLE);
                this.timeText1.setText(voiceChatingItem.getLan2());
                this.messageText1.setText(voiceChatingItem.getStr2());
                return;
            }
            this.leyout_right.setVisibility(View.INVISIBLE);
            this.leyout_left.setVisibility(View.VISIBLE);
            this.timeText.setText(voiceChatingItem.getLan1());
            this.messageText.setText(voiceChatingItem.getStr1());
        }
    }
}
