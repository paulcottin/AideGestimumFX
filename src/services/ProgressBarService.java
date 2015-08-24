package services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ProgressBarService extends Service<Boolean>{

	Task<Boolean> task;
	
	public ProgressBarService(Task<Boolean> task) {
		this.task = task;
	}
	
	@Override
	protected Task<Boolean> createTask() {
		return task;
	}

}
