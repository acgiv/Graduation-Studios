package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.laureapp.R;
import com.laureapp.ui.card.Segnalazioni.Chat;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.annotation.Nullable;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int Msg_TYPE_RIGHT = 1;
    private List<Chat> mChat;
    private Long id_sender;
    private String imageurl;

    FirebaseUser mUser;


    public MessageAdapter(Context context, List<Chat> mChats, Long id_sender, String imageurl){
        this.mChat = mChats;
        this.mContext = context;
        this.id_sender = id_sender;
        this.imageurl = imageurl;

    }

    @NotNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        if (viewType == Msg_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        Log.d("MessageAdapter", "Message: " + chat.getMessage() + ", Sender: " + chat.getSender());
        holder.show_message.setText(chat.getMessage());

        if (imageurl.equals("Default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
        }else{
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.messaggio);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position){
        if(mChat.get(position).getSender().equals(id_sender)){
            return Msg_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }

    }
}

