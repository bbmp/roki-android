package com.robam.roki.utils.audio;


import com.robam.roki.utils.audio.encoder.EncoderCallback;

/**
 * Created by kermit on 16/7/13.
 */

public interface SimpleAudioEncoder {

    void addEncoderCallback(EncoderCallback callback);

    void encode(String inputPath, String outputPath);

}
