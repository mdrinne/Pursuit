package com.example.pursuit;

import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.util.Log;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;


public class NewConversationDialogFragment extends DialogFragment {
    private String TAG = "NewConversationDialogFragment";

    public interface NewConversationDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NewConversationDialogListener listener;

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

//        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
////                Activity messagesActivity = getActivity();
////                ((MessagesActivity)messagesActivity).createConversation();
//                Log.d(TAG, "clicked the confirm button");
//
//            }
//        });
//        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                NewConversationDialogFragment.this.getDialog().cancel();
//            }
//        });
        return builder.create();
    }

}
