package com.ibm.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.tfs.service.model.TFSDataModel;

public class EncryptionUtility {

	private static final Logger logger = LoggerFactory.getLogger(EncryptionUtility.class.getName());
	private static final byte[] INITIAL_IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	// Appender console = RollingFileAppender.Builder<Builder<B>>;

	// static variable single_instance of type Singleton
	private static EncryptionUtility single_instance = null;

	/**
	 * The constant that denotes the algorithm being used.
	 */
	private SecureRandom r = new SecureRandom();
	private Cipher c;
	private IvParameterSpec IV;
	private Key key;
	private String keystorePass = "mystorepass";
	private String alias = "aesalias";
	private String keyPass = "mykeypass";

	/**
	 * The private constructor to prevent instantiation of this object.
	 * 
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 */
	private EncryptionUtility() throws NoSuchAlgorithmException, NoSuchPaddingException, KeyStoreException,
			CertificateException, IOException, UnrecoverableKeyException {
		this.c = Cipher.getInstance("AES/CBC/PKCS5PADDING");

		ClassLoader classLoader = getClass().getClassLoader();
		InputStream keystoreStream = classLoader.getResourceAsStream("locale/aes-keystore.jck");

		KeyStore keystore = KeyStore.getInstance("JCEKS");
		keystore.load(keystoreStream, keystorePass.toCharArray());

		if (!keystore.containsAlias(alias)) {
			throw new RuntimeException("Alias for key not found");
		}

		this.key = keystore.getKey(alias, keyPass.toCharArray());
		this.IV = generateIV();

	}

	// static method to create instance of Singleton class
	public static EncryptionUtility getInstance() throws UnrecoverableKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, KeyStoreException, CertificateException, IOException {
		if (single_instance == null)
			single_instance = new EncryptionUtility();

		return single_instance;
	}

	// Create the IV.Create a Secure Random Number Generator and an empty 16byte
	// array. Fill the array.Returns IV

	private IvParameterSpec generateIV() {

//		byte[] newSeed = r.generateSeed(16);
//		r.setSeed(newSeed);
//		byte[] byteIV = new byte[16];
//		r.nextBytes(byteIV);
		String str = "Timepass";
		IV = new IvParameterSpec(INITIAL_IV);

		return IV;
	}

	public SealedObject encrypt(byte[] tfsdata) throws InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException {

		this.c.init(Cipher.ENCRYPT_MODE, this.key, this.IV, this.r);
		// Encrypt packet
		SealedObject so = new SealedObject(tfsdata, this.c);
		return so;

	}

	// Initialize the cipher in DECRYPT_MODE Decrypt and store as byte[] Convert
	// to plainText and return
	public TFSDataModel decrypt(SealedObject sealedObject)
			throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, ClassNotFoundException, IOException {

		this.c.init(Cipher.DECRYPT_MODE, this.key, this.IV);

		byte[] decryptedModel = (byte[]) sealedObject.getObject(this.c);
		TFSDataModel tfsData = (TFSDataModel) ObjectConverter.deserialize(decryptedModel);

		return tfsData;

	}

	public static void main(String[] args) throws InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

		// call the encrypt method to encrypt the TFSDatamodel
		// the bytes you want to encrypt
		logger.info("TFS Orchestration Service - POST - begin");

	//	fileLogger.info("TFS Orchestration Service - POST - begin 11111");
		String agentId = "agent01";
		String channelId = "channel01";
		String hostName = "host01";
		String sttResponse = "This is stt response";
		String wcsResponse = "This is wcs response";
		String wdsResponse = "This is wds response";
		String wcsRequest = "This is wcs request";
		String wdsRequest = "This is wds request";
		EncryptionUtility enc = null;
		TFSDataModel tfsModel = null;
		TFSDataModel tfsDecryptedModel = null;
		try {
			enc = new EncryptionUtility();

			// File sampleAudioFile = new File(
			// "C:\\Workspaces\\CBDS\\Projects\\TFS\\DevTool\\TFS_Sample_Call
			// Recordings-2 hrs\\Angela O No
			// PII_1_6475744010258424712_1_30.wav");

			// FileInputStream in = new FileInputStream(sampleAudioFile);
			// BufferedInputStream bis = new BufferedInputStream(in);
			// byte[] sampleBytes = new byte[bis.available()];
			// bis.close();

			// byte[] sampleBytes =
			// ResultQueueProcessorTest.serializeAudioFile();

			// Map<Object,Object> map1 = new Map<Object, Object>();
			tfsModel = new TFSDataModel(agentId, channelId, null, sttResponse, wcsResponse, wdsResponse,
					wcsRequest, wdsRequest, null, null);
			System.out.println("Original TFSDataModel to be encrypted : " + tfsModel);

			// Encrypt the TFSDataModel
			SealedObject encryptedModel = enc.encrypt(ObjectConverter.serialize(tfsModel));
			System.out.println("Encrypted DataModel : " + encryptedModel);

			// Convert and Send Encrypted Message object to Orchestration
			// service
			byte[] tfsSealedObjectBytes = ObjectConverter.serialize(encryptedModel);
			
				//enc = null;
				enc = new EncryptionUtility();
			SealedObject encryptedSealedObj = (SealedObject) ObjectConverter.deserialize(tfsSealedObjectBytes);

			// Decrypt packet
			tfsDecryptedModel = enc.decrypt(encryptedSealedObj);
			System.out.println("Decrypted DataModel : " + tfsDecryptedModel);

		} catch (Exception e) {
			logger.error("Error while encrypting : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
