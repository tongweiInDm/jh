package com.tw.slideconfict;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.vp_test_vp);
        PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Object instantiateItem(@NonNull ViewGroup container, final int position) {
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View view = layoutInflater.inflate(R.layout.page_view_content, container, false);
                final TextView textView = view.findViewById(R.id.tv_page_content);
                textView.setText("page index " + position);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(textView, new Property<TextView, Float>(Float.class, "tongwei") {

                            @Override
                            public void set(TextView object, Float value) {
                                object.setText(String.valueOf((long)(value * 1000)));
                            }

                            @Override
                            public Float get(TextView object) {
                                float floatValue = -1;
                                try {
                                    floatValue = Float.parseFloat(object.getText().toString());
                                } catch (Exception e) {
                                    //ignore
                                }
                                return floatValue;
                            }
                        }, 0,1).setDuration(3000);
                        animator.start();

                        Toast.makeText(MainActivity.this, position + " clicked.", Toast.LENGTH_SHORT).show();
                    }
                });
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }
        };
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
