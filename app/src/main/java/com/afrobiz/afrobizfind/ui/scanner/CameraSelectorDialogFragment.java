package com.afrobiz.afrobizfind.ui.scanner;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

import com.afrobiz.afrobizfind.R;

public class CameraSelectorDialogFragment extends DialogFragment {
    public interface CameraSelectorDialogListener {
        public void onCameraSelected(int cameraId);
    }
    int checkedIndex = 0;
    private int mCameraId;
    private CameraSelectorDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static CameraSelectorDialogFragment newInstance(CameraSelectorDialogListener listener, int cameraId) {
        CameraSelectorDialogFragment fragment = new CameraSelectorDialogFragment();
        fragment.mCameraId = cameraId;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if(mListener == null) {
            dismiss();
            return null;
        }

        int numberOfCameras = Camera.getNumberOfCameras();
        String[] cameraNames = new String[numberOfCameras];


        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraNames[i] = "Front Facing";
            } else if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraNames[i] = "Rear Facing";
            } else {
                cameraNames[i] = "Camera ID: " + i;
            }
            if(i == mCameraId) {
                checkedIndex = i;
            }
        }
        Dialog d_camera = new Dialog(getActivity());
        d_camera.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_camera.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        d_camera.setContentView(R.layout.dialog_camera_number);
        LinearLayout layout_front = (LinearLayout) d_camera.findViewById(R.id.layout_front);
        LinearLayout layout_rear = (LinearLayout) d_camera.findViewById(R.id.layout_rear);
        ImageView img_front = (ImageView) d_camera.findViewById(R.id.img_front);
        ImageView img_rear = (ImageView) d_camera.findViewById(R.id.img_rear);
        ImageView img_cancel = (ImageView) d_camera.findViewById(R.id.img_cancel);

        if(checkedIndex == 0)
        {
            img_rear.setImageResource(R.drawable.check);
            img_front.setImageResource(R.drawable.uncheck);
        }
        else
        {
            img_front.setImageResource(R.drawable.check);
            img_rear.setImageResource(R.drawable.uncheck);
        }
        img_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCameraId = checkedIndex;
                if (mListener != null) {
                    mListener.onCameraSelected(mCameraId);
                }
                d_camera.dismiss();
            }
        });

        layout_front.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCameraId = 1;
                img_front.setImageResource(R.drawable.check);
                img_rear.setImageResource(R.drawable.uncheck);
                if (mListener != null) {
                    mListener.onCameraSelected(mCameraId);
                }
                d_camera.dismiss();
            }
        });
        layout_rear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCameraId = 0;
                img_rear.setImageResource(R.drawable.check);
                img_front.setImageResource(R.drawable.uncheck);
                if (mListener != null) {
                    mListener.onCameraSelected(mCameraId);
                }
                d_camera.dismiss();
            }
        });
        d_camera.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Set the dialog title
//        builder.setTitle(R.string.select_camera)
//                // Specify the list array, the items to be selected by default (null for none),
//                // and the listener through which to receive callbacks when items are selected
//                .setSingleChoiceItems(cameraNames, checkedIndex,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                mCameraId = which;
//                            }
//                        })
//                // Set the action buttons
//                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User clicked OK, so save the mSelectedIndices results somewhere
//                        // or return them to the component that opened the dialog
//                        if (mListener != null) {
//                            mListener.onCameraSelected(mCameraId);
//                        }
//                    }
//                })
//                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                    }
//                });
//
//        return builder.create();
        return d_camera;
    }
}
