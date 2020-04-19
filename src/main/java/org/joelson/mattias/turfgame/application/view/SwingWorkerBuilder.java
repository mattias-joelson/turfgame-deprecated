package org.joelson.mattias.turfgame.application.view;

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import javax.swing.SwingWorker;

class SwingWorkerBuilder<T, V> {
    
    @FunctionalInterface
    public interface DoInBackgroundSupplier<T> {
        T get() throws Exception;
    }
    
    private final DoInBackgroundSupplier<T> doInBackground;
    private Consumer<Future<T>> done;
    
    public SwingWorkerBuilder(DoInBackgroundSupplier<T> doInBackground) {
        this.doInBackground = Objects.requireNonNull(doInBackground, "Background can not be null"); //NON-NLS
    }
    
    public SwingWorkerBuilder<T, V> withDone(Consumer<Future<T>> done) {
        this.done = done;
        return this;
    }
    
    public SwingWorker<T, V> build() {
        return new DelegatingSwingWorker<>(doInBackground, done);
    }
    
    private static final class DelegatingSwingWorker<T, V> extends SwingWorker<T, V> {
    
        private final DoInBackgroundSupplier<T> doInBackground;
        private final Consumer<Future<T>> done;
    
        private DelegatingSwingWorker(DoInBackgroundSupplier<T> doInBackground, Consumer<Future<T>> done) {
            this.doInBackground = doInBackground;
            this.done = done;
        }
    
        @Override
        protected T doInBackground() throws Exception {
            return doInBackground.get();
        }
        
        @Override
        protected void done() {
            if (done != null) {
                done.accept(this);
            } else {
                super.done();
            }
        }
    }
}
