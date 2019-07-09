package org.kwang.art.direction;

import android.content.Context;
import android.content.Intent;
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
import org.kwang.art.main_screen.GalleryActivity;

import java.util.ArrayList;


/**
 * Created by Wonka on 20.07.2017.
 */

public class HorizontalRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> images = new ArrayList<String>();


    private String[] mDataList;
    Context mContext;
    int number;

    public HorizontalRVAdapter() {
    }

    public HorizontalRVAdapter(String[] dataList,int number) {
        mDataList = dataList;
        this.number=number;
    }




    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView text;
        ImageView mPicture;

        public ItemViewHolder(View itemView) {
            super(itemView);
            //text = (TextView) itemView.findViewById(R.id.horizontal_item_text);
            mPicture= (ImageView) itemView.findViewById(R.id.pictureID);
        }
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.horizontal_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        mContext=parent.getContext();
        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, final int position) {

       // Direction direction = mDataList.get(this.number);

        final ItemViewHolder holder = (ItemViewHolder) rawHolder;

      //  holder.text.setText(direction.getName());

       // String[] parts = direction.getUrlImage().split(",");// Здесь получаю урлы на Картины


        // holder.text.setText(direction.getName());
        Glide.with(mContext)
                .load(mDataList[position])
                .placeholder(R.drawable.papiros)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        holder.mPicture.setImageDrawable(resource);
                    }
                });
                //.into(holder.mPicture);


        for(int i=0;i<mDataList.length;i++){
            images.add(mDataList[i]);  // Adding pictures to gallery class

            if(position==i){


            }
        }


        holder.mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // тут данные надо перепихать в галлери



                Intent intent = new Intent(holder.mPicture.getContext(), GalleryActivity.class);

                intent.putExtra(GalleryActivity.EXTRA_NAME, mDataList);

                intent.putExtra(GalleryActivity.EXTRA_NUMBER,position);

                holder.mPicture.getContext().startActivity(intent);


            }
        });



    }

    @Override
    public int getItemCount() {
        return mDataList.length;
    }

}