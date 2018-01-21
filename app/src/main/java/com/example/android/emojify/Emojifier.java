package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Gormandizer on 1/19/2018.
 */

public class Emojifier {

    public static void detectFaces(Context context, final Bitmap resampledBitmap) {

        // Create the detector
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // Create the frame
        Frame frame = new Frame.Builder().setBitmap(resampledBitmap).build();

        // Get the array of faces from the frame, using the detector
        SparseArray<Face> faces = detector.detect(frame);

        String facesFound;
        if (faces.size() == 0){ facesFound = "No faces detected"; }
        facesFound = String.format("Found %s faces", faces.size());

        Toast.makeText(context, facesFound, Toast
                .LENGTH_SHORT).show();

        for(int i = 0; i < faces.size(); i++) {
            Face face = faces.valueAt(i);
            getClassifications(face);
        }

        // Release the detector
        detector.release();
    }

    private static HashMap<String,Float> getClassifications(Face face) {
        HashMap<String,Float> probabilities = new HashMap<>();

       // Calculate the probabiliby of each eye being open, and of a smile
        for (Landmark landmark : face.getLandmarks()) {
            switch (landmark.getType()) {
                case Landmark.LEFT_EYE:
                    probabilities.put(String.valueOf(Landmark.LEFT_EYE),
                            face.getIsLeftEyeOpenProbability());
                    break;
                case Landmark.RIGHT_EYE:
                    probabilities.put(String.valueOf(Landmark.RIGHT_EYE),
                            face.getIsRightEyeOpenProbability());
                    break;
                case Landmark.RIGHT_MOUTH:
                case Landmark.BOTTOM_MOUTH:
                case Landmark.LEFT_MOUTH:
                    probabilities.put("smilingProb",
                            face.getIsSmilingProbability());
                    break;
                default:
                    probabilities.clear();
            }
        }

        return probabilities;
    }
}
