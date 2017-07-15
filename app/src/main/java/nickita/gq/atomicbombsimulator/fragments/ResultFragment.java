package nickita.gq.atomicbombsimulator.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import nickita.gq.atomicbombsimulator.R;
import nickita.gq.atomicbombsimulator.ViewPagerAdapter;
import nickita.gq.atomicbombsimulator.model.Bomb;
import nickita.gq.atomicbombsimulator.model.EventMessage;
import nickita.gq.atomicbombsimulator.utils.Values;

/**
 * Created by Nickita on 1/3/17.
 */
public class ResultFragment extends Fragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.result_fragment_layout, container, false);
        setUpViewPager();
        initialize();
        return mView;
    }

    private void setUpViewPager() {
        ViewPager viewPager = (ViewPager) mView.findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), Values.DAMAGE_LEVELS, Values.DAMAGE_DESCRIPTIONS);
        viewPager.setAdapter(adapter);
    }

    private void initialize() {
        Button closeResultsButton = (Button) mView.findViewById(R.id.close_results_button);
        closeResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventMessage<Bomb>(null, Values.DROP_ANOTHER));
            }
        });
    }
}
