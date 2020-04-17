package com.example.pursuit;

import androidx.fragment.app.DialogFragment;

import android.content.Context;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;


public class NewConversationDialogFragment extends DialogFragment {

    public interface NewConversationDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
        public void
    }

    NewConversationDialogListener listener;

    ArrayList<String> allUsernames;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NewConversationDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("The activity must implement NewConversationDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_conversation, null));

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDialogPositiveClick(NewConversationDialogFragment.this);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDialogNegativeClick(NewConversationDialogFragment.this);
            }
        });
        final UsernameAdapter adapter = new UsernameAdapter(this, allUsernames);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: build conversation creation such that only a list item can be clicked in order to start a conversation
                String username = adapter.getItem(which);

            }
        });

        return builder.create();
    }

}
