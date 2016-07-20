/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.strandgenomics.imaging.iclient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * loads certificate dynamically
 * 
 * @author Anup Kulkarni
 *
 */
public class LoadCert {
	
	private static String passphrase = "changeit";
	
	public LoadCert()
	{}
	
	/**
	 * 
	 * @param host name of the host
	 * @param port specified https port
	 * @throws KeyStoreException 
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static String loadCert(String host, int port,String proxyHost,int proxyPort,String username,String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException
	{
		KeyStore ks = getKeyStore();
		
		SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory factory = context.getSocketFactory();
        SSLSocket socket = null;
        if(proxyHost!=null){
        	String tunnelHost = proxyHost;
            int tunnelPort = proxyPort;

            Socket tunnel = new Socket(tunnelHost, tunnelPort);
            doTunnelHandshake(tunnel, host, port,username,password);   // tunnel through proxy server

            socket =
            		(SSLSocket)factory.createSocket(tunnel, host, port, true);

            socket.addHandshakeCompletedListener(
            		new HandshakeCompletedListener() {
            			public void handshakeCompleted(
            					HandshakeCompletedEvent event) {
            				System.out.println("Handshake finished!");
            				System.out.println(
            						"\t CipherSuite:" + event.getCipherSuite());
            				System.out.println(
            						"\t SessionId " + event.getSession());
            				System.out.println(
            						"\t PeerHost " + event.getSession().getPeerHost());
            			}
            		}
            		);
        }
        else{
        	socket = (SSLSocket) factory.createSocket(host, port);
            
        }
        
		socket.setSoTimeout(10000);
        try {
            socket.startHandshake();
            socket.close();
        } catch (SSLException e) {
        }

        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            return "";
        }

        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            sha1.update(cert.getEncoded());
            md5.update(cert.getEncoded());
        }

        //System.out.println("Enter certificate to add to trusted keystore or 'q' to quit: [1]");
        int k = 0;

        X509Certificate cert = chain[k];
        String alias = host + "-" + (k + 1);
        ks.setCertificateEntry(alias, cert);

        File certFile = new File("jssecacerts");
        
        OutputStream out = new FileOutputStream(certFile);
        ks.store(out, passphrase.toCharArray());
        out.close();
        
        return certFile.getAbsolutePath();
	}
	
	private static KeyStore getKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
	{
		char SEP = File.separatorChar;
		File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
		File file = new File(dir, "jssecacerts");
		if (file.isFile() == false)
		{
			file = new File(dir, "cacerts");
		}
        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, "changeit".toCharArray());
        in.close();
        
        return ks;
	}
	private static void doTunnelHandshake(Socket tunnel, String host, int port,String username, String password)
			throws IOException
			{
		OutputStream out = tunnel.getOutputStream();
		String msg = null;
		if(username!=null && password != null){
			String proxyUser = username;
			String proxyPassword = password;


			String proxyCredentials = proxyUser+":"+proxyPassword;
			String encproxyCredentials = new sun.misc.BASE64Encoder().encode(proxyCredentials.getBytes());
			msg = "CONNECT " + host + ":" + port + " HTTP/1.1\n"
					+ "User-Agent: "
					+ sun.net.www.protocol.http.HttpURLConnection.userAgent+ "\n";
			if(proxyUser != null && proxyUser.length() > 0 && proxyPassword != null && proxyPassword.length() > 0){
				msg+="Proxy-Authorization: Basic "+encproxyCredentials;
			}
			msg+="\r\n\r\n";
		}
		else{
			msg = "CONNECT " + host + ":" + port + " HTTP/1.0\n"
					+ "User-Agent: "
					+ sun.net.www.protocol.http.HttpURLConnection.userAgent
					+ "\r\n\r\n";
		}

		byte b[];
		try {
			/*
			 * We really do want ASCII7 -- the http protocol doesn't change
			 * with locale.
			 */
			b = msg.getBytes("ASCII7");
		} catch (UnsupportedEncodingException ignored) {
			/*
			 * If ASCII7 isn't there, something serious is wrong, but
			 * Paranoia Is Good (tm)
			 */
			b = msg.getBytes();
		}
		out.write(b);
		out.flush();

		/*
		 * We need to store the reply so we can create a detailed
		 * error message to the user.
		 */
		byte            reply[] = new byte[200];
		int             replyLen = 0;
		int             newlinesSeen = 0;
		boolean         headerDone = false;     /* Done on first newline */

		InputStream     in = tunnel.getInputStream();
		boolean         error = false;

		while (newlinesSeen < 2) {
			int i = in.read();
			if (i < 0) {
				throw new IOException("Unexpected EOF from proxy");
			}
			if (i == '\n') {
				headerDone = true;
				++newlinesSeen;
			} else if (i != '\r') {
				newlinesSeen = 0;
				if (!headerDone && replyLen < reply.length) {
					reply[replyLen++] = (byte) i;
				}
			}
		}

		/*
		 * Converting the byte array to a string is slightly wasteful
		 * in the case where the connection was successful, but it's
		 * insignificant compared to the network overhead.
		 */
		String replyStr;
		try {
			replyStr = new String(reply, 0, replyLen, "ASCII7");
		} catch (UnsupportedEncodingException ignored) {
			replyStr = new String(reply, 0, replyLen);
		}
		System.out.println(replyStr);
		/* We asked for HTTP/1.0, so we should get that back */
		//			       if (!replyStr.startsWith("HTTP/1.1 200")) {
		//			           throw new IOException("Unable to tunnel through "
		//			                   + tunnelHost + ":" + tunnelPort
		//			                   + ".  Proxy returns \"" + replyStr + "\"");
		//			       }

		/* tunneling Handshake was successful! */
			}
	private static class SavingTrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        public X509Certificate[] getAcceptedIssuers() {
            throw new UnsupportedOperationException();
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }
}
