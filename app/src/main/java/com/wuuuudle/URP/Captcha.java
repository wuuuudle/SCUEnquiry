package com.wuuuudle.URP;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.util.ArrayMap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Captcha implements AutoCloseable {
    private Interpreter interpreter = null;

    public Captcha(Context context) throws IOException {
        interpreter = new Interpreter(loadModelFile(context));
    }

    @Override
    public void close() {
        interpreter.close();
    }

    public String OCR(Bitmap bitmap) {
        byte[] rgb = bitmap2RGB(bitmap);
        float[][][][][] imageData = new float[1][1][60][180][3];
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 180; j++) {
                for (int k = 0; k < 3; k++)
                    imageData[0][0][i][j][k] = (float) ((rgb[3 * (i * 180 + j) + k] & 0xff) / 255.0);
            }
        }
        float[][][] out = new float[4][1][36];
        Map<Integer, Object> outputs = new ArrayMap<>();
        outputs.put(0, out[0]);
        outputs.put(1, out[1]);
        outputs.put(2, out[2]);
        outputs.put(3, out[3]);
        interpreter.runForMultipleInputsOutputs(imageData, outputs);
        return Arrays.stream(out).map(this::decode).collect(Collectors.joining());
    }

    private String decode(float[][] out) {
        int max = 0;
        for (int i = 1; i < out[0].length; i++)
            if (out[0][i] > out[0][max]) max = i;
        String characters = "0123456789abcdefghijklmnopqrstuvwxyz";
        return String.valueOf(characters.charAt(max));
    }

    private static byte[] bitmap2RGB(Bitmap bitmap) {
        int bytes = bitmap.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer);
        byte[] rgba = buffer.array();
        byte[] pixels = new byte[(rgba.length / 4) * 3];
        int count = rgba.length / 4;
        for (int i = 0; i < count; i++) {
            pixels[i * 3] = rgba[i * 4];        //R
            pixels[i * 3 + 1] = rgba[i * 4 + 1];    //G
            pixels[i * 3 + 2] = rgba[i * 4 + 2];       //B

        }
        return pixels;
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("captcha_cnn.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
