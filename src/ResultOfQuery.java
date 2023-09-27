
public class ResultOfQuery {
    //Possibly redundant
    public String country;
    //Possibly redundant
    public String source;
    public int value;

    public ResultOfQuery(String country,String source){
        this.country = country;
        this.source = source;
        value = JDBCQuery.SqlQuery(country, source);
    }
}
