package nickita.gq.atomicbombsimulator.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nickita.gq.atomicbombsimulator.callbacks.OnReady;
import nickita.gq.atomicbombsimulator.model.BombedPlace;

/**
 * Created by admin on 19/7/17.
 */
public class Statistics {
    public static void bobmbsDroppedByCountry(final String country, final OnReady<Integer> c) {
        FirebaseDatabase.getInstance().getReference().child(Values.DB_BOMED_PLACES_LOC).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int bombsDroppedByCountry = 0;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (d.getValue(BombedPlace.class).getmBombedByCountry().equals(country)) {
                        bombsDroppedByCountry++;
                    }
                }
                c.ready(bombsDroppedByCountry);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
