package com.masterJugglers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.util.Assert;


public class ChunkingTaskExecutorService extends AbstractExecutorService {

	private final int chunkSize;
	private ArrayList<Runnable> currentChunk;
	private ExecutorService executorService;

	public ChunkingTaskExecutorService(ExecutorService executorService, int chunkSize) {
		Assert.notNull(executorService, "executorService must not be null");
		Assert.isTrue(chunkSize > 0, "chunkSize must be greater than zero");

		this.executorService = executorService;
		this.chunkSize = chunkSize;
		initCurrentChunk();
	}

	private void initCurrentChunk() {
		this.currentChunk = new ArrayList<Runnable>(chunkSize);
	}

	public int getCurrentChunkSize() {
		return currentChunk.size();
	}

	public void execute(Runnable task) {
		currentChunk.add(task);

		if(getCurrentChunkSize() == chunkSize) {
			executeCurrentChunk();
		}
	}

	public void shutdown() {
		executeCurrentChunk();
		executorService.shutdown();
	}

	public void executeCurrentChunk() {
		int currentSize = getCurrentChunkSize();

		if (currentSize == 0) {
			return;
		}

		final List<Runnable> chunk = currentChunk;

		initCurrentChunk();

		executorService.execute(new Runnable() {
			public void run() {
				for (Runnable task : chunk) {
					task.run();
				}
			}
		});
	}

	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return executorService.awaitTermination(timeout, unit);
	}

	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	public boolean isTerminated() {
		return executorService.isTerminated();
	}

	public List<Runnable> shutdownNow() {
		return executorService.shutdownNow();
	}

}