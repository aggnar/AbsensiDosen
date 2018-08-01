package com.vincent.absensidosen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Squix on 2/22/2018.
 */

public class Dosen extends Fragment {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView textNamaDosen, textJamDosen, textRuangDosen, textStatusDosen, textMatkulDosen;

    //pake public instead of protected karena kalau pake public ga terdefinisi soalnya kita pake createView
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dosen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Dosen");

        //id text yang bakal muncul buat nama, pake getView() karena pada class ini merupakan Fragment
        textNamaDosen = (TextView) getView().findViewById(R.id.txtNamaDosen);
        textJamDosen = (TextView) getView().findViewById(R.id.txtJamDosen);
        textRuangDosen = (TextView) getView().findViewById(R.id.txtRuangDosen);
        textStatusDosen = (TextView) getView().findViewById(R.id.txtStatusDosen);
        textMatkulDosen = (TextView) getView().findViewById(R.id.txtMatkulDosen);

        //auth data untuk ngambil nama dari database
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            textNamaDosen.setText(String.valueOf(dataSnapshot.child("namaDosen").getValue()));
                            textMatkulDosen.setText(String.valueOf(dataSnapshot.child("matkulDosen").getValue()));
                            textRuangDosen.setText(String.valueOf(dataSnapshot.child("ruangDosen").getValue()));
                            textJamDosen.setText(String.valueOf(dataSnapshot.child("jamDosen").getValue()));
                            textStatusDosen.setText(String.valueOf(dataSnapshot.child("statusDosen").getValue()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    //pake getActivity() karena pada class ini merupakan Fragment, kalau gapake getActivity() si finish() ga ngerti apa yang harus di finish()
                    getActivity().finish();
                }
            }
        };
    }
}
