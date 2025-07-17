package hi.muses.happy;

/**
 * Hello world!
 */
public final class App {

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        // Human zhangsan = new Human();
        // zhangsan.setName("Zhang san");
        // zhangsan.talk();
        // zhangsan.handsCount = "2";
        // System.out.println(zhangsan.handsCount);
        
        // Human lisi = new Human();
        // lisi.setName("Li si");
        // lisi.talk();
        // lisi.handsCount = "1";
        // System.out.println(zhangsan.handsCount);
        
        SomeInterface bird = new Bird();
        bird.doSomething("butterfly");
        
        SomeInterface human = new Human();
        human.doSomething("computer");
    }

}


