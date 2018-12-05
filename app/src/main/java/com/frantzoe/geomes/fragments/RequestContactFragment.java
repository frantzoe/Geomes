package com.frantzoe.geomes.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.frantzoe.geomes.R;
import com.frantzoe.geomes.activities.RequestActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


public class RequestContactFragment extends Fragment {

    private Set<String> numbers;
    private boolean contSelected;

    private ChipGroup chgNumbers;
    private TextView txtContactPick;
    private TextView txtDialpadAdd;

    private static final int PICK_CONTACT = 1;

    public RequestContactFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_request_contact, container, false);
        chgNumbers = rootView.findViewById(R.id.chgNumbers);
        txtDialpadAdd = rootView.findViewById(R.id.txtDialpadAdd);
        txtContactPick = rootView.findViewById(R.id.txtContactPick);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numbers = new HashSet<>();
        txtContactPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(ContactsContract.Contacts.CONTENT_URI, ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        txtDialpadAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = getLayoutInflater();
                final View input = inflater.inflate(R.layout.number_input, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Alert Dialog");
                alert.setView(input);
                alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String number = ((EditText) input).getText().toString().trim();
                        if (number.length() > 0 && numbers.add(number)) {
                            addNewChip(number, getActivity().getDrawable(R.drawable.ic_dialpad_black_24dp));
                            contSelected = true;
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK && data != null) {
            final Uri contactData = data.getData();
            if (contactData != null) {
                final Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    final String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    cursor.close();

                    if (numbers.add(phoneNumber)) {
                        addNewChip(phoneNumber, getActivity().getDrawable(R.drawable.ic_person_add_black_24dp));
                        contSelected = true;
                    }

                    /*
                    StringBuilder children = new StringBuilder();
                    for (int i = 0; i < chgNumbers.getChildCount(); i++) {
                        Chip chip = (Chip) chgNumbers.getChildAt(i);
                        children.append(chip.getText().toString());
                    }
                    Toast.makeText(getApplicationContext(), children.toString(), Toast.LENGTH_SHORT).show();
                    */
                }
            }
        }
    }

    private void addNewChip(final String number, final Drawable icon) {
        final Chip newChip = new Chip(getContext());
        newChip.setText(number);
        newChip.setCloseIconVisible(true);
        newChip.setChipIcon(icon);
        newChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numbers.remove(number);
                chgNumbers.removeView(view);
                //((RequestActivity) getActivity()).setContacts((String[]) numbers.toArray());
                Toast.makeText(getContext(), chgNumbers.getChildCount() + " " + numbers.size(), Toast.LENGTH_SHORT).show();
                contSelected = !numbers.isEmpty();
            }
        });
        chgNumbers.addView(newChip);
        Toast.makeText(getContext(), chgNumbers.getChildCount() + " " + numbers.size(), Toast.LENGTH_SHORT).show();
    }

    public Set<String> getNumbers() {
        return numbers;
    }

    public boolean isContSelected() {
        return contSelected;
    }
}
