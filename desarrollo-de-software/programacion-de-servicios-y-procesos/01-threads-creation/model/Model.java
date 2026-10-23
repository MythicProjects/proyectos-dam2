package model;

public class Model implements IModel{

    @Override
    public void execute() {

        Resource resource = new Resource(0);
        Thread producer = new Thread(new Producer(resource, 10_000), "Producer");
        Thread consumer = new Thread(new Consumer(resource, 10_000), "Consumer");

        producer.start();
        consumer.start();

        //producer.join();
        //consumer.join();


        System.out.println(resource.getProduct());
    }   
}
