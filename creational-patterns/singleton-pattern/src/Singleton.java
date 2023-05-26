public class Singleton {
    private static volatile Singleton instance;
    private String data; //Example data. It can be anything.

    private Singleton(String data){
        this.data = data;
    }

    public static Singleton getInstance(String data){
        Singleton localInstance = instance;
        if(localInstance == null){
            synchronized (Singleton.class){
                localInstance = instance;
                if(localInstance == null){
                    instance = localInstance = new Singleton(data);
                }
            }
        }
        return localInstance;
    }
}
