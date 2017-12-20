package com.ibm.utility;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class WavEncoderStream extends OutputStream {

	private String outputFilePath;
	private RandomAccessFile randomAccess;

	private final static int FILE_SIZE_POSITION = 4;
	private final static int DATA_SIZE_POSITION = 40;
	
	private final static byte[] RIFF = {'R', 'I', 'F', 'F'};
	private final static int INITIAL_FILE_SIZE = 36; // Not count RIFF and size itself (8bytes)
	private final static byte[] WAV = {'W', 'A', 'V', 'E'};
	private final static byte[] FMT_CHUNK = {'f', 'm', 't', ' '};
	private final static int FMT_CHUNK_SIZE = 16; // Not count FMT and size itself (8bytes)
	private final static byte[] FMT_ID = {1, 0}; // Microsoft PCM format ID 
	private short numOfChannels;
	private int sampleRate;
	private int bytePerSec;
	private short blockSize;
	private short bitPerSample;
	private short bytePerSample;
	private final static byte[] DATA_CHUNK = {'d', 'a', 't', 'a'};
	private final static int INITIAL_DATA_SIZE = 0;

	public WavEncoderStream(String outputFilePath, int sampleRate, int bytePerSample, int numOfChannels) throws IOException
	{
		init(outputFilePath, sampleRate, bytePerSample, numOfChannels);
	}
	
	public void init(String outputFilePath, int sampleRate, int bytePerSample, int numOfChannels) throws IOException
	{
		this.outputFilePath = outputFilePath;
		this.sampleRate = sampleRate;
		this.bytePerSample = (short)bytePerSample;
		this.bitPerSample = (short) (bytePerSample * 8);
		this.bytePerSec = bytePerSample * sampleRate * numOfChannels;
		this.numOfChannels = (short)numOfChannels;
		this.blockSize = (short) (bytePerSample * numOfChannels);
		
		File outputFile = new File(outputFilePath);
		if (outputFile.exists())
		{
			outputFile.delete();
		}
		
		try
		{
			this.randomAccess = new RandomAccessFile(outputFilePath, "rw");
		}
		catch (IOException e)
		{
			System.err.println("Failed to open " + outputFilePath);
			e.printStackTrace();
			this.randomAccess = null;
			throw e;
		}
		
		try
		{
			randomAccess.seek(0);
			outputRiffHeader();
			outputFmtChunk();
			outputDataChunk();
		}
		catch (IOException e)
		{
			System.err.println("Failed to write initial data to " + outputFilePath);
			e.printStackTrace();
			close();
			
			throw e;
		}
	}
	
	private void outputRiffHeader() throws IOException
	{
		try
		{
			randomAccess.write(RIFF);
			randomAccess.write(intToLittleEndianBytes(INITIAL_FILE_SIZE));
			randomAccess.write(WAV);
		}
		catch (IOException e)
		{
			System.err.println("Failed to output a header to " + outputFilePath);
			e.printStackTrace();
			throw e;
		}
	}
	
	private void outputFmtChunk() throws IOException
	{
		try
		{
			randomAccess.write(FMT_CHUNK);
			randomAccess.write(intToLittleEndianBytes(FMT_CHUNK_SIZE));
			randomAccess.write(FMT_ID);
			randomAccess.write(shortToLittenEndianBytes(numOfChannels));
			randomAccess.write(intToLittleEndianBytes(sampleRate));
			randomAccess.write(intToLittleEndianBytes(bytePerSec));
			randomAccess.write(shortToLittenEndianBytes(blockSize));
			randomAccess.write(shortToLittenEndianBytes(bitPerSample));
		}
		catch (IOException e)
		{
			System.err.println("Failed to output a fmt chunk to " + outputFilePath);
			e.printStackTrace();
			throw e;
		}
	}
	
	private void outputDataChunk() throws IOException
	{
		try
		{
			randomAccess.write(DATA_CHUNK);
			randomAccess.write(intToLittleEndianBytes(INITIAL_DATA_SIZE));
		}
		catch (IOException e)
		{
			System.err.println("Failed to output a data chunk to " + outputFilePath);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void outputData(byte[] data) throws IOException
	{
		if (randomAccess == null)
		{
			return;
		}
		
		try
		{
			randomAccess.seek(randomAccess.length());
			randomAccess.write(data);
			
			updateFileAndDataSize();
		}
		catch (IOException e)
		{
			System.err.println("Failed to output data to " + outputFilePath);
			e.printStackTrace();
			throw e;
		}
	}
	
	private void updateFileAndDataSize() throws IOException
	{
		int fileSize = (int) (randomAccess.length() - 8);
		randomAccess.seek(FILE_SIZE_POSITION);
		randomAccess.write(intToLittleEndianBytes(fileSize));
		
		int dataSize = (int) (randomAccess.length() - 44);
		randomAccess.seek(DATA_SIZE_POSITION);
		randomAccess.write(intToLittleEndianBytes(dataSize));
	}
	
	private byte[] intToLittleEndianBytes(int v)
	{
	    return new byte[] {
	            (byte)(v),
	            (byte)(v >>> 8),
	            (byte)(v >>> 16),
	            (byte)(v >>> 24)
	            };
	}
	
	private byte[] shortToLittenEndianBytes(short v)
	{
	    return new byte[] {
	            (byte)(v),
	            (byte)(v >>> 8)
	            };
	}

	@Override
	public void write(byte[] bytes) throws IOException
	{
		outputData(bytes);
	}
	
	@Override
	public void write(int b) throws IOException 
	{
		byte[] byte_array = new byte[1];
		byte_array[0] = (byte)b;
		write(byte_array);
	}
	
	@Override
	public void close() throws IOException
	{
		if (randomAccess == null)
		{
			return;
		}
		
		randomAccess.close();
	}
	
	@Override
	public void flush() throws IOException
	{
		// Nothing to do
	}
}
