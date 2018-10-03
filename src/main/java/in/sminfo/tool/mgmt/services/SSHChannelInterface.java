package in.sminfo.tool.mgmt.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import in.sminfo.tool.mgmt.entity.KeyPair;
import in.sminfo.tool.mgmt.entity.SshKeys;
import in.sminfo.tool.mgmt.entity.UserEntity;
import in.sminfo.tool.mgmt.repository.SshUserRepo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SSHChannelInterface {
	private static final String CLASS_NAME = "SSHChannelInterface : ";
	@Resource
	SshUserRepo sshUserRepo;

	@Resource
	VaultService vaultService;
	private final Integer PORT = 22;

	public Session getSession(String host, KeyPair keyPair) {
		try {

			String path = System.getProperty("java.io.tmpdir");
			File keyPairTmpFile = new File(path + "/" + keyPair.getKeypairName());
			keyPairTmpFile.createNewFile();
			FileWriter writer = new FileWriter(keyPairTmpFile);
			writer.write(vaultService.readAwsAccountKeysPair(keyPair.getAwsAccountId(), keyPair.getKeypairName()));
			writer.close();

			JSch jsch = new JSch();
			String user = keyPair.getUserName();

			log.debug(CLASS_NAME + keyPairTmpFile.getAbsolutePath());
			jsch.addIdentity(keyPairTmpFile.getAbsolutePath());
			log.debug(CLASS_NAME + "Identity file added in JSch Command.");

			Session session = jsch.getSession(user, host, PORT);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			session.setTimeout(60000);
			log.debug(CLASS_NAME + "session connected.... With IP - " + host);

			return session;
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(CLASS_NAME + "exception in execRedemption ");
			return null;
		}
	}

	public Boolean scpFile(String host, KeyPair keyPair, SshKeys sshUser) throws IOException, JSchException {
		String path = System.getProperty("java.io.tmpdir");
		File sshUserFile = new File(path + "/" + sshUser.getName());
		sshUserFile.createNewFile();
		FileWriter writer = new FileWriter(sshUserFile);
		writer.write(sshUser.getSshKey());
		writer.close();
		log.debug(CLASS_NAME + "User key file Path - " + sshUserFile.getAbsolutePath());
		String command = "scp -p  -t ~/" + sshUserFile.getName();
		log.debug(CLASS_NAME + "Command - " + command);
		Channel channel = getSession(host, keyPair).openChannel("exec");
		ChannelExec ce = ((ChannelExec) channel);
		ce.setCommand(command);
		// get I/O streams for remote scp
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();
		String fileName = sshUserFile.getAbsolutePath();
		channel.connect();
		long filesize = sshUserFile.length();
		command = "C0644 " + filesize + " ";
		if (sshUserFile.getAbsolutePath().lastIndexOf('/') > 0) {
			command += fileName.substring(fileName.lastIndexOf('/') + 1);
		} else {
			command += fileName;
		}
		command += "\n";
		log.debug(CLASS_NAME + "SSH Command :" + command);
		out.write(command.getBytes());
		out.flush();
		if (checkAck(in) != 0) {
			System.exit(0);
		}
		// send a content of lfile
		FileInputStream fis = new FileInputStream(sshUserFile.getAbsolutePath());
		byte[] buf = new byte[1024];
		while (true) {
			int len = fis.read(buf, 0, buf.length);
			if (len <= 0)
				break;
			out.write(buf, 0, len); // out.flush();
		}
		fis.close();
		fis = null;
		// send '\0'
		buf[0] = 0;
		out.write(buf, 0, 1);
		out.flush();
		if (checkAck(in) != 0) {
			System.exit(0);
		}
		out.close();
		log.debug(CLASS_NAME + "scp done");
		channel.disconnect();
		return true;
	}

	public Boolean scpFile(String host, KeyPair keyPair, Integer userId, UserEntity user)
			throws IOException, JSchException {
		SshKeys sshUser = sshUserRepo.findByUserAndIsActive(user, true);
		return scpFile(host, keyPair, sshUser);

	}

	public String connectAndExecute(Session session, String command1) {
		String CommandOutput = null;
		try {

			log.debug(CLASS_NAME + "Connected");
			log.debug(CLASS_NAME + "Command " + command1);
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command1);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);

					if (i < 0)
						break;
					log.debug(CLASS_NAME + new String(tmp, 0, i));
					CommandOutput = new String(tmp, 0, i);
				}

				if (channel.isClosed()) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			int exitstatus = channel.getExitStatus();
			channel.disconnect();
			log.debug(CLASS_NAME + "Exit Status :" + exitstatus);
			log.debug(CLASS_NAME + "Output : " + CommandOutput);
			return String.valueOf(exitstatus);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				log.debug(CLASS_NAME + sb.toString());
			}
			if (b == 2) { // fatal error
				log.debug(CLASS_NAME + sb.toString());
			}
		}
		return b;
	}

}
