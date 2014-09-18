/*
 * By, Agilution Technologies
 * 
 * Dev : Ajay Patel
 * Contact No : 8000710064
 * Email-Id : patelajay2012@gmail.com
 * Date : 09/02/2014 12:10 AM
 * Class Name : MainActivity.java
 * 
 */

package com.ap.cropper.cropwindow.handle;

import android.graphics.Rect;

import com.ap.cropper.cropwindow.edge.Edge;
import com.ap.cropper.cropwindow.edge.EdgePair;

/**
 * HandleHelper class to handle corner Handles (i.e. top-left, top-right,
 * bottom-left, and bottom-right handles).
 */
class CornerHandleHelper extends HandleHelper {

    // Constructor /////////////////////////////////////////////////////////////

    CornerHandleHelper(Edge horizontalEdge, Edge verticalEdge) {
        super(horizontalEdge, verticalEdge);
    }

    // HandleHelper Methods ////////////////////////////////////////////////////

    @Override
    void updateCropWindow(float x,
                          float y,
                          float targetAspectRatio,
                          Rect imageRect,
                          float snapRadius) {

        final EdgePair activeEdges = getActiveEdges(x, y, targetAspectRatio);
        final Edge primaryEdge = activeEdges.primary;
        final Edge secondaryEdge = activeEdges.secondary;

        primaryEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);
        secondaryEdge.adjustCoordinate(targetAspectRatio);

        if (secondaryEdge.isOutsideMargin(imageRect, snapRadius)) {
            secondaryEdge.snapToRect(imageRect);
            primaryEdge.adjustCoordinate(targetAspectRatio);
        }
    }
}
