/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leslie.socialink.secondma.android;

import static com.leslie.socialink.secondma.decode.DecodeHandler.DECODE;
import static com.leslie.socialink.secondma.decode.DecodeHandler.DECODE_FAILED;
import static com.leslie.socialink.secondma.decode.DecodeHandler.DECODE_SUCCEEDED;
import static com.leslie.socialink.secondma.decode.DecodeHandler.QUIT;
import static com.leslie.socialink.secondma.decode.DecodeHandler.RESTART_PREVIEW;
import static com.leslie.socialink.secondma.decode.DecodeHandler.RETURN_SCAN_RESULT;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.Result;
import com.leslie.socialink.secondma.camera.CameraManager;
import com.leslie.socialink.secondma.decode.DecodeThread;
import com.leslie.socialink.secondma.view.ViewfinderResultPointCallback;


/**
 * This class handles all the messaging which comprises the state machine for
 * capture. 该类用于处理有关拍摄状态的所有信息
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CaptureActivityHandler extends Handler {

    private static final String TAG = CaptureActivityHandler.class
            .getSimpleName();

    private final CaptureActivity activity;
    private final DecodeThread decodeThread;
    private State state;
    private final CameraManager cameraManager;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler(CaptureActivity activity, CameraManager cameraManager) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity, new ViewfinderResultPointCallback(
                activity.getViewfinderView()));
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        // 开始拍摄预览和解码
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case RESTART_PREVIEW:
                // 重新预览

                restartPreviewAndDecode();
                break;
            case DECODE_SUCCEEDED:
                // 解码成功

                state = State.SUCCESS;
                activity.handleDecode((Result) message.obj);

                break;
            case DECODE_FAILED:

                // 尽可能快的解码，以便可以在解码失败时，开始另一次解码

                state = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(),
                        DECODE);
                break;
            case RETURN_SCAN_RESULT:

                activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
                activity.finish();
                break;
        }
    }

    /**
     * 完全退出
     */
    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), QUIT);
        quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause()
            // will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        //确保不会发送任何队列消息
        removeMessages(DECODE_SUCCEEDED);
        removeMessages(DECODE_FAILED);
    }

    public void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), DECODE);
            activity.drawViewfinder();
        }
    }

}
