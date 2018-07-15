package sonic.sync.core.security;

import java.io.File;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import sonic.sync.core.util.*;
import sonic.sync.core.file.FileManager;
import sonic.sync.core.file.FileTreeNode;
import sonic.sync.core.network.data.NetworkContent;


public class UserProfile extends NetworkContent {


	private static final long serialVersionUID = 1L;

	private final String userId;
	private final KeyPair encryptionKeys;
	private transient final FileTreeNode root;

	public UserProfile(String userId) {
		this(userId, EncryptionUtil.generateRSAKeyPair(Constants.KEYLENGTH_USER_KEYS), EncryptionUtil.generateProtectionKey());
	}
	public UserProfile(String userId, KeyPair encryptionKeys, KeyPair protectionKeys) {
		if (userId == null)
			throw new IllegalArgumentException("User id can't be null.");
		if (encryptionKeys == null)
			throw new IllegalArgumentException("Encryption keys can't be null.");
		if (protectionKeys == null)
			throw new IllegalArgumentException("Protection keys can't be null.");
		this.userId = userId;
		this.encryptionKeys = encryptionKeys;
		root = new FileTreeNode(encryptionKeys, protectionKeys);
	}

	public String getUserId() {
		return userId;
	}

	public KeyPair getEncryptionKeys() {
		return encryptionKeys;
	}

	public KeyPair getProtectionKeys() {
		return root.getProtectionKeys();
	}

	public FileTreeNode getRoot() {
		return root;
	}
	protected int getContentHash() {
		return userId.hashCode() + 21 * encryptionKeys.hashCode();
	}

	public FileTreeNode getFileById(PublicKey fileId) {
		return findById(root, fileId);
	}

	private FileTreeNode findById(FileTreeNode current, PublicKey fileId) {
		if (current.getKeyPair().getPublic().equals(fileId)) {
			return current;
		}

		FileTreeNode found = null;
		for (FileTreeNode child : current.getChildren()) {
			found = findById(child, fileId);
			if (found != null) {
				return found;
			}
		}
		return found;
	}

	public FileTreeNode getFileByPath(File file, FileManager fileManager) {
		Path relativePath = fileManager.getRoot().relativize(file.toPath());
		return getFileByPath(relativePath);
	}

	public FileTreeNode getFileByPath(Path relativePath) {
		String[] split = relativePath.toString().split(FileManager.getFileSep());
		FileTreeNode current = root;
		for (int i = 0; i < split.length; i++) {
			if (split[i].isEmpty()) {
				continue;
			}
			FileTreeNode child = current.getChildByName(split[i]);
			if (child == null) {
				return null;
			} else {
				current = child;
			}
		}

		return current;
	}
}