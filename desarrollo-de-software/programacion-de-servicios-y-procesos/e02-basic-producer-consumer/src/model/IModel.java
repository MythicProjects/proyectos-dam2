package model;

import java.util.List;

public interface IModel {
    void start(Config config);
    void stop();
    boolean isRunning();
    List<Resource> getResources();
}
