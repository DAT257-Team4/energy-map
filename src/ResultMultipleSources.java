public class ResultMultipleSources {
    public String country;
    public int[] values;
    public final static String[] ENERGY_SOURCES={"Mixed", "Generation", "Load", "Biomass", "Fossil Brown coal/Lignite",
            "Fossil Coal-derived gas", "Fossil Gas", "Fossil Hard coal", "Fossil Oil", "Fossil Oil shale", "Fossil Peat",
            "Geothermal", "Hydro Pumped Storage", "Hydro Run-of-river and poundage", "Hydro Water Reservoir", "Marine",
            "Nuclear", "Other renewable", "Solar", "Waste", "Wind Offshore", "Wind Onshore", "Other", "AC Link",
            "DC Link", "Substation", "Transformer"};


    public ResultMultipleSources(String country){
        // @TODO
        values=new int[]{909, 142, 378, 524, 303, 225, 155, 225, 543, 644, 916, 107, 917, 958, 617, 514, 130, 409, 610,
                702, 439, 121, 574, 59, 487, 707, 650};
        this.country=country;
    }
}
