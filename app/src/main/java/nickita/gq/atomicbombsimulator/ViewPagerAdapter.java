package nickita.gq.atomicbombsimulator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Nickita on 23/4/17.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private String[] mDamageLevels;
    private String[] mDamageDescriptions;

    public ViewPagerAdapter(Context context, String[] damageLevels, String[] damageDescriptions) {
        this.mContext = context;
        this.mDamageDescriptions = damageDescriptions;
        this.mDamageLevels = damageLevels;
    }

    @Override
    public int getCount() {
        return mDamageLevels.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView damageDescriptionItem;
        ImageView damageGraphicItem;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,
                false);
        damageDescriptionItem = (TextView) itemView.findViewById(R.id.damageDescription);
        damageGraphicItem = (ImageView) itemView.findViewById(R.id.damageGraphic);
        damageDescriptionItem.setText(mDamageDescriptions[position]);
        switch (mDamageLevels[position]) {
            case "Level 1":
                damageGraphicItem.setImageResource(R.drawable.red_circle);
                break;
            case "Level 2":
                damageGraphicItem.setImageResource(R.drawable.orange_circle);
                break;
            case "Level 3":
                damageGraphicItem.setImageResource(R.drawable.blue_circle);
                break;
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
