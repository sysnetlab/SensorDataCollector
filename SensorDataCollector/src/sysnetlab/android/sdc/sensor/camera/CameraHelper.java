/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package sysnetlab.android.sdc.sensor.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.util.Log;

public class CameraHelper {
    CameraSensor[] cameras;

    @SuppressLint("NewApi")
    CameraHelper(Context ctx) {
        int nCameras;
        if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                nCameras = Camera.getNumberOfCameras();
            } else {
                nCameras = 1;
            }
            cameras = new CameraSensor[nCameras];
            for (int i = 0; i < nCameras; i++) {
                final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    cameras[i].setName("Camera " + i + " - " + "Facing Back");
                } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    cameras[i].setName("Camera " + i + " - " + "Facing Front");
                } else {
                    cameras[i].setName("Camera " + i + " - " + "Facing Unknown");
                    Log.e("SensorDataCollector", "Unexpected camera facing.");
                }
            }
        } else {
            cameras = null;
        }
    }

    @SuppressLint("NewApi")
    void runCamera(int cameraId) {
        Camera camera;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            camera = Camera.open(cameraId);
            camera.takePicture(null, null, new PictureHandler());
            camera.release();
        } else { // support one camera

        }
    }

    private class PictureHandler implements PictureCallback {

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub

        }
    }
}
