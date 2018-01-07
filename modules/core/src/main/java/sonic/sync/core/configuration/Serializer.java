package sonic.sync.core.configuration;

public class Serializer {
	private String mode;
	private FST fst;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public FST getFst() {
		return fst;
	}
	
	public void setFst(FST fst) {
		this.fst = fst;
	}

	public class FST {
		private boolean unsafe;

		public boolean isUnsafe() {
			return unsafe;
		}

		public void setUnsafe(boolean unsafe) {
			this.unsafe = unsafe;
		}
	}
}