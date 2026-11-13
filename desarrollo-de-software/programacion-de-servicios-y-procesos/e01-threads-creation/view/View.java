package view;

public class View implements IView, Runnable{

    private int testNum;

    @Override 
    public void start(){
        Thread view = new Thread();
        view.start();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(testNum); // always 0
        }
    }

    public void setResult(int testNum){
        this.testNum = testNum;
    }
}
