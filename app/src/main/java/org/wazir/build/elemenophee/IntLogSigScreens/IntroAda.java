package org.wazir.build.elemenophee.IntLogSigScreens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.wazir.build.elemenophee.R;

import java.util.List;

public class IntroAda extends PagerAdapter {
    private Context mContext;
    private List<IntroMoObj> mList;

    IntroAda(Context mContext, List<IntroMoObj> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.elemet_layout, null);

        ImageView imgSlide = layoutScreen.findViewById(R.id.id_intro_img);
        TextView description = layoutScreen.findViewById(R.id.id_intro_desc);
        TextView title = layoutScreen.findViewById(R.id.id_intro_title);

        description.setText(mList.get(position).getIntro_message());
        imgSlide.setImageResource(mList.get(position).getIntro_img());
        title.setText(mList.get(position).getIntro_title());

        container.addView(layoutScreen);
        return layoutScreen;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
