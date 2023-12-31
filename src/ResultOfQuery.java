
public class ResultOfQuery {
    public int value;

    /**
     * Object that holds the value of a single energy source of a single country.
     * Automatically queries the database on creation.
     * @param country country to be queried
     * @param source energy type to be queried
     */
    public ResultOfQuery(String country,String source,String tab){
        updateValue(country.toLowerCase(), source,tab);
    }

    /**
     * Queries the database to update the value held
     * @param country country to be queried
     * @param source energy type to be queried
     */
    public void updateValue(String country, String source,String tab) {
        value = JDBCQuery.SqlQuery(country.toLowerCase().trim(), source.toLowerCase().trim(), DBupdate.dbURL,tab);
    }
}
