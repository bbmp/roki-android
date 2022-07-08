package com.robam.roki.utils.audio;

import com.robam.roki.utils.audio.encoder.EncoderCallback;

public interface RokiAudioEncoder {
    public void addEncoderCallback(EncoderCallback callback);
    public void encode(String inputPath, String outputPath);
}
