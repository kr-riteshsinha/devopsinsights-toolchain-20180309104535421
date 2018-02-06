package com.ibm.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import com.sun.media.sound.WaveFileWriter;

public class SpeechAudioFileWriter {
	private static int increment =0;

	public static void main(String arg[]) {

		WaveFileWriter  writer = new WaveFileWriter();
		//writer.write(arg0, arg1, arg2)
	}
	
	public static void writeTofile(byte[] audioStream) throws IOException {
	
		InputStream byteArrayInputStream = new ByteArrayInputStream(audioStream);
		//FileOutputStream out = new FileOutputStream(File.createTempFile("sppech", ".wav"));
		
		AudioFormat inAudioFormat = new AudioFormat(16000, 16, 1, true, false);
		
		AudioInputStream audioIn = new AudioInputStream(new ByteArrayInputStream(audioStream), inAudioFormat, audioStream.length);
		
		OutputStream encoder = new WavEncoderStream("c:\\audioStore\\speech.wav", 16000, 2, 1);
		encoder.write(audioStream);
		
	}
}
