package com.example.smartageliketool.data.util;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.smartageliketool.R;
import com.example.smartageliketool.view.main.MainActivity;

import java.io.IOException;

import retrofit2.HttpException;

public class ErrorHandling {
    private Throwable error;
    private Context context;

    public ErrorHandling(Throwable error, Context context) {
        this.error = error;
        this.context = context;
    }

    public int getErrorCode() {
        if (error instanceof HttpException)
            return ((HttpException) error).code();
        else if (error instanceof IOException)
            return 1000;
        else return 0;
    }

    private void ToastMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private void networkPopUp() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        ViewGroup viewGroup1 = ((AppCompatActivity) context).findViewById(android.R.id.content);
        View dialogView1 = LayoutInflater.from(context).inflate(R.layout.single_button_dialog, viewGroup1, false);
        builder1.setView(dialogView1);
        final AlertDialog alertDialog1 = builder1.create();

        alertDialog1.setCancelable(false);

        TextView message1 = dialogView1.findViewById(R.id.txt_message_single_button_dialog);
        Button yes1 = dialogView1.findViewById(R.id.btn_single_button_dialog);

        yes1.setText("Retry");

        message1.setText("Make sure internet is available !");

        yes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.cancel();
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });

        alertDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog1.show();
    }

    public void showErrorToast() {
        switch (getErrorCode()) {
            case 705:
                ToastMessage("لینک پیدا نشد");
                break;

            default:
                System.out.println("Status is " + getErrorCode());
                ToastMessage("خطای نامعلوم");
                break;
        }
    }
}
