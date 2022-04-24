package clientAndServer.tools.insideCommands;

import clientAndServer.startingData.*;

import java.time.LocalDate;
import java.util.Date;

public class MovieBuilder {

    public Movie build(){
        var newElementBuilder = Movie.builder();
        UserInput userInput = new UserInput();
        boolean fCreatedObj = false;
        Movie movie = null;
        while (!fCreatedObj) {
            newElementBuilder.id(-1);
            newElementBuilder.creationDate(LocalDate.now());
            String nameOfElement = userInput.getString("Enter name:");
            newElementBuilder.name(nameOfElement);
            double x = userInput.getDoubleX("Enter x coordinate (it can be only double):");
            float y = userInput.getFloatY("Enter y coordinate (it can be only double):");
            newElementBuilder.coordinates(new Coordinates(x, y));
            long oscarsCount = userInput.getLong("Enter oscars count (it can be only long): ");
            newElementBuilder.oscarsCount(oscarsCount);
            MovieGenre genre = userInput.getMovieGenre("Enter movie genre.\nChoose ACTION, MUSICAL or TRAGEDY:");
            newElementBuilder.genre(genre);
            MpaaRating mpaaRating = userInput.getMpaaRating("Enter mpaa rating. \nChoose G, PG, PG_13 or R:");
            newElementBuilder.mpaaRating(mpaaRating);
            String dirName = userInput.getString("Enter director's name:");
            Date birthday = userInput.getDate("Enter birthday of director (pattern \"dd-mm-yyyy\" (day-month-year)): ");
            EyeColor dirEyes = userInput.getEyeColor("Enter color of director's eyes.\nChoose GREEN, BLUE, ORANGE or BROWN:");
            HairColor dirHairs = userInput.getHairColor("Enter color of director's hairs.\nChoose RED, BLUE, ORANGE or BROWN:");
            Country country = userInput.getCountry("Enter nationality of director.\nChoose ITALY, SOUTH_KOREA or JAPAN");
            newElementBuilder.director(new Person(dirName, birthday, dirEyes, dirHairs, country));
            movie = newElementBuilder.build();
            fCreatedObj = true;
        }
        return movie;
    }
}
