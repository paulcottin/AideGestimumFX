package interfaces;

import java.io.File;
import java.util.ArrayList;

import exceptions.ParametrageError;

public interface LancerAction extends Runnable, LongTask, NeedSelectionFichiers {

	public void lancerActionAll() throws ParametrageError;
	public void lancerAction(ArrayList<File> files);
	public void parametrer() throws ParametrageError;
	public void reloadFiles(ArrayList<File> files);
}
