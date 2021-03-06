package nickita.gq.atomicbombsimulator.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

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
    private View mView, mResultView;
    private TextView mAchievementText;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(EventMessage event) {
        switch (event.getType()) {
            case Values.SHOW_ACHIEVEMENT: {
                final BombedPlace bp = (BombedPlace) event.getValue();
                Log.d("RESULT_FRAGMENT", "RECEIVED BOMB FROM " + bp.getmBombedByCountry());
                Statistics.bobmbsDroppedByCountry(bp.getmBombedByCountry(), new OnReady<Integer>() {
                    @Override
                    public void ready(Integer result) {
                        mAchievementText.setText(
                                bp.getmBombedByCountry() + " proudly participated in nuclear war. There have been " + result + " bombs launched from this territory"
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
        mResultView = (LinearLayout) mView.findViewById(R.id.result_view);
        Button closeResultsButton = (Button) mView.findViewById(R.id.close_results_button);
        closeResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventMessage<Bomb>(null, Values.DROP_ANOTHER));
            }
        });
        Button shareResultsButton = (Button) mView.findViewById(R.id.share_results_button);
        shareResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .setShareHashtag(new ShareHashtag.Builder()
                                .setHashtag("#ConnectTheWorld")
                                .build())
                        .build();
            }
        });
    }
}
