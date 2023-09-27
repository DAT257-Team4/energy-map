
public class ResultOfQuery {
    public String country;
    public String source;
    public int value;

    public ResultOfQuery(String country,String energyType){
        value = JDBCQuery.SqlQuery(country, energyType);
    }
}
