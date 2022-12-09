package com.razzfazz.trophylist.Adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razzfazz.trophylist.GameDatabase.DBHelper;
import com.razzfazz.trophylist.R;

public class GameListAdapter extends RecyclerView.Adapter<GameItemViewHolder> {


    private Context mContext;
    private Cursor mCursor;
    private OnItemListener mOnItemListener;

    private static int[] genreIconArray = getGenreIcons();

    // array of genre icons
    public static int[] getGenreIcons() {
        int[] local = {R.drawable.ic_genre_action,
                R.drawable.ic_genre_adventure,
                R.drawable.ic_genre_actionadventure,
                R.drawable.ic_genre_sports,
                R.drawable.ic_genre_shooter,
                R.drawable.ic_genre_platformer,
                R.drawable.ic_genre_racing,
                R.drawable.ic_genre_rpg,
                R.drawable.ic_genre_puzzle,
                R.drawable.ic_genre_simulation,
                R.drawable.ic_genre_strategy};
        return local;
    }

    public GameListAdapter(Context context, Cursor cursor, OnItemListener onItemListener) {
        mContext = context;
        mCursor = cursor;
        mOnItemListener = onItemListener;
    }

    @Override
    public GameItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.game_list_item_layout, parent, false);
        return new GameItemViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(GameItemViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)) {
            return;
        }
        holder.gameTitle.setText(mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_TITLE)));
        holder.gamePicture.setImageResource(genreIconArray[mCursor.getInt(mCursor.getColumnIndex(DBHelper.COLUMN_PICTURE))]);
        holder.gameBronzeCount.setText(String.valueOf(mCursor.getInt(mCursor.getColumnIndex(DBHelper.COLUMN_PROGRESS_BRONZE))));
        holder.gameSilverCount.setText(String.valueOf(mCursor.getInt(mCursor.getColumnIndex(DBHelper.COLUMN_PROGRESS_SILVER))));
        holder.gameGoldCount.setText(String.valueOf(mCursor.getInt(mCursor.getColumnIndex(DBHelper.COLUMN_PROGRESS_GOLD))));
        holder.gamePlatCount.setText(String.valueOf(mCursor.getInt(mCursor.getColumnIndex(DBHelper.COLUMN_PROGRESS_PLAT))));
        holder.gameProgressBar.setProgress(mCursor.getInt(mCursor.getColumnIndex(DBHelper.COLUMN_PERCENT)));
        long id = mCursor.getInt(mCursor.getColumnIndex(DBHelper.COLUMN_ID));
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    // updates the Cursor to refresh the RecyclerView
    public void swapCursor(Cursor newCursor) {
        if(mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;

        if(newCursor != null) {
            notifyDataSetChanged();
        }
    }

    // returns the Cursor
    public Cursor getCursor() {
        return mCursor;
    }

    // interface needed to make the items in the RecyclerView clickable
    public interface OnItemListener {
        void onItemClick(int position);
    }

}
