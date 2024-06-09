package org.joelson.turf.application.view;

import javax.swing.SwingWorker;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.function.Consumer;

class SwingWorkerBuilder<T, V> {

    private final DoInBackgroundSupplier<T> doInBackground;
    private Consumer<Future<T>> done;

    public SwingWorkerBuilder(DoInBackgroundSupplier<T> doInBackground) {
        this.doInBackground = Objects.requireNonNull(doInBackground, "Background can not be null");
    }

    public SwingWorkerBuilder<T, V> withDone(Consumer<Future<T>> done) {
        this.done = done;
        return this;
    }

    public SwingWorker<T, V> build() {
        return new DelegatingSwingWorker<>(doInBackground, done);
    }

    @FunctionalInterface
    public interface DoInBackgroundSupplier<T> {
        T get() throws Exception;
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
