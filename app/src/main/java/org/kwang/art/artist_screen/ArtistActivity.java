package org.kwang.art.artist_screen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.WheelViewAlternative;
import org.kwang.art.dto.Artist;
import org.wangjie.wheelview.R;
import org.kwang.art.game.SixStep;

import java.util.ArrayList;



public class ArtistActivity extends Activity {
    ProgressBar progressBar;
    RecyclerView mRecyclerViewPicture;
    TextView mTitle,mDates,mBiography,mTitleDate,mTitleAbout;
    ImageView mImageArtist;
    Firebase ref;
    ArrayList<Artist> mArtistList;
    LinearLayoutManager layoutManager;

    int mMumber,mCondition;

    String mFirebaseUrl;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_artistbiography);

        Toolbar toolbar = findViewById(R.id.toolbar);


        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).
                withName(R.string.drawer_item_home).withSelectable(false).withTextColor(Color.BLACK);
        // SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_paintings).withSelectable(false).withTextColor(Color.BLACK);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().
                withIdentifier(2).withName(R.string.malevichGame).withSelectable(false).withTextColor(Color.BLACK);

        //  item1.withName("A new name for this drawerItem").withBadge("19").withBadgeStyle(new BadgeStyle().withTextColor(Color.BLACK));
//notify the drawer about the updated element. it will take care about everything else


//create the drawer and remember the `Drawer` result object


        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.i_007)

                .build();

//Now create your drawer and pass the AccountHeader.Result

        //additional Drawer setup as shown above

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        //new DividerDrawerItem(),
                        // item2,
                        item3
                        // new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                ).withTranslucentStatusBar(false).withAccountHeader(headerResult).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if (position == 1) {
                            Intent intent = new Intent(view.getContext(), WheelViewAlternative.class);
                            startActivity(intent);
                        }

                        if (position == 2) {
                            Intent intent = new Intent(view.getContext(), SixStep.class);
                            startActivity(intent);
                        }
                        return false;
                    }
                }).withSelectedItem(-1)

                .build();


//        String imgpath = "/mnt/sdcard/joke.png";
//
//        String result = imgpath.substring(imgpath.lastIndexOf("/") + 1);
//        System.out.println("Image name " + result);


        View targetView = findViewById(R.id.mDateOfBirth);
        targetView.getParent().requestChildFocus(targetView, targetView);

        mArtistList = new ArrayList<>();

        mRecyclerViewPicture = (RecyclerView) findViewById(R.id.horizontal_artist_list);
        mTitle = (TextView) findViewById(R.id.artist_title);
        mDates = (TextView) findViewById(R.id.mDateOfBirth);
        mBiography = (TextView) findViewById(R.id.mBiography);
        mImageArtist = (ImageView) findViewById(R.id.artist_portreit);
        mTitleAbout = findViewById(R.id.textAbout);
        mTitleDate = findViewById(R.id.textDate);
        progressBar = findViewById(R.id.progressBar);


        progressBar.setVisibility(View.VISIBLE);

        mTitleDate.setVisibility(View.INVISIBLE);
        mTitleAbout.setVisibility(View.INVISIBLE);


        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        layoutManager.scrollToPositionWithOffset(1, 20);
        //recycler_artist.setHasFixedSize(false);
        mRecyclerViewPicture.setLayoutManager(layoutManager);

        //Firebase.setAndroidContext(this);


        // ref = new Firebase("https://fir-data-976bd.firebaseio.com/users/Da Vinci/artists"); //Root URL

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();


        intent = getIntent();
        String srt = intent.getStringExtra("Number");// Получаю из Направления номер активити

        String[] parts = srt.split(",");


        mMumber = Integer.parseInt(parts[0]);// Номер худодника

        mCondition = Integer.parseInt(parts[1]);// Номер моего направления

        switch (mCondition) // Беру художников по направлению из таймлайна передаю число в направление, оттуда в адаптер из адаптера в активиту Художника
        {
            case 0:
                mFirebaseUrl = "Направления/A/artists";
                break;
            case 1:
                mFirebaseUrl = "Направления/B/artists";
                break;
            case 2:
                mFirebaseUrl = "Направления/C/artists";
                break;
            case 3:
                mFirebaseUrl = "Направления/D/artists";
                break;
            case 4:
                mFirebaseUrl = "Направления/E/artists";
                break;
            case 5:
                mFirebaseUrl = "Направления/F/artists";
                break;
            case 6:
                mFirebaseUrl = "Направления/G/artists";
                break;
            case 7:
                mFirebaseUrl = "Направления/H/artists";
                break;
            default:
                mFirebaseUrl = "Направления/I/artists";
                break;


        }


        DatabaseReference childReference = databaseReference.child(mFirebaseUrl);


        childReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int count = 0;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    count++;
                    if (count >= dataSnapshot.getChildrenCount()) {
                        //stop progress bar here

                        progressBar.setVisibility(View.INVISIBLE);

                        mTitleDate.setVisibility(View.VISIBLE);
                        mTitleAbout.setVisibility(View.VISIBLE);

                    }


                    Artist user = child.getValue(Artist.class);
                    mArtistList.add(user);


                }

                String[] parts = mArtistList.get(mMumber).getPictures().split(",");


                mRecyclerViewPicture.setAdapter(new ArtistBiographyAdapter(parts, mMumber));// Список с картинами , беру массив из строк (там урлы на картинки) вместо массива классов


                mTitle.setText(mArtistList.get(mMumber).getName());
                mDates.setText(mArtistList.get(mMumber).getDates());
                mBiography.setText(mArtistList.get(mMumber).getBiography());

//        Glide.with(getApplicationContext())//Artist Icon
//                .load(mArtistList.get(mMumber).getIconPicture())
//                .placeholder(R.drawable.papiros)
//                .into(mImageArtist);


                Glide.with(getApplicationContext())
                        .load(mArtistList.get(mMumber).getIconPicture())
                        .placeholder(R.drawable.rama)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                mImageArtist.setImageDrawable(resource);
                            }
                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

