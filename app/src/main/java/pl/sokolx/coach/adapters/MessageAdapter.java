package pl.sokolx.coach.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import pl.sokolx.coach.R;
import pl.sokolx.coach.models.MessageModel;
import pl.sokolx.coach.utils.DateUtils;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageModel> messageList;
    private FirebaseAuth FbAuth;

    public MessageAdapter(List<MessageModel> messageList) {
        this.messageList = messageList;
    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_messages, parent, false));
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {

        FbAuth = FbAuth.getInstance();
        FirebaseUser user = FbAuth.getCurrentUser();
        String userID = user.getUid();
        MessageModel message = messageList.get(position);

        holder.textViewDateMessage.setText(DateUtils.parseDateToString(message.getDateSend()));

        if(message.getKeyFrom().equals(userID)) {

            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
            holder.textViewContentMessage.setBackgroundResource(R.drawable.cloud_message_from_me);
            holder.textViewContentMessage.setTextColor(Color.parseColor("#ffffff"));
            holder.textViewDateMessage.setText(DateUtils.parseDateToString(message.getDateSend()));
            llp.setMargins(100, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
            llp.gravity = Gravity.RIGHT;
            holder.textViewDateMessage.setLayoutParams(llp);
            holder.textViewContentMessage.setLayoutParams(llp);
            holder.textViewContentMessage.setText(message.getContentMessage());

        } else {

            LinearLayout.LayoutParams rrp = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT);
            rrp.setMargins(0, 0, 100, 0); // llp.setMargins(left, top, right, bottom);
            rrp.gravity = Gravity.LEFT;
            holder.textViewDateMessage.setLayoutParams(rrp);
            holder.textViewContentMessage.setLayoutParams(rrp);
            holder.textViewContentMessage.setText(message.getContentMessage());
            holder.textViewContentMessage.setBackgroundResource(R.drawable.cloud_message_to_me);
            holder.textViewContentMessage.setTextColor(Color.parseColor("#000000"));

        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView textViewContentMessage, textViewDateMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);

            textViewDateMessage = (TextView) itemView.findViewById(R.id.textViewDateMessage);
            textViewContentMessage = (TextView) itemView.findViewById(R.id.textViewContentMessage);

        }
    }
}
