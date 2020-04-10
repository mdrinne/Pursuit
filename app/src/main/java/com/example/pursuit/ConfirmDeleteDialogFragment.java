package com.example.pursuit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;

public class ConfirmDeleteDialogFragment extends DialogFragment {

    public interface ConfirmDeleteDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmDeleteDialogFragment.ConfirmDeleteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("The activity must implement NewConversationDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.confirm_delete_conversation);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDialogPositiveClick(ConfirmDeleteDialogFragment.this);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDialogNegativeClick(ConfirmDeleteDialogFragment.this);
            }
        });

        return builder.create();
    }
}
