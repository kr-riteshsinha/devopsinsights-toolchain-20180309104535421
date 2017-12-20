package com.ibm.smc;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import org.apache.commons.io.IOUtils;

import com.ibm.utility.WavEncoderStream;

@WebServlet("/sttListener")
@MultipartConfig
public class STTController extends HttpServlet{

	private static int increment = 0;
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		 DataInputStream in = 
	                new DataInputStream((InputStream)req.getInputStream());
	         
		 byte[] bytes = IOUtils.toByteArray(in);
	        String text = in.readUTF();

	        
	        AudioFormat inAudioFormat = new AudioFormat(16000, 16, 1, true, false);
			
	        
			AudioInputStream audioIn = new AudioInputStream(new ByteArrayInputStream(bytes), inAudioFormat, bytes.length);
			
			OutputStream encoder = new WavEncoderStream("c:\\audioStore\\speech_"+increment+++".wav", 16000, 2, 1);
			encoder.write(bytes);
			
	        
	        System.out.println(text);
	        String message;
	        try {
	            message = "100 ok";
	        } catch (Throwable t) {
	            message = "200 " + t.toString();
	        }
	        resp.setContentType("text/plain");
	        resp.setContentLength(message.length());
	        PrintWriter out = resp.getWriter();;
	        out.println(message);
	        in.close();
	        out.close();
	        out.flush();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
}
