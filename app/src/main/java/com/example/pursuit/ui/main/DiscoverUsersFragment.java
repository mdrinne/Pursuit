package com.example.pursuit.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
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
    private static final String ARG_PARAM1 = "currentUserId";
    private static final String ARG_PARAM2 = "currentUserRole";

    // TODO: Rename and change types of parameters
    private String currentUserId;
    private String currentUserRole;

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
    public static DiscoverUsersFragment newInstance(String currentUserId, String currentUserRole) {
        DiscoverUsersFragment fragment = new DiscoverUsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, currentUserId);
        args.putString(ARG_PARAM2, currentUserRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUserId = getArguments().getString(ARG_PARAM1);
            currentUserRole = getArguments().getString(ARG_PARAM2);
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

                    if (student != null && !student.getId().equals(currentUserId)) {
                        usersList.add(student);
                    }
                }
            }

            postUsersListener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private void postUsersListener() {
        RecyclerView usersRecycler = getView().findViewById(R.id.users_recycler);
        final UsersListAdapter usersListAdapter = new UsersListAdapter(getContext(), usersList, currentUserId, currentUserRole);
        usersRecycler.setAdapter(usersListAdapter);
        usersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        EditText usersFilter = getView().findViewById(R.id.users_filter);
        usersFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                usersListAdapter.getFilter().filter(s);
            }
        });
    }

}
