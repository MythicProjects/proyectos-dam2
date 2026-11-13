# Producer-Consumer Simulator

Java Swing (OpenJDK 17), MVP. Producers and consumers are threads.

## Run

```
rm -rf out
javac -d out $(find src -name "*.java")
java -cp out Main
```

## Structure

- `model/`: Config, Resource, Producer, Consumer, IModel, Simulation
- `view/`: IView, View, dto/Settings, dto/ResourceRow
- `presenter/`: IPresenter, Presenter
- `Main.java`
