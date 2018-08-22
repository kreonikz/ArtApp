package org.wangjie.wheelview.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.WheelViewAlternative;
import org.wangjie.wheelview.R;


/**
 * Created by Wonka on 28.07.2017.
 */

public class starter_activity extends Activity  {
    Button mStart;
    Context mContext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starter_screen);

        mStart=(Button) findViewById(R.id.start_button);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent=new Intent(starter_activity.this, WheelViewAlternative.class );
                startActivity(intent);
            }
        });


    }


}
