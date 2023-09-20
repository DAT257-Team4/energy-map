public class ResultOfQuery {
    public String country;
    public String source;
    public int value;

    public ResultOfQuery(String country,String energyType){
        value=10;  // @TODO
        this.country=country;
        source=energyType;
    }
}
