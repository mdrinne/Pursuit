package com.example.pursuit.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.pursuit.R;
import com.example.pursuit.adapters.UsersListAdapter;
import com.example.pursuit.models.Company;
import com.example.pursuit.models.Employee;
import com.example.pursuit.models.Student;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverUsersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private Student currentStudent;
    private Employee currentEmployee;
    private Company currentCompany;
    private String currentRole;
    private ArrayList<Student> usersList;

    private DatabaseReference dbRef;

    public DiscoverUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentUserId Parameter 1.
     * @return A new instance of fragment DiscoverUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverUsersFragment newInstance(String currentUserId) {
        DiscoverUsersFragment fragment = new DiscoverUsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, currentUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        dbRef = FirebaseDatabase.getInstance().getReference();
        getUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        EditText usersFilter = getView().findViewById(R.id.users_filter);
        return inflater.inflate(R.layout.fragment_discover_users, container, false);
    }

    private void getUsers() {
        Query usersQuery = dbRef.child("Students").orderByChild("id");
        usersQuery.addValueEventListener(usersListener);
    }

    private ValueEventListener usersListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            usersList = new ArrayList<>();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);

                    if (student != null && !student.getId().equals(mParam1)) {
                        usersList.add(student);
                    }
                }
            }

            postUsersListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void postUsersListener() {
        RecyclerView usersRecycler = getView().findViewById(R.id.users_recycler);
        UsersListAdapter usersListAdapter = new UsersListAdapter(getContext(), usersList);
        usersRecycler.setAdapter(usersListAdapter);
        usersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
