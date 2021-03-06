package com.fyl.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.CRC32;

public class CrcUtils {
	public static long checksumInputStream(String filepath) throws IOException {
		InputStream inputStreamn = new FileInputStream(filepath);
		CRC32 crc = new CRC32();

		int cnt;

		byte[] buffer = new byte[5120];
		
		while ((cnt = inputStreamn.read(buffer)) != -1) {
			crc.update(buffer, 0, cnt);
		}
		inputStreamn.close();
		return crc.getValue();
	}

	public static int checksumBufferedInputStream(String filepath)
			throws IOException {
		InputStream inputStream = new BufferedInputStream(new FileInputStream(
				filepath));

		CRC32 crc = new CRC32();

		int cnt;
		byte[] buffer = new byte[5120];
		
		while ((cnt = inputStream.read(buffer)) != -1) {
			crc.update(buffer, 0, cnt);
		}
		inputStream.close();
		return (int)crc.getValue();
	}
	

	public static long checksumRandomAccessFile(String filepath)
			throws IOException {
		RandomAccessFile randAccfile = new RandomAccessFile(filepath, "r");
		long length = randAccfile.length();
		CRC32 crc = new CRC32();

		for (long i = 0; i < length; i++) {
			randAccfile.seek(i);
			int cnt = randAccfile.readByte();
			crc.update(cnt);
		}
		randAccfile.close();
		return crc.getValue();
	}

	public static long checksumMappedFile(String filepath) throws IOException {
		FileInputStream inputStream = new FileInputStream(filepath);
		FileChannel fileChannel = inputStream.getChannel();

		int len = (int) fileChannel.size();

		MappedByteBuffer buffer = fileChannel.map(
				FileChannel.MapMode.READ_ONLY, 0, len);

		CRC32 crc = new CRC32();

		for (int cnt = 0; cnt < len; cnt++) {

			int i = buffer.get(cnt);
			crc.update(i);
		}
		inputStream.close();
		return crc.getValue();
	}
	
	public static long checksumbuf(byte[] buf) {
		CRC32 crc = new CRC32();
		crc.update(buf);
		return crc.getValue();
	}
}
