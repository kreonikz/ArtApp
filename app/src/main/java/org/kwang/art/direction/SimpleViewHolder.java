package org.kwang.art.direction;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.wangjie.wheelview.R;


/**
 * Created by Wonka on 25.07.2017.
 */
public  class SimpleViewHolder extends  RecyclerView.ViewHolder {
         TextView title;
         RecyclerView horizontalList;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.course_item_name_tv);
            horizontalList = (RecyclerView) itemView.findViewById(R.id.horizontal_list);
        }
    }
