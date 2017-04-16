package com.androdheight.firebase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androdheight.firebase.MainActivity;
import com.androdheight.firebase.R;
import com.androdheight.firebase.model.PlayerModel;

import java.util.ArrayList;

/**
 * Created by prabhakar on 11/04/17.
 */

public class PlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    private ArrayList<PlayerModel> playerlist;
    Context context;
    static OnListItemClickListener listItemClickListener;

    public  interface OnListItemClickListener{
        public  void onItemClickL(View view, int position);
    }


    public  class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Name,tv_Match,tv_Inning,tv_Run,tv_Wicket,tv_BestRun,tv_BestBowling,tv_Average;


        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_Name = (TextView) itemView.findViewById(R.id.tv_Name);
            tv_Match = (TextView) itemView.findViewById(R.id.tv_Match);
            tv_Inning = (TextView) itemView.findViewById(R.id.tv_Inning);
            tv_Run = (TextView) itemView.findViewById(R.id.tv_Run);
            tv_Wicket = (TextView) itemView.findViewById(R.id.tv_Wicket);
            tv_BestRun = (TextView) itemView.findViewById(R.id.tv_BestRun);
            tv_BestBowling = (TextView) itemView.findViewById(R.id.tv_BestBowling);
            tv_Average = (TextView) itemView.findViewById(R.id.tv_Average);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItemClickListener.onItemClickL(itemView,getAdapterPosition());
                }
            });

        }
    }


    class MyViewHolderHeader extends RecyclerView.ViewHolder{

        public MyViewHolderHeader(View itemView) {
            super(itemView);

        }
    }


    private PlayerModel getItem (int position) {
        return playerlist.get (position);
    }



    public PlayerRecyclerAdapter(Context context,ArrayList<PlayerModel> playerlist) {

        listItemClickListener= (MainActivity) context;
        this.context = context;
        this.playerlist = playerlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item_list_header, parent, false);
            return new MyViewHolderHeader(v);
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.player_item_list, parent, false);


            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder,  int listPosition) {
        if(viewHolder instanceof MyViewHolder) {
          //  PlayerModel currentItem = getItem (listPosition - 1);
            MyViewHolder holder = (MyViewHolder) viewHolder;

            holder.tv_Name.setText(playerlist.get(listPosition-1).getPlayer_name());
            holder.tv_Match.setText(playerlist.get(listPosition-1).getPlayer_match());
            holder.tv_Inning.setText(playerlist.get(listPosition-1).getPlayer_inning());
            holder.tv_Run.setText(playerlist.get(listPosition-1).getPlayer_run());
            holder.tv_Wicket.setText(playerlist.get(listPosition-1).getPlayer_wicket());
            holder.tv_BestRun.setText(playerlist.get(listPosition-1).getPlayer_bestRun());
            holder.tv_BestBowling.setText(playerlist.get(listPosition-1).getPlayer_bestBowling());
            holder.tv_Average.setText(playerlist.get(listPosition-1).getAverage());

        }
    }


    @Override
    public int getItemViewType (int position) {
        if(isPositionHeader (position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader (int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return playerlist.size()+1;
    }
}