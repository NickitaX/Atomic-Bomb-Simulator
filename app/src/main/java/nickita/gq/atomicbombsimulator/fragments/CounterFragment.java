package nickita.gq.atomicbombsimulator.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nickita.gq.atomicbombsimulator.R;
import nickita.gq.atomicbombsimulator.utils.Values;

/**
 * Created by admin on 18/7/17.
 */
public class CounterFragment extends Fragment {
    private View mView;
    private TextView mBombCounter;
    private ProgressBar mBombCapacity;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.counter_fragment_layout ,container, false);
        initialize();
        setUpCounters();
        return mView;
    }

    private void initialize(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mBombCounter = (TextView) mView.findViewById(R.id.bomb_counter);
        mBombCapacity = (ProgressBar) mView.findViewById(R.id.load_progress);
    }

    private void setUpCounters(){
        mDatabase.child(Values.DB_BOMED_PLACES_LOC).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int size = 0;
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    size++;
                }
                mBombCounter.setText(String.valueOf(size));
                mBombCapacity.setProgress(size/10);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
