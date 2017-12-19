package grimoire;

/**
 * Created by Camille K on 19/12/2017.
 */

public class Recipe {
    private String difficulty;
    private String protocol;
    private String[] symptoms;
    private boolean discovered;

    //TODO : Heuu constructeur de Item ici aussi, modifier
    //ArrayList Item peut être?

    public Recipe(String difficulty, String[] symptoms, String protocol){
        this.difficulty = difficulty;
        this.protocol = protocol;
        this.symptoms = symptoms;
        //TODO : à vérifier
        this.discovered = false;
    }
}
