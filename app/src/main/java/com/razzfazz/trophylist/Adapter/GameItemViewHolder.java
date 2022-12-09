package com.razzfazz.trophylist.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.razzfazz.trophylist.R;

public class GameItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView gameTitle;
    ImageView gamePicture;
    ProgressBar gameProgressBar;
    TextView gameBronzeCount;
    TextView gameSilverCount;
    TextView gameGoldCount;
    TextView gamePlatCount;
    GameListAdapter.OnItemListener onItemListener;

    public GameItemViewHolder(View itemView, GameListAdapter.OnItemListener onItemListener) {
        super(itemView);
        gameTitle = itemView.findViewById(R.id.gameItemTitle);
        gamePicture = itemView.findViewById(R.id.gameItemPicture);
        gameProgressBar = itemView.findViewById(R.id.gameItemProgressBar);
        gameBronzeCount = itemView.findViewById(R.id.gameItemBronzeCount);
        gameSilverCount = itemView.findViewById(R.id.gameItemSilverCount);
        gameGoldCount = itemView.findViewById(R.id.gameItemGoldCount);
        gamePlatCount = itemView.findViewById(R.id.gameItemPlatCount);
        this.onItemListener = onItemListener;

        itemView.setOnClickListener(this);
    }

    // returns items adapter position
    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition());
    }
}