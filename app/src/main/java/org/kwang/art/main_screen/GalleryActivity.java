package org.kwang.art.main_screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import junit.framework.Assert;

import org.wangjie.wheelview.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GalleryActivity extends AppCompatActivity {
    public static final String TAG = "GalleryActivity";
    public static final String EXTRA_NAME = "images";

    public static final String EXTRA_NUMBER = "number";

    int orderNumber;

    private String[] _images;
    private GalleryPagerAdapter _adapter;

    @InjectView(R.id.pager)
    ViewPager _pager;
    @InjectView(R.id.thumbnails)
    LinearLayout _thumbnails;
    @InjectView(R.id.btn_close)
    ImageButton _closeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery2);
        ButterKnife.inject(this);

        _images = (String[]) getIntent().getSerializableExtra(EXTRA_NAME);
        Assert.assertNotNull(_images);

        orderNumber = (Integer) getIntent().getSerializableExtra(EXTRA_NUMBER);

        _adapter = new GalleryPagerAdapter(this);
        _pager.setAdapter(_adapter);
        _pager.setCurrentItem(orderNumber);// Ссетит пейджер на определенную позицию


        _pager.setOffscreenPageLimit(_images.length); // how many images to load into memory


        // _pager.setCurrentItem(orderNumber);  // Ссетит пейджер на определенную позицию меняет позицию нижних картинок

        _closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Close clicked");
                finish();
            }
        });
    }


    class GalleryPagerAdapter extends PagerAdapter {

        Context _context;
        LayoutInflater _inflater;

        public GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return _images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = _inflater.inflate(R.layout.pager_gallery_item2, container, false);
            container.addView(itemView);

            // Get the border size to show around each image
            int borderSize = _thumbnails.getPaddingTop();

            // Get the size of the actual thumbnail image
            int thumbnailSize = ((FrameLayout.LayoutParams)
                    _pager.getLayoutParams()).bottomMargin - (borderSize * 2);

            // Set the thumbnail layout parameters. Adjust as required
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
            params.setMargins(0, 0, borderSize, 0);

            // You could also set like so to remove borders
            //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            //        ViewGroup.LayoutParams.WRAP_CONTENT,
            //        ViewGroup.LayoutParams.WRAP_CONTENT);


//            final ImageView thumbView = new ImageView(_context);  // Нижняя полоса с картинами
//            thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            thumbView.setLayoutParams(params);
//            thumbView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "Thumbnail clicked");
//
//
//                    // Set the pager position when thumbnail clicked
//                    _pager.setCurrentItem(position);  // Ссетит пейджер на определенную позицию
//                }
//            });
//
//
//            _thumbnails.addView(thumbView);


            final SubsamplingScaleImageView imageView =
                    (SubsamplingScaleImageView) itemView.findViewById(R.id.image); //  Тут основная картинка

            // Asynchronously load the image and set the thumbnail and pager view

//            Glide.with(_context)
//                    .load(_images[position])
//                    .asBitmap()
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                            imageView.setImage(ImageSource.bitmap(bitmap));
//                          //  thumbView.setImageBitmap(bitmap);
//                        }
//                    });

            orderNumber = (Integer) getIntent().getSerializableExtra(EXTRA_NUMBER);


            Glide.with(_context)
                    .load(_images[position])
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            imageView.setImage(ImageSource.bitmap(bitmap));
                            //  thumbView.setImageBitmap(bitmap);
                        }
                    });


//            Glide.with(_context)  // Заполнение мелкой штуки снизу
//                    .load(_images[0])
//                    .asBitmap()
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                           // imageView.setImage(ImageSource.bitmap(bitmap));
//                            thumbView.setImageBitmap(bitmap);
//                        }
//                    });
//


            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
