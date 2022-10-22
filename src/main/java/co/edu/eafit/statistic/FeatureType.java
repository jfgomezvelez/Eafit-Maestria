package co.edu.eafit.statistic;

public enum FeatureType {

    COMMANDMESSAGE("Command-Message");

    private final String type;

    FeatureType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }
}
