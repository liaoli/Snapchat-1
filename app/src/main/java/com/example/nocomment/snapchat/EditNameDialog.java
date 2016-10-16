package com.example.nocomment.snapchat;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by guomingsun on 17/10/16.
 */

public class EditNameDialog extends DialogFragment {

    private EditText editNameField;
    private Button save;
    private TextView cancel;

    private Context context;
    private DialogCallback dialogCallback;

    public interface DialogCallback {
        public void onValuesSet(String name);
    }

    public EditNameDialog(Context context) {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.editname_dialog, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        editNameField = (EditText) convertView.findViewById(R.id.editNameField);
        save = (Button) convertView.findViewById(R.id.saveNameBtn);
        cancel = (TextView) convertView.findViewById(R.id.closeEditDialog);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editNameField.getText().toString();
                if(TextUtils.isEmpty(newName)){
                    return;
                } else {
                    dialogCallback.onValuesSet(newName);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });



        return convertView;
    }
}
