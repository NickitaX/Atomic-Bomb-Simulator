package nickita.gq.atomicbombsimulator.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nickita.gq.atomicbombsimulator.R;
import nickita.gq.atomicbombsimulator.model.Bomb;
import nickita.gq.atomicbombsimulator.model.EventMessage;
import nickita.gq.atomicbombsimulator.utils.Values;

/**
 * Created by Nickita on 1/3/17.
 */
public class BombConfigurationFragment extends Fragment {
    private View mView;
    private TextView mIndicator;
    private int mKilotons = 100;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessage event) {
        switch (event.getType()) {
            case Values.DROP_BOMB: {
                break;
            }
            case Values.PREPARE_BOMB: {
                EventBus.getDefault().post(new EventMessage<Bomb>(new Bomb(mKilotons), Values.DROP_BOMB));
                break;
            }
        }
    }

    private void setUpSeekBar() {
        SeekBar seekBar = (SeekBar) mView.findViewById(R.id.seek_bar_configuration);
        mIndicator = (TextView) mView.findViewById(R.id.kilotons_indicator);
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mKilotons = i*100;
                mIndicator.setText(String.valueOf(i*100)+Values.BOMB_POWER_SELECTOR_LABEL);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mView = inflater.inflate(R.layout.bomb_configuration_fragment_layout, container, false);
        setUpSeekBar();
        return mView;
    }
}
