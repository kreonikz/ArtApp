package org.com.art.application.artistactivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.wangjie.wheelview.R;


/**
 * Created by Wonka on 03.08.2017.
 */

public class ArtistBiographyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

 Context mContext;
    private String [] mDataList;
    int number;



    public ArtistBiographyAdapter() {
    }

    public ArtistBiographyAdapter(String [] dataList,int number) {
        mDataList = dataList;
        this.number=number;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_artist_picture, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        mContext=parent.getContext();



        return holder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

       // Artist mArtist = mDataList.get(this.number);
        final ItemViewHolder holder1 = (ItemViewHolder) holder;

       // holder1.text.setText(mArtist.get);
       //
        // String[] parts = mArtist.getPictures().split(",");// Здесь получаю урлы на Картины
        // holder.text.setText(direction.getName());


//        Glide.with(mContext)
//                .load(mDataList[position])
//                .placeholder(R.drawable.papiros)
//                .into(holder1.mPicture);


        Glide.with(mContext)
                .load(mDataList[position])
                .placeholder(R.drawable.papiros)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        holder1.mPicture.setImageDrawable(resource);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return mDataList.length;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView text;
        ImageView mPicture;

        public ItemViewHolder(View itemView) {
            super(itemView);
          //  text = (TextView) itemView.findViewById(R.id.horizontal_item_text);
            mPicture= (ImageView) itemView.findViewById(R.id.pictureID);
        }
    }








}
