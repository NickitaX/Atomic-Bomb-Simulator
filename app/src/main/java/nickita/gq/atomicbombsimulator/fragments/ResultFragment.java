package nickita.gq.atomicbombsimulator.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nickita.gq.atomicbombsimulator.R;
import nickita.gq.atomicbombsimulator.callbacks.OnReady;
import nickita.gq.atomicbombsimulator.model.Bomb;
import nickita.gq.atomicbombsimulator.model.BombedPlace;
import nickita.gq.atomicbombsimulator.model.EventMessage;
import nickita.gq.atomicbombsimulator.utils.Statistics;
import nickita.gq.atomicbombsimulator.utils.Values;

/**
 * Created by Nickita on 1/3/17.
 */
public class ResultFragment extends Fragment {
    private View mView;
    private TextView mAchievementText;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(EventMessage event) {
        switch (event.getType()) {
            case Values.SHOW_ACHIEVEMENT: {
                final BombedPlace bp = (BombedPlace) event.getValue();
                Log.d("RESULT_FRAGMENT", "RECEIVED BOMB FROM "+bp.getmBombedByCountry());
                Statistics.bobmbsDroppedByCountry(bp.getmBombedByCountry(), new OnReady<Integer>() {
                    @Override
                    public void ready(Integer result) {
                        mAchievementText.setText(
                                bp.getmBombedByCountry() + " is proudly participated in nuclear war. There have been " + result + " bombs launched from this territory"
                        );
                    }
                });
                break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.result_fragment_layout, container, false);
        initialize();
        return mView;
    }

    private void initialize() {
        EventBus.getDefault().register(this);
        mAchievementText = (TextView) mView.findViewById(R.id.achievement_text);
        Button closeResultsButton = (Button) mView.findViewById(R.id.close_results_button);
        closeResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventMessage<Bomb>(null, Values.DROP_ANOTHER));
            }
        });
    }
}
