package interfaces;

public interface LongTask {
	
	public boolean isRunning();
	public void setRunning(boolean b);
	public void onProgressBarDispose();
	public String getFichierTraitement();
	public String getTitre();
}
