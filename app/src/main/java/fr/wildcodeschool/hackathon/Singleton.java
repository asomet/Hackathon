package fr.wildcodeschool.hackathon;

class Singleton {

    private static final Singleton ourInstance = new Singleton();

    private LoginModel logModel = null;

    public LoginModel getLogModel() {
        return logModel;
    }

    public void setLogModel(LoginModel logModel) {
        this.logModel = logModel;
    }

    static Singleton getInstance() {

        return ourInstance;
    }

    private Singleton() {
    }

    public void singleClear(){
        setLogModel(null);
    }
}
