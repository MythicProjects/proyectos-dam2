package model;

public class Model {

    public void execute(){
        Resource resource = new Resource(0);
        Producer producer = new Producer(resource, 10000);
        Consumer consumer = new Consumer(resource, 10000);

        Thread h1 = new Thread(producer, "Producer");
        Thread h2 = new Thread(consumer, "Consumer");

        h1.start();
        h2.start();

        System.out.println(resource.getProduct());
    }   
}
